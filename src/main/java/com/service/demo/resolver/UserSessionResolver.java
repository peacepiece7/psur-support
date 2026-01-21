package com.service.demo.resolver;

import com.service.demo.common.annotation.UserSession;
import com.service.demo.common.constant.SessionConst;
import com.service.demo.common.error.TokenErrorCode;
import com.service.demo.common.exception.ApiException;
import com.service.demo.domain.user.model.UserSessionInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class UserSessionResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAnnotation = parameter.hasParameterAnnotation(UserSession.class);
        Class<?> type = parameter.getParameterType();
        return hasAnnotation && (Long.class.equals(type) || UserSessionInfo.class.equals(type));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new ApiException(TokenErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
        }

        Object userId = request.getAttribute(SessionConst.USER_ID);
        if (userId == null) {
            throw new ApiException(TokenErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
        }

        if (Long.class.equals(parameter.getParameterType())) {
            if (userId instanceof Number) {
                return ((Number) userId).longValue();
            }
            return Long.parseLong(userId.toString());
        }

        return new UserSessionInfo(Long.parseLong(userId.toString()));
    }
}
