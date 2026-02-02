# Dev Guide

## 프로젝트 목표

- 프로세스를 처리하는 BPM(Business Process Management)를 구현한다.
- 신청 - 승인 프로세스의 화면을 schema로 서버에서 내려주어 프론트를 자동 생성한다.
- 행정지원 시스템을 개발하고 느낀점을 여기에서 해결해본다.
- 저장 - 신청 - 접수 - 접수반려 - 검토 - 검토반려 - 승인 등 다양한 프로세스를 구현한다.
- mybatis, mysql, spring-boot 등 익숙하지 않은 기술을 익힌다.

## 기술 스택

- Spring Boot
- MyBatis
- MySQL
- Camunda BPM 7 (embedded)
- Lombok

## 프로젝트 기본 정보

- Backend는 `backend/`에 위치.
- 기본 포트: `9090` (`backend/src/main/resources/application.properties`).
- DB 스키마/시드:
  - `backend/src/main/resources/db/schema.sql`
  - `backend/src/main/resources/data.sql`

## 로컬 실행

1) MySQL 실행 (docker compose 예시):

```
docker compose -f backend/docker/mysql/docker-compose.yml up -d
```

2) 애플리케이션 실행:

```
./gradlew bootRun
```

기본 URL: `http://localhost:9090`

## BPM / Camunda 7 흐름

- Camunda Modeler로 BPMN 작성
- 작성한 BPMN을 Camunda Engine에 배포
- 백엔드 서버와 Camunda Engine을 연동
- 신청 - 접수 - 승인 프로세스는 Camunda Modeler가 처리
- 백엔드는 신청 입력 폼, 접수 처리 내역 등 DB 저장을 담당

대략적인 구조:

```
Spring Boot (API 서버)
 ├─ ApplicationController
 ├─ ApplicationService (비즈니스)
 ├─ BpmService (Camunda 연동)
 └─ Camunda Engine (embedded)
```

의존성 예시:

```yml
dependencies {
    implementation 'org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter:7.21.0'
    implementation 'org.springframework.boot:spring-boot-starter-web'
}
```

## 프론트 자동 생성 (Form Schema)

- 신청 - 처리 - 승인 등 다양한 프로세스별 화면을 서버에서 schema로 제공
- 프론트는 schema를 받아 자동으로 form을 생성
- form.io는 JSON schema 형태로 시작

Form builder 참고:
```
https://formio.github.io/formio.js/app/builder.html
```

## Sports Club Flow (Operating Sports)

- 운영 종목 공통코드는 `common_code` 테이블의 그룹 `OPERATING_SPORT`에 저장
- 일반 스포츠클럽 API:
  - `categoryIds` (common_code.id 리스트)
  - `sports_club_category`에 저장
- 등록 스포츠클럽 신청:
  - `operatingSportParentCodeId` / `operatingSportChildCodeId` (legacy)
  - `operatingSportCodeIds` (common_code.id 리스트)
  - `reg_sports_club_application_category`에 저장
- 승인 시 신청 카테고리를 `sports_club_category`로 복사

## 서버 구조 / 인증 처리

- session 기반 인증 (JSESSIONID cookie)
- 인터셉터 제외:
  - `/auth/login`, `/auth/register`, Swagger, error endpoints

`AuthorizationInterceptor` 예시:

```java
package org.delivery.api.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.error.TokenErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.api.domain.token.business.TokenBusiness;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final TokenBusiness tokenBusiness;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("@ Authorization Interceptor URL: {}", request.getRequestURI());

        // WEB, chrome 의 경우 GET, POST OPTIONS = pass
        if(HttpMethod.OPTIONS.matches(request.getMethod())){
            return true;
        }

        // js. html. png resource 를 요청하는 경우 = pass
        if(handler instanceof ResourceHttpRequestHandler){
            return true;
        }

        var accessToken = request.getHeader("authorization-token");
        if(accessToken == null) {
            throw new ApiException(TokenErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
        }

        var userId = tokenBusiness.validationAccessToken(accessToken);

        if(userId != null) {
            // 한가지 요청에 대해 스레드 로컬에 저장함
            var requestContext = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
            requestContext.setAttribute("userId", userId, RequestAttributes.SCOPE_REQUEST);
            return true;
        }

        throw new ApiException(ErrorCode.BAD_REQUEST, "인증 실패");
    }
}
```

`UserSessionResolver` 예시:

