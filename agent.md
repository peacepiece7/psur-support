# 프로젝트 설명

이 프로젝트는 다음 기능을 구현하는 것을 목표로함

- 프로세스를 처리하는 BPM(Business Process Management)를 구현하는 것
- 신청 - 승인 프로세스의 화면을 schema로 서버에서 내려주어, 프론트를 자동 생성하는 기능을 구현하는 것
- 행정지원 시스템을 개발하고 느낀점을 여기에서 해결해보는 것
- 저장 - 신청 - 접수 - 접수반려 - 검토 - 검토반려 - 승인 등 다양한 프로세스를 구현하는것
- mybatis, mysql, spring-boot 등 익숙하지 않은 기술을 익히는 것

## BPM 구현에 대한 상세 설명

Camunda Modeler를 사용해서 BMPN를 작성한다.
작성한 BPMN 파일을 Camunda Engine에 배포한다.
이 백엔드 서버와 Camunda Engine을 연동한다.
신청 - 접수 - 승인 프로세스를 Camunda Modeler가 처리한다.
백엔드 서버에서는 신청 입력 폼, 접수 처리 내역 등 DB를 만들고 거기에 데이터를 저장하기만 하고
신청 관련 프로세스는 Camunda Engine이 처리한다.

대락적으로 다음과 같이 흘러할 것이다.

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


BmpSerivce는 다음과 같은 구조일 것이다.
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
      throw new IllegalStateException("해당 상태에서 수행 불가한 액션");
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

BPMN 파일은 이런 형태의 XML 일 것이다
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

전체적인 구조는 단간하게 표현화면 다음과 같다.

```txt
Spring Boot (API 서버)
 ├─ ApplicationController
 ├─ ApplicationService (비즈니스)
 ├─ BpmService (Camunda 어댑터)
 └─ Camunda Engine (로컬, embedded)
```

camunda bpm은 7버전을 사용해서 설치한다.

```yml
dependencies {
    implementation 'org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter:7.21.0'
    implementation 'org.springframework.boot:spring-boot-starter-web'
}
```

## 프론트를 자동 생성하는 기능 구현

신청 - 처리 - 승인 등등 다양한 프로세스마다 화면에 표기되어야하는 schema를 서버에서 내려준다.

프론트엔드 프로젝트에서 schema를 이어받아 form을 완성한다.

form.io와 똑같은 json schema 형태에서, 부족하게 시작한다.

https://formio.github.io/formio.js/app/builder.html 여기서 처름에는 신청 화면을 만들어보고 

