package com.example.transaction.mappers;

import com.example.common.model.Transaction;
import org.apache.ibatis.annotations.*;

import java.time.OffsetDateTime;
import java.util.List;

@Mapper
public interface TransactionMapper {

    @Insert("INSERT INTO transactions (account_id, amount, currency_id, transaction_direction, description, transaction_date) " +
            "VALUES (#{accountId}, #{amount}, (SELECT id FROM currencies WHERE code = #{currency}), #{transactionDirection}, #{description}, CURRENT_TIMESTAMP)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertTransaction(Transaction transaction);

    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "amount", column = "amount"),
            @Result(property = "currency", column = "currency"),
            @Result(property = "transactionDirection", column = "transaction_direction"),
            @Result(property = "description", column = "description"),
            @Result(property = "transactionDate", column = "transaction_date", javaType = OffsetDateTime.class)
    })
    @Select("SELECT t.id, t.account_id, t.amount, c.code as currency, t.transaction_direction, t.description, t.transaction_date " +
            "FROM transactions t " +
            "JOIN currencies c ON t.currency_id = c.id " +
            "WHERE t.account_id = #{accountId}")
    List<Transaction> findTransactionsByAccountId(Long accountId);





}