```java
package org.delivery.api.resolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.delivery.api.common.annotation.UserSession;
import org.delivery.api.domain.user.converter.UserConverter;
import org.delivery.api.domain.user.model.User;
import org.delivery.api.domain.user.service.UserService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 위치: 스프링 MVC 컨트롤러 레벨 스프링의 컨트롤러 메서드의 파라미터를 자동으로 바인딩해주는 기능을 제공
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserSessionResolver implements HandlerMethodArgumentResolver {

    private final UserService userService;
    private final UserConverter userConverter;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 1. 어노테이션 체크
        var annotation = parameter.hasParameterAnnotation(UserSession.class);

        // 2. 파라미터 타입 체크
        var parameterType = parameter.getParameterType().equals(User.class);

        log.info("@ annotation && parameterType : {}", annotation && parameterType);
        return (annotation && parameterType);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        var requestContext = RequestContextHolder.getRequestAttributes();
        var userId = requestContext.getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        log.info("@ userId : {}", userId.toString());

        var userEntity = userService.getUserWithThrow(Long.parseLong(userId.toString()));
        log.info("@ userEntity : {}", userEntity.toString());

        return User.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .status(userEntity.getStatus())
                .password(userEntity.getPassword())
                .address(userEntity.getAddress())
                .registeredAt(userEntity.getRegisteredAt())
                .unregisteredAt(userEntity.getUnregisteredAt())
                .lastLoginAt(userEntity.getLastLogin())
                .build();
    }
}
```

ErrorCode 예시:

```java
package org.delivery.api.common.error;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode implements ErrorCodeIfs {

    OK(200, 200, "성공"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), 400, "잘못된 요청"),

    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, "서버에러"),

    CONVERT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, "Entity 변환 실패 에러"),

    NULL_POINT(HttpStatus.INTERNAL_SERVER_ERROR.value(), 512, "Null point");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
```

## 권한 / 역할 / 조직 설계

### 핵심 결론

- 권한은 역할(Role)로 본다.
- 역할(Role)과 조직(Organization)은 반드시 분리한다.
- 실제 권한 판단은 (사용자 + 조직 + 역할) 조합으로 결정한다.
- 권한 추가는 가능하지만 영향이 자동으로 퍼지지 않도록 설계한다.

### 개념 정의

#### Role (역할)

- 사용자가 무엇을 할 수 있는가
- 행위 단위 (Action 수행 가능 여부)
- 예:
  - `APPLICANT`
  - `RECEIPT_MANAGER`
  - `REVIEWER`
  - `APPROVER`

> “지자체”, “체육회”, “클럽” 같은 개념은 Role이 아님

#### Organization (조직)

- 사용자가 어디에 속해 있는가
- 역할이 행사되는 맥락(Context)
- 예:
  - 대한체육회
  - 서울시 체육회
  - 강남구 체육회
  - OO 스포츠클럽

#### User

- 하나의 사용자
- 여러 조직에 소속될 수 있음
- 조직마다 서로 다른 Role을 가질 수 있음

### 테이블 설계 (권장 구조)

```sql
organization (
  id,
  org_type,        -- CLUB, LOCAL_GOV, KOC 등
  org_name,
  parent_org_id
)
```

## 인코딩 / DB 설정 메모

- JDBC `characterEncoding`은 `UTF-8`을 사용
- DB collation: `utf8mb4_unicode_ci`

DB 리셋 (Docker MySQL):

```
docker exec -i mysql mysql -uroot -proot1234! --default-character-set=utf8mb4 -e "DROP DATABASE IF EXISTS mydb; CREATE DATABASE mydb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
cmd /c "docker exec -i mysql mysql -uroot -proot1234! --default-character-set=utf8mb4 mydb < backend\\src\\main\\resources\\db\\schema.sql"
cmd /c "docker exec -i mysql mysql -uroot -proot1234! --default-character-set=utf8mb4 mydb < backend\\src\\main\\resources\\data.sql"
```

## Appendix: BPM / Schema 예시

### Controller / Service 예시

```java
@RestController
@RequiredArgsConstructor
@RequestMapping("/applications")
public class ApplicationController {

    private final BpmService bpmService;

    @PostMapping("/{id}/start")
    public void start(@PathVariable Long id) {
        bpmService.startProcess(id);
    }

    @PostMapping("/{id}/actions")
    public void action(
        @PathVariable Long id,
        @RequestBody ActionRequest req
    ) {
        bpmService.completeTask(id, req.action());
    }

    @GetMapping("/{id}/state")
    public String state(@PathVariable Long id) {
        return bpmService.getCurrentState(id);
    }
}

record ActionRequest(String action) {}
```

