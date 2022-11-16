package com.example.apideliveryservice.interceptor;

import com.example.apideliveryservice.dto.ResponseError;
import com.example.apideliveryservice.threadLocalStorage.ThreadLocalStorage;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class ExceptionResponseInterceptor implements HandlerInterceptor {

    private ObjectMapper objectMapper = new ObjectMapper();
    private ThreadLocalStorage threadLocalStorage = new ThreadLocalStorage();

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
        Object handler, Exception ex) throws Exception {
        requestAttributeExceptionControl(request, response);
        threadLocalExceptionControl(request, response);
    }

    private void threadLocalExceptionControl(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        if (threadLocalStorage.getErrorType() != null) {
            String errorType = threadLocalStorage.getErrorType();
            String errorDetail = threadLocalStorage.getErrorDetail();
            String errorTitle = threadLocalStorage.getErrorTitle();
            threadLocalStorage.removeErrorType();
            threadLocalStorage.removeErrorDetail();
            threadLocalStorage.removeErrorTitle();
            log.warn("TLSExceptionResponseInterceptor, error type={}", errorType);
            ResponseError error = new ResponseError(errorType, errorTitle,
                response.getStatus(), errorDetail, request.getRequestURI());
            String errorResponseBody = objectMapper.writeValueAsString(error);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(errorResponseBody);
        }
    }

    private void requestAttributeExceptionControl(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        if (request.getAttribute("errorType") != null) {
            String errorType = request.getAttribute("errorType").toString();
            String errorDetail = request.getAttribute("errorDetail").toString();
            String errorTitle = request.getAttribute("errorTitle").toString();
            log.info("ExceptionResponseInterceptor, error type={}", errorType);
            ResponseError error = new ResponseError(errorType, errorTitle,
                response.getStatus(), errorDetail, request.getRequestURI());
            String errorResponseBody = objectMapper.writeValueAsString(error);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(errorResponseBody);
        }
    }
}
