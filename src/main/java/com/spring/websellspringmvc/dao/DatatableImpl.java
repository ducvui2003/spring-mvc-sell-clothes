package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.dto.request.OrderDatatableRequest;
import com.spring.websellspringmvc.dto.response.datatable.OrderDatatable;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.SqlStatements;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class DatatableImpl implements DatatableDAO {
    Jdbi jdbi;

    @Override
    public List<OrderDatatable> datatable(OrderDatatableRequest request) {
        // Build the SQL query dynamically
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id, fullName, dateOrder, paymentMethodId, orderStatusId ");
        sql.append("FROM orders WHERE 1=1 ");

        if (request.getContentSearch() != null && request.getSearchSelect() != null && !request.getContentSearch().isBlank())
            switch (request.getSearchSelect()) {
                case ORDER_ID:
                    sql.append("AND id LIKE :contentSearch ");
                    break;
                case CUSTOMER_NAME:
                    sql.append("AND fullName LIKE :contentSearch ");
                    break;
            }

        if (request.getPaymentMethod() != null) {
            sql.append("AND paymentMethodId IN (<paymentMethod>) ");
        }

        if (request.getOrderStatus() != null) {
            sql.append("AND orderStatusId IN (<orderStatus>) ");
        }

        if (request.getTransactionStatus() != null) {
            sql.append("AND transactionStatusId IN (<transactionStatus>) ");
        }

        if (request.getStartDate() != null && request.getEndDate() != null) {
            sql.append("AND dateOrder BETWEEN :startDate AND :endDate ");
        }

        sql.append("ORDER BY dateOrder DESC ");
        sql.append("LIMIT :limit OFFSET :offset ");

        // Execute the query and return results
        jdbi.getConfig(SqlStatements.class).setUnusedBindingAllowed(true);
        return jdbi.withHandle(handle ->
                handle.createQuery(sql.toString())
                        .bind("contentSearch", request.getContentSearch())
                        .bindList("paymentMethod", request.getPaymentMethod())
                        .bindList("orderStatus", request.getOrderStatus())
                        .bindList("transactionStatus", request.getTransactionStatus())
                        .bind("startDate", request.getStartDate())
                        .bind("endDate", request.getEndDate())
                        .bind("limit", request.getLength())
                        .bind("offset", request.getStart())
                        .mapToBean(OrderDatatable.class)
                        .list()
        );
    }

    @Override
    public long count(OrderDatatableRequest request) {
        // Build the SQL query dynamically
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) ");
        sql.append("FROM orders WHERE 1=1 ");

        if (request.getContentSearch() != null && request.getSearchSelect() != null && !request.getContentSearch().isBlank())
            switch (request.getSearchSelect()) {
                case ORDER_ID:
                    sql.append("AND id LIKE :contentSearch ");
                    break;
                case CUSTOMER_NAME:
                    sql.append("AND fullName LIKE :contentSearch ");
                    break;
            }

        if (request.getPaymentMethod() != null) {
            sql.append("AND paymentMethodId IN (<paymentMethod>) ");
        }

        if (request.getOrderStatus() != null) {
            sql.append("AND orderStatusId IN (<orderStatus>) ");
        }

        if (request.getTransactionStatus() != null) {
            sql.append("AND transactionStatusId IN (<transactionStatus>) ");
        }

        if (request.getStartDate() != null && request.getEndDate() != null) {
            sql.append("AND dateOrder BETWEEN :startDate AND :endDate ");
        }

        // Execute the query and return results
        return jdbi.withHandle(handle ->
                handle.createQuery(sql.toString())
                        .bind("contentSearch", request.getContentSearch())
                        .bindList("paymentMethod", request.getPaymentMethod())
                        .bindList("orderStatus", request.getOrderStatus())
                        .bindList("transactionStatus", request.getTransactionStatus())
                        .bind("startDate", request.getStartDate())
                        .bind("endDate", request.getEndDate())
                        .mapTo(Long.class).one()
        );
    }
}
