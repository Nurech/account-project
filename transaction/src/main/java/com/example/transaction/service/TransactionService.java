package com.example.transaction.service;

import com.example.common.domain.transaction.TransactionRequest;
import com.example.common.domain.transaction.TransactionResponse;
import com.example.common.domain.transaction.TransactionsByAccountRequest;
import com.example.common.domain.transaction.TransactionsByAccountResponse;
import com.example.common.exception.exceptions.BusinessException;
import com.example.common.model.Balance;
import com.example.transaction.mappers.TransactionMapper;
import com.example.common.model.Transaction;
import com.example.transaction.serviceclient.AccountService;
import com.example.transaction.serviceclient.BalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.example.common.exception.enums.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionMapper transactionMapper;
    private final BalanceService balanceService;
    private final AccountService accountService;

    /**
     * Client created transaction, which can be either IN or OUT.
     * If the transaction is OUT and the account has insufficient funds,
     * the transaction will be rejected and account balance will remain unchanged.
     *
     * @param request TransactionRequest
     * @return TransactionResponse
     */
    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request) {
        validateRequest(request);

        Transaction transaction = new Transaction();
        transaction.setAccountId(request.getAccountId());
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setTransactionDirection(request.getTransactionDirection());
        transaction.setDescription(request.getDescription());

        Balance balance = balanceService.getAccountBalanceByCurrency(transaction.getAccountId(), transaction.getCurrency());

        try {
            BigDecimal currentBalance = balance.getAvailableAmount();
            BigDecimal sum = transaction.getAmount();

            if (transaction.getTransactionDirection().equals("IN")) {
                balance.setAvailableAmount(currentBalance.add(sum));
                balanceService.updateBalance(balance);
                transactionMapper.insertTransaction(transaction);
            } else {
                if (currentBalance.subtract(sum).compareTo(BigDecimal.ZERO) < 0) {
                    log.warn("Insufficient funds for accountId: {}, required: {}, available: {}", transaction.getAccountId(), sum, currentBalance);
                    throw new BusinessException(INSUFFICIENT_FUNDS);
                }
                balance.setAvailableAmount(currentBalance.subtract(sum));
                balanceService.updateBalance(balance);
                transactionMapper.insertTransaction(transaction);
            }
            balanceService.updateBalance(balance);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unhandled error while processing transaction", e);
            throw new BusinessException(TRANSACTION_PROCESSING_ERROR);
        }

        return buildTransactionResponse(transaction, balance);
    }

    private TransactionResponse buildTransactionResponse(Transaction transaction, Balance balance) {
        return new TransactionResponse(
                transaction.getAccountId(),
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getTransactionDirection(),
                transaction.getDescription(),
                balance.getAvailableAmount()
        );
    }


    public TransactionsByAccountResponse getTransactions(TransactionsByAccountRequest request) {
        Long accountId = request.getAccountId();

        // Check if the account exists
        accountService.checkAccountExists(accountId);

        List<Transaction> transactions = transactionMapper.findTransactionsByAccountId(accountId);
        if (transactions.isEmpty()) {
            throw new BusinessException(NO_TRANSACTIONS);
        }

        return new TransactionsByAccountResponse(accountId, transactions);
    }

    private void validateRequest(TransactionRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(NEGATIVE_AMOUNT);
        }
        if (!request.getTransactionDirection().equals("IN") && !request.getTransactionDirection().equals("OUT")) {
            throw new BusinessException(INVALID_DIRECTION);
        }
        if (request.getDescription() == null || request.getDescription().isEmpty()) {
            throw new BusinessException(DESCRIPTION_MISSING);
        }
    }

}
