package com.example.apideliveryservice.threadlocalstorage;

public class ErrorInformationTls {

    private final static ThreadLocal<String> errorType = new ThreadLocal<>();
    private final static ThreadLocal<String> errorTitle = new ThreadLocal<>();
    private final static ThreadLocal<String> errorDetail = new ThreadLocal<>();

    public void setErrorType(String str) {
        errorType.set(str);
    }

    public void setErrorTitle(String str) {
        errorTitle.set(str);
    }

    public void setErrorDetail(String str) {
        errorDetail.set(str);
    }

    public String getErrorType() {
        return errorType.get();
    }

    public String getErrorTitle() {
        return errorTitle.get();
    }

    public String getErrorDetail() {
        return errorDetail.get();
    }

    public void removeErrorType() {
        errorType.remove();
    }

    public void removeErrorTitle() {
        errorTitle.remove();
    }

    public void removeErrorDetail() {
        errorDetail.remove();
    }

}
