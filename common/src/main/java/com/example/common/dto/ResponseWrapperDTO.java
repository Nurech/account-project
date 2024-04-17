package com.example.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseWrapperDTO<T> {
    private T data;
    private ErrorDTO error;

    public ResponseWrapperDTO(T data) {
        this.data = data;
    }

    public ResponseWrapperDTO(ErrorDTO error) {
        this.error = error;
    }

    public boolean hasError() {
        return error != null;
    }
}
