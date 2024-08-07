package com.felipe.souls.dscatalog.controllers.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {

    private List<FieldMessage> errors = new ArrayList<>();

    public List<FieldMessage> getErrors() {
        return errors;
    }

    public void addError(String fieldName, String message) {
        errors.removeIf(fieldMessage -> fieldMessage.getMessage().equals(fieldName));

        errors.add(new FieldMessage(fieldName, message));
    }
}
