package com.example.account.service;

import com.example.common.dto.ErrorDTO;
import com.example.common.dto.ResponseWrapperDTO;
import com.example.common.exception.enums.ErrorCode;
import com.example.common.exception.exceptions.BusinessException;
import com.example.common.exception.exceptions.NoResponseFromRabbitException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageProcessingService {

    /**
     * Processes the response from RabbitMQ
     * Helps to handle the responses and throw exceptions if necessary
     * Useful to handle synchronous messaging between producers and consumers
     *
     * @param response response from RabbitMQ
     * @param T        type of the data in the response (can be any from domain)
     * @return data of domain class T type
     * @throws BusinessException             if there is a business error in the response
     * @throws NoResponseFromRabbitException if there is no ResponseWrapperDTO
     *                                       received from RabbitMQ in reasonable time
     */
    public <T> T processResponseWrapper(ResponseWrapperDTO<T> response) throws BusinessException, NoResponseFromRabbitException {
        if (response == null) {
            throw new NoResponseFromRabbitException("No valid response data received from RabbitMQ");
        } else if (response.hasError()) {
            ErrorDTO error = response.getError();
            ErrorCode errorCode = ErrorCode.fromCode(error.getErrorCode());
            throw new BusinessException(errorCode, error.getErrorMessage());
        } else if (response.getData() == null) {
            throw new NoResponseFromRabbitException("No valid response data received from RabbitMQ");
        }
        return response.getData();
    }

}



