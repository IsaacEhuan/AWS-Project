package com.siceiapp.crud.exceptions;

import lombok.*;
@Getter
@Setter
@Builder
@AllArgsConstructor
public class BusinessException extends RuntimeException{
    private final String message;
    private final String details;
}
