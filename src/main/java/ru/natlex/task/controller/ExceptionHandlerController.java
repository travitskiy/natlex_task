package ru.natlex.task.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.natlex.task.exception.*;
import ru.natlex.task.model.ApiError;

@ControllerAdvice
class ExceptionHandlerController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({AsyncJobNotFoundException.class,
            ExportFileNotFoundException.class, SectionNotFoundException.class})
    @ResponseBody
    public ApiError handleNotFound(NatlexException e) {
        return new ApiError(e.getMessage());
    }

    @ResponseStatus(HttpStatus.PROCESSING)
    @ExceptionHandler({ExportingInProcessException.class})
    @ResponseBody
    public ApiError handleProcessing(NatlexException e) {
        return new ApiError(e.getMessage());
    }
}
