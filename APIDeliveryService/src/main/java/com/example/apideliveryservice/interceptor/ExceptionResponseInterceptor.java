package com.example.apideliveryservice.interceptor;

import com.example.apideliveryservice.dto.ResponseError;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class ExceptionResponseInterceptor implements HandlerInterceptor {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
        Object handler, Exception ex) throws Exception {
//        HandlerMethod handlerMethod = (HandlerMethod) handler;
//        String controllerName = handlerMethod.getBeanType().getSimpleName().replace("Controller", "");
        String errorType = request.getAttribute("errorType").toString();
        String errorDetail = request.getAttribute("errorDetail").toString();
        String errorTitle = request.getAttribute("errorTitle").toString();

        if (errorType != null) {
            log.info("ExceptionResponseInterceptor, error type={}", errorType);
            ResponseError error = new ResponseError(errorType, errorTitle,
                response.getStatus(), errorDetail, request.getRequestURI());
            String errorResponseBody = objectMapper.writeValueAsString(error);
//            response.setContentType("application/json");
//            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(errorResponseBody);
        }
    }
}
