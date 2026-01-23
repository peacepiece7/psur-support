package com.service.demo.interceptor;

import com.service.demo.common.constant.SessionConst;
import com.service.demo.common.error.TokenErrorCode;
import com.service.demo.common.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        String path = request.getRequestURI();
        if (path.equals("/auth/login") || path.equals("/auth/register")
                || path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")
                || path.startsWith("/error") || path.startsWith("/actuator")) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new ApiException(TokenErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
        }

        Object userId = session.getAttribute(SessionConst.USER_ID);
        if (userId == null) {
            throw new ApiException(TokenErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
        }

        request.setAttribute(SessionConst.USER_ID, userId);
        return true;
    }
}
