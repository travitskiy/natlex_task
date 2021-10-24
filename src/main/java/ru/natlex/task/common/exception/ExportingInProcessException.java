package ru.natlex.task.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class ExportingInProcessException extends NatlexException {
    public ExportingInProcessException(Long id) {
        super(id + " exporting is in process");
    }
}
