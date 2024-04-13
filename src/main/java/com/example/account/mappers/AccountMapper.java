package com.example.account.mappers;

import com.example.account.model.Account;
import com.example.account.model.Balance;
import com.example.account.model.Transaction;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AccountMapper {

    @Insert("INSERT INTO accounts(customer_id, country) VALUES(#{customerId}, #{country})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertAccount(Account account);

    @Select("SELECT * FROM accounts WHERE id = #{id}")
    Account findAccountById(Long id);

    @Insert("INSERT INTO balances(account_id, currency, available_amount) VALUES(#{accountId}, #{currency}, #{availableAmount})")
    void insertBalance(Balance balance);

    @Select("SELECT * FROM balances WHERE account_id = #{accountId}")
    List<Balance> findBalancesByAccountId(Long accountId);

    @Insert("INSERT INTO transactions(account_id, amount, currency, transaction_direction, description) VALUES(#{accountId}, #{amount}, #{currency}, #{direction}, #{description})")
    void insertTransaction(Transaction transaction);

    @Update("UPDATE balances SET available_amount = available_amount + #{amount} WHERE account_id = #{accountId} AND currency = #{currency}")
    void updateBalance(Transaction transaction);

    @Select("SELECT * FROM transactions WHERE account_id = #{accountId}")
    List<Transaction> findTransactionsByAccountId(Long accountId);

    // This could be moved to a separate mapper
    @Select("SELECT code FROM currencies WHERE is_allowed = TRUE")
    List<String> findAllowedCurrencies();
}
