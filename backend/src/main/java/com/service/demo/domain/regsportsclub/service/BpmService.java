package com.service.demo.domain.regsportsclub.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.service.demo.common.error.ErrorCode;
import com.service.demo.common.exception.ApiException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BpmService {
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String processDefinitionId;

    public BpmService(
            RestTemplate restTemplate,
            @Value("${camunda.orchestration.base-url}") String baseUrl,
            @Value("${camunda.orchestration.process-definition-id}") String processDefinitionId) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.processDefinitionId = processDefinitionId;
    }

    public String start() {
        String url = baseUrl + "/v2/process-instances";
        ProcessInstanceCreateRequest request = new ProcessInstanceCreateRequest();
        request.setProcessDefinitionId(processDefinitionId);
        request.setVariables(new HashMap<>());
        ProcessInstanceCreateResponse response =
                restTemplate.postForObject(url, request, ProcessInstanceCreateResponse.class);
        if (response == null || response.getProcessInstanceKey() == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Camunda process instance not created");
        }
        return response.getProcessInstanceKey();
    }

    public void update(String processTaskId, String action) {
        String searchUrl = baseUrl + "/v2/user-tasks/search";
        UserTaskSearchRequest searchRequest = new UserTaskSearchRequest();
        UserTaskFilter filter = new UserTaskFilter();
        filter.setProcessInstanceKey(processTaskId);
        filter.setState("CREATED");
        searchRequest.setFilter(filter);

        String userTaskKey = findUserTaskKeyWithRetry(searchUrl, searchRequest);
        if (userTaskKey == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "User task key not found");
        }

        String completeUrl = baseUrl + "/v2/user-tasks/" + userTaskKey + "/completion";
        UserTaskCompletionRequest completionRequest = new UserTaskCompletionRequest();
        Map<String, Object> variables = new HashMap<>();
        variables.put("action", action);
        completionRequest.setVariables(variables);
        restTemplate.postForEntity(completeUrl, completionRequest, Void.class);
    }

    private String findUserTaskKeyWithRetry(String searchUrl, UserTaskSearchRequest searchRequest) {
        int maxRetries = 10;
        long delayMs = 200L;
        for (int i = 0; i < maxRetries; i++) {
            UserTaskSearchResult searchResult =
                    restTemplate.postForObject(searchUrl, searchRequest, UserTaskSearchResult.class);
            if (searchResult != null && searchResult.getItems() != null && !searchResult.getItems().isEmpty()) {
                return searchResult.getItems().get(0).getUserTaskKey();
            }
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        throw new ApiException(ErrorCode.BAD_REQUEST, "Active user task not found");
    }

    @Getter
    @Setter
    private static class ProcessInstanceCreateRequest {
        private String processDefinitionId;
        private Map<String, Object> variables;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class ProcessInstanceCreateResponse {
        private String processInstanceKey;
    }

    @Getter
    @Setter
    private static class UserTaskSearchRequest {
        private UserTaskFilter filter;
    }

    @Getter
    @Setter
    private static class UserTaskFilter {
        private String processInstanceKey;
        private String state;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class UserTaskSearchResult {
        private List<UserTaskResult> items;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class UserTaskResult {
        private String userTaskKey;
    }

    @Getter
    @Setter
    private static class UserTaskCompletionRequest {
        private Map<String, Object> variables;
    }
}
