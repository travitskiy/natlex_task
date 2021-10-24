package ru.natlex.task.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class ExportFileNotFoundException extends NatlexException {
    public ExportFileNotFoundException(Long id) {
        super("Could not find job " + id);
    }
}