```java
@Service
@RequiredArgsConstructor
public class BpmService {

  private final RuntimeService runtimeService;
  private final TaskService taskService;

  /** 프로세스 시작 */
  public String startProcess(Long applicationId) {
    ProcessInstance instance =
        runtimeService.startProcessInstanceByKey(
            "applicationProcess",
            Map.of("applicationId", applicationId)
        );
    return instance.getId();
  }

  /** 액션 수행 (approve / receipt 등) */
  public void completeTask(Long applicationId, String action) {
    Task task = taskService.createTaskQuery()
        .processVariableValueEquals("applicationId", applicationId)
        .taskDefinitionKey(action)
        .singleResult();

    if (task == null) {
      throw new IllegalStateException("해당 상태에서 수행 불가능한 액션");
    }

    taskService.complete(task.getId());
  }

  /** 현재 상태 조회 */
  public String getCurrentState(Long applicationId) {
    Task task = taskService.createTaskQuery()
        .processVariableValueEquals("applicationId", applicationId)
        .singleResult();

    return task != null ? task.getTaskDefinitionKey() : "END";
  }
}
```

### BPMN 예시

```xml
<?xml version="1.0" encoding="UTF-8"?>
<bpmn:definitions xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL"
  xmlns:camunda="http://camunda.org/schema/1.0/bpmn"
  targetNamespace="http://example.com/bpm">

  <bpmn:process id="applicationProcess" name="신청 프로세스" isExecutable="true">

    <bpmn:startEvent id="start" />

    <bpmn:userTask id="apply" name="신청">
      <bpmn:outgoing>flow1</bpmn:outgoing>
    </bpmn:userTask>

    <bpmn:userTask id="receipt" name="접수">
      <bpmn:incoming>flow1</bpmn:incoming>
      <bpmn:outgoing>flow2</bpmn:outgoing>
    </bpmn:userTask>

    <bpmn:userTask id="approve" name="승인">
      <bpmn:incoming>flow2</bpmn:incoming>
    </bpmn:userTask>

    <bpmn:sequenceFlow id="flow1" sourceRef="apply" targetRef="receipt"/>
    <bpmn:sequenceFlow id="flow2" sourceRef="receipt" targetRef="approve"/>

  </bpmn:process>
</bpmn:definitions>
```

### Form Schema 예시 (발췌)

```json
{
    "display": "form",
    "settings": {
        "pdf": {
            "id": "1ec0f8ee-6685-5d98-a847-26f67b67d6f0",
            "src": "https://files.form.io/pdf/5692b91fd1028f01000407e3/file/1ec0f8ee-6685-5d98-a847-26f67b67d6f0"
        }
    },
    "components": [
        {
            "label": "Name",
            "applyMaskOn": "change",
            "tableView": true,
            "validate": {
                "required": true,
                "minLength": 10,
                "maxLength": 100
            },
            "unique": true,
            "validateWhenHidden": false,
            "key": "name",
            "type": "textfield",
            "input": true
        },
        {
            "label": "Age",
            "applyMaskOn": "change",
            "tableView": true,
            "validate": {
                "minLength": 10,
                "maxLength": 100
            },
            "validateWhenHidden": false,
            "errorLabel": "Error Occured!",
            "key": "age",
            "type": "textfield",
            "input": true
        },
        {
            "label": "Checkbox 2",
            "tableView": false,
            "validateWhenHidden": false,
            "key": "checkbox2",
            "type": "checkbox",
            "input": true,
            "defaultValue": false
        }
    ]
}
```

### 응답 payload 구조 예시

```json
{
  "applicationId": 123,
  "currentState": "REVIEW",
  "availableActions": [
    { "key": "approve", "label": "승인" },
    { "key": "reject", "label": "반려" }
  ],
  "formSchema": {  },
  "components": [
    {
       "id": "sortNo",
       "type": "number",
       "order": 1,
       "label": "정렬순서",
       "required": true,
       "readonly": false,
       "value": "3",
       "ui": {
           "size": "sm"
       }
    }
  ],
  "availableActions": [
    {
      "type": "cta",
      "key": "approve",
      "label": "승인",
      "variant": "primary",
      "confirm": {
        "message": "승인하시겠습니까?"
      }
    },
    {
      "type": "cta",
      "key": "reject",
      "label": "반려",
      "variant": "danger",
      "schema": {
        "fields": [
          {
            "id": "reason",
            "type": "textarea",
            "required": true
          }
        ]
      }
    }
  ]
}
```
