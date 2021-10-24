package ru.natlex.task.common.dto;

public class ApiError {
    private String message;

    public ApiError(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
