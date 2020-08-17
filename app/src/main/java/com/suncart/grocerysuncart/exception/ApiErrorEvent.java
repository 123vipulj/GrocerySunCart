package com.suncart.grocerysuncart.exception;


public class ApiErrorEvent {

    private Throwable throwable;
    private String message;

    public ApiErrorEvent(String message) {
        this.message = message;
    }

    public ApiErrorEvent(Throwable throwable, String message) {
        this.throwable = throwable;
        this.message = message;
    }

    public ApiErrorEvent(Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
