package ru.natlex.task.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class SectionNotFoundException extends NatlexException {
    public SectionNotFoundException(Long id) {
        super("Could not find section " + id);
    }
}