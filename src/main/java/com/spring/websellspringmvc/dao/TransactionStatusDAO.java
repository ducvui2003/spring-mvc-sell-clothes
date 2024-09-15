package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.TransactionStatus;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionStatusDAO {
    @SqlQuery("SELECT * FROM transaction_statuses")
    public List<TransactionStatus> getListAllTransactionStatus();

    @SqlQuery("SELECT * FROM transaction_statuses WHERE id = :id")
    public TransactionStatus getTransactionStatusById(@Bind("id") int transactionStatusId);
}
