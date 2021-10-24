package ru.natlex.task.common;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.natlex.task.common.exception.*;
import ru.natlex.task.common.dto.ApiError;

import java.util.HashMap;
import java.util.Map;

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
