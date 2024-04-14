package com.example.transaction.service;

import com.example.common.dto.transaction.TransactionRequest;
import com.example.common.dto.transaction.TransactionResponse;
import com.example.common.dto.transaction.TransactionsByAccountRequest;
import com.example.common.dto.transaction.TransactionsByAccountResponse;
import com.example.common.exception.exceptions.BusinessException;
import com.example.transaction.mappers.TransactionMapper;
import com.example.common.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.example.common.exception.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionMapper transactionMapper;

    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request) {
        validateRequest(request);

        Transaction transaction = new Transaction();
        transaction.setAccountId(request.getAccountId());
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setTransactionDirection(request.getTransactionDirection());
        transaction.setDescription(request.getDescription());

        transactionMapper.insertTransaction(transaction);
        updateAccountBalance(transaction);

        return buildTransactionResponse(transaction);
    }

    public TransactionsByAccountResponse getTransactions(TransactionsByAccountRequest request) {
        Long accountId = request.getAccountId();
        List<Transaction> transactions = transactionMapper.findTransactionsByAccountId(accountId);
        return new TransactionsByAccountResponse(accountId, transactions);
    }

    private void validateRequest(TransactionRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(NEGATIVE_AMOUNT);
        }
        if (!request.getTransactionDirection().equals("IN") && !request.getTransactionDirection().equals("OUT")) {
            throw new BusinessException(INVALID_DIRECTION);
        }
    }

    private void updateAccountBalance(Transaction transaction) {
        BigDecimal inboundSum = transactionMapper.getInboundSum(transaction.getAccountId(), transaction.getCurrency());
        BigDecimal outboundSum = transactionMapper.getOutboundSum(transaction.getAccountId(), transaction.getCurrency());
        BigDecimal currentBalance = (inboundSum != null ? inboundSum : BigDecimal.ZERO)
                .subtract(outboundSum != null ? outboundSum : BigDecimal.ZERO);

        if ("OUT".equals(transaction.getTransactionDirection()) && currentBalance.compareTo(transaction.getAmount()) < 0) {
            throw new BusinessException(INSUFFICIENT_FUNDS);
        }
    }

    private TransactionResponse buildTransactionResponse(Transaction transaction) {
        BigDecimal inboundSum = transactionMapper.getInboundSum(transaction.getAccountId(), transaction.getCurrency());
        BigDecimal outboundSum = transactionMapper.getOutboundSum(transaction.getAccountId(), transaction.getCurrency());
        BigDecimal balanceAfterTransaction = (inboundSum != null ? inboundSum : BigDecimal.ZERO)
                .subtract(outboundSum != null ? outboundSum : BigDecimal.ZERO);

        return new TransactionResponse(
                transaction.getAccountId(),
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getTransactionDirection(),
                transaction.getDescription(),
                balanceAfterTransaction
        );
    }

}
