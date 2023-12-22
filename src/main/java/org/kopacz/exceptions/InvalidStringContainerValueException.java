package org.kopacz.exceptions;

import javax.management.RuntimeMBeanException;

public class InvalidStringContainerValueException extends RuntimeException {

    public InvalidStringContainerValueException(String message) {
        super(message);
    }
}
