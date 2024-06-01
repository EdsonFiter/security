package com.springboot.security.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ValidationExceptionDetails extends ExceptionDetails{
    private String field;
    private String fieldMessages;
}
