package com.example.account.mappers;

import com.example.account.model.Transaction;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface TransactionMapper {

    @Insert("INSERT INTO transactions (account_id, amount, currency, transaction_direction, description, transaction_date) " +
            "VALUES (#{accountId}, #{amount}, #{currency}, #{transactionDirection}, #{description}, CURRENT_TIMESTAMP)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertTransaction(Transaction transaction);

    @Select("SELECT SUM(amount) FROM transactions WHERE account_id = #{accountId} AND currency = #{currency} AND transaction_direction = 'IN'")
    BigDecimal getInboundSum(Long accountId, String currency);

    @Select("SELECT SUM(amount) FROM transactions WHERE account_id = #{accountId} AND currency = #{currency} AND transaction_direction = 'OUT'")
    BigDecimal getOutboundSum(Long accountId, String currency);

    @Select("SELECT * FROM transactions WHERE account_id = #{accountId}")
    List<Transaction> findTransactionsByAccountId(Long accountId);
}
