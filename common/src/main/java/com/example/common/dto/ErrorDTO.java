package com.example.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ErrorDTO implements Serializable {
    private String errorCode;
    private String errorMessage;


    public boolean hasError() {
        return errorCode != null;
    }

}