해당 결과를 json 형태로 db에 저장한다.

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
        },
        {
            "label": "Checkbox 1",
            "tableView": false,
            "validateWhenHidden": false,
            "key": "checkbox1",
            "type": "checkbox",
            "input": true,
            "defaultValue": false
        },
        {
            "label": "Checkbox 3",
            "tableView": false,
            "validateWhenHidden": false,
            "key": "checkbox3",
            "type": "checkbox",
            "input": true,
            "defaultValue": false
        },
        {
            "label": "Text Field",
            "applyMaskOn": "change",
            "tableView": true,
            "validateWhenHidden": false,
            "key": "textField",
            "type": "textfield",
            "input": true
        },
        {
            "label": "Address",
            "tableView": false,
            "provider": "google",
            "validateWhenHidden": false,
            "key": "address",
            "type": "address",
            "providerOptions": {
                "params": {
                    "autocompleteOptions": {},
                    "key": "3221asdasd"
                }
            },
            "input": true,
            "components": [
                {
                    "label": "Address 1",
                    "tableView": false,
                    "key": "address1",
                    "type": "textfield",
                    "input": true,
                    "customConditional": "show = _.get(instance, 'parent.manualMode', false);"
                },
                {
                    "label": "Address 2",
                    "tableView": false,
                    "key": "address2",
                    "type": "textfield",
                    "input": true,
                    "customConditional": "show = _.get(instance, 'parent.manualMode', false);"
                },
                {
                    "label": "City",
                    "tableView": false,
                    "key": "city",
                    "type": "textfield",
                    "input": true,
                    "customConditional": "show = _.get(instance, 'parent.manualMode', false);"
                },
                {
                    "label": "State",
                    "tableView": false,
                    "key": "state",
                    "type": "textfield",
                    "input": true,
                    "customConditional": "show = _.get(instance, 'parent.manualMode', false);"
                },
                {
                    "label": "Country",
                    "tableView": false,
                    "key": "country",
                    "type": "textfield",
                    "input": true,
                    "customConditional": "show = _.get(instance, 'parent.manualMode', false);"
                },
                {
                    "label": "Zip Code",
                    "tableView": false,
                    "key": "zip",
                    "type": "textfield",
                    "input": true,
                    "customConditional": "show = _.get(instance, 'parent.manualMode', false);"
                }
            ]
        },
        {
            "label": "Columns",
            "columns": [
                {
                    "components": [
                        {
                            "label": "Text Field 1",
                            "applyMaskOn": "change",
                            "tableView": true,
                            "validateWhenHidden": false,
                            "key": "textField2",
                            "type": "textfield",
                            "input": true
                        }
                    ],
                    "width": 6,
                    "offset": 0,
                    "push": 0,
                    "pull": 0,
                    "size": "sm",
                    "currentWidth": 6
                },
                {
                    "components": [
                        {
                            "label": "Text Field 2",
                            "applyMaskOn": "change",
                            "tableView": true,
                            "validateWhenHidden": false,
                            "key": "textField3",
                            "type": "textfield",
                            "input": true
                        }
                    ],
                    "width": 6,
                    "offset": 0,
                    "push": 0,
                    "pull": 0,
                    "size": "md",
                    "currentWidth": 6
                }
            ],
            "key": "columns",
            "type": "columns",
            "input": false,
            "tableView": false
        },
        {
            "label": "Radio",
            "optionsLabelPosition": "right",
            "inline": false,
            "tableView": false,
            "values": [
                {
                    "label": "Foo",
                    "value": "A",
                    "shortcut": ""
                },
                {
                    "label": "Bar",
                    "value": "B",
                    "shortcut": ""
                },
                {
                    "label": "Barz",
                    "value": "C",
                    "shortcut": ""
                }
            ],
            "validate": {
                "required": true
            },
            "validateWhenHidden": false,
            "key": "radio",
            "type": "radio",
            "input": true
        },
        {
            "label": "Select 1",
            "widget": "choicesjs",
            "tableView": true,
            "validateWhenHidden": false,
            "key": "select1",
            "type": "select",
            "input": true
        },
        {
            "label": "Select 2",
            "widget": "choicesjs",
            "tableView": true,
            "validateWhenHidden": false,
            "key": "select2",
            "type": "select",
            "input": true
        },
        {
            "label": "Add",
            "showValidations": false,
            "tableView": false,
            "key": "add",
            "type": "button",
            "input": true,
            "saveOnEnter": false
        },
        {
            "type": "button",
            "label": "Submit",
            "key": "submit",
            "disableOnInvalid": true,
            "input": true,
            "tableView": false
        }
    ]
}
```
이후 고도화 작업시
form builder를 만들고 form builder에서 만든 schema를 서버에 저장하고 관리한다.
백엔드 서버는 특정 스키마아이디만 내려주는 API를 구현하면 된다.

아래 예시처럼 꼭 components만 오는건 아니고 다양한 메타정보와 활용가능한 액션도한 정의하여 내려준다.


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
       id: 'sortNo',
       type: 'number',
       order: 1,
       label: '정렬순서',
       required: true,
       readonly: false,
       value: '3',
       ui: {
           size: "sm"
       }
    },
    {
	  "id": "clubId",
	  "type": "select",
	  "label": "스포츠클럽",
	  "required": true,
	  "optionSource": {
	    "kind": "api",
	    "request": {
	      "method": "GET",
	      "url": "/api/sports-clubs"
	    },
	    "map": {
	      "label": "name",
	      "value": "id"
	    },
	    "filter": {
	      "include": [],
	      "exclude": []
	    }
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

---
## 프로젝트 기술 및 구현 과제

spring-boot, mysql, mybatis, camunda bpm 7, lombok 정도를 사용한다.

서버개발쪽인 이해도가 부족해서 기능을 계속 추가해 나갈 예정이다.

mysql 서버는 docker/docker-compose로 올려서 사용중이다.

앞으로 구현해야할 일은 다음과 같다.

### 1. ERD 만들기

행정 시스템은 다음기능이 필요한다

**사용자 테이블** 
(id, username, email, telno, password, createdAt, updatedAt, deletedAt, ci) 컬럼이 있고 
토이프로젝트이기때문에 소셜로그인은 고려하지 않는다.

---

**공통코드 테이블**
여기에 들어있는 데이터는 관리자가 추가 삭제할 수 있다.

code, codeName, groupName, groupCode, hrnkGroupCode, description, createdAt, updatedAt, deletedAt 이런 컬럼을 가지고 있다.

공통코드는 내가 DB쪽을 잘 몰라서 그러는데 상위 - 하위 코드 그룹의 개념이 존재햐아한다.

예르를들어 시도 - 시군구 - 읍면동 이런식으로 게층구조가 있을 경우 공통코드 테이블에서는 그룹코드, 상위그룹코드 이런식으로 계층구조를 표현할 수 있어야 한다.
여기에 필요한 컬럼이 무엇이 있어야하는지는 잘 모르겟다. (이 부분은 AI의 도움이 필요하다.)

아래와 같은 권한이 앞으로 추가 될것이다.

**사용자 권한 테이블**
사용자의 권한은 다음 종류가 있다. 
사용자의 권한: '비회원', '일반 회원', '행정 관리자', '클럽 관리자', '지도자'

이런 식으로 쓸 거다.
  행정 관리자 -> 포털, 행정 사이트에 접근 가능
  클럽 괸리자 -> 포털, 클럽 관리 사이트 접근 가능
  지도자 -> 포털, 행정사이트에 접근가능
  일반회원 -> 포털 접근가능
  비회원 -> 포털 접근가능 (비회원 포털화면만 볼 수 있음)

**스포츠클럽 권한 테이블**
스포츠클럽은 다음 권한을 가진다
- 미지정 스포츠크럽 권한 (아직 권한을 부여받지 못한 일반 스포츠클럽)
- 지정스포츠클럽 권한 ("지정"이라는 권한이 있는 스포츠클럽)
- 등록스포츠클럽 권한 ("등록"이라는 권한이 있는 스포츠클럽)
- 예비지정스포츠클럽 권한 ("에비지정"이라는 권한이 있는 스포츠클럽)

**운영 종목 테이블**
축구 - 축구
축구 - 족구
축구 - 풋살
댄스스포츠 - 라틴댄스
댄스스포츠 - 스포츠댄스
육상 - 달리기 - 100m 달리기
육상 - 달리기 - 1000m 달리기
...

이런식으로 계층구조로 운영종목을 표현해야 한다.

---

**스포츠클럽 정보 테이블**
스포츠크럽이름, 위치, 대표자, 고유번호, 대표자 연락처, 스포츠클럽 권한

여기서 고유번호는 사업자번호또는 고유한 스포츠클럽 번호이다.
스포츠클럽 권한은 "스포츠클럽 권한" 공통 코드에 있는 권한 중 하나이다.

---

### 2. 기능 구현 - MyBatis 설명 및 CRUD 기능

MyBatis를 사용해서 CRUD 기능 작성이 필요하다.

### 3. 기능 구현 - Swagger 설정

스웨거 설정을 통해 API 문서를 자동으로 생성하고 관리할 수 있도록 한다.

프론트에서 API 명세서(Open API Sepecification)를 통해 DTO를 type화 할 것이기 떄문에 설명과 DTO가 API에 잘 붙어 있어야한다.

### 4. 기능 구현 - 로그인

로그인 기능이 우선 필요하다.

화면은 프론트에서 만들 것이므로 서버에서는 API만 제공하면 된다.

회원가입, 로그인, 로그아웃, 비밀번호 재설정, 회원정보 수정 등의 기능이 필요하다.

### 5. DB 추가 - ERD에 공통 코드 추가

위에 공통 테이블에 대한 설명이 있는데, 공통코드를 추가해야한다.

그리고 각 공통코드는 호출시 그룹코드로 상위 하위계층이 포함된 형태로 응답되는 API가 필요하다.

최대 상위 - 하위 표현은 3단계 까지 지원하도록 한다.

### 6. 그리고 앞으로 할 것들

일단 5번까지의 작업이 모두 끝나면 6번은 나중에 진행한다. (5번까지 테스트가 끝나고 프론트 화면이 완성되면 이어서 진행한다.)

Cammuda BPM과 연동하여 신청 - 승인 프로세스를 구현한다.

여기서는 일반 사용자가 포털사이트에서 "등록스포츠클럽 신청"을 할 수있는데 (이것이 승인되면 "등록스포츠클럽"권한을 획득한다.)

행정관리자는 행정사이트에서 "등록스포츠클럽 신청"을 접수하고 승인 또는 반려할 수 있다.

이것을 Cammunda로 만든 BPMN과 연동하여 진행한다.

## 프로젝트 희망 구조

API 마다 DTO를 하나씩 가지고 service - controller 형태로 개발을 해주길 바란다.

filter/loggerFilter와 exception/exceptionHandler도 공통으로 만들어서 사용하길 바란다.

interceptor/AuthorizationInterceptor 도 만들어서 다음과 같이 쓰고

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


resolver/CurrentUserResolver 도 만들어서 다음과 같이 쓴다.

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

common/api
common/error
common/exception 을 만들고


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
이런식으로 글로벌 에러를 정의한다
