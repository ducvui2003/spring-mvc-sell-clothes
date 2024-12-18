package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.TransactionStatus;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionStatusDAO {
    @SqlQuery("SELECT * FROM transaction_statuses")
    @RegisterBeanMapper(TransactionStatus.class)
    public List<TransactionStatus> getListAllTransactionStatus();

    @SqlQuery("SELECT * FROM transaction_statuses WHERE id = :id")
    public TransactionStatus getTransactionStatusById(@Bind("id") int transactionStatusId);

    @SqlQuery("SELECT transaction_statuses.alias FROM transaction_statuses JOIN orders ON transaction_statuses.id = orders.transactionStatusId WHERE orders.id = :orderId")
    public String getOrderStatusByOrderId(@Bind("orderId") String orderId);

    default com.spring.websellspringmvc.utils.constraint.TransactionStatus getTransactionStatus(String orderId) {
        String status = getOrderStatusByOrderId(orderId);
        return com.spring.websellspringmvc.utils.constraint.TransactionStatus.valueOf(status.toUpperCase());
    }
}

