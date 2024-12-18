package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.dto.request.datatable.OrderDatatableRequest;
import com.spring.websellspringmvc.dto.response.datatable.OrderDatatable;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class DatatableImpl implements DatatableDAO {
    Jdbi jdbi;

    @Override
    public List<OrderDatatable> datatable(OrderDatatableRequest request) {
        // Build the SQL query dynamically
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT orders.id, fullName, dateOrder, paymentMethod, order_statuses.alias AS orderStatus ");
        sql.append("FROM orders JOIN order_statuses ON orders.orderStatusId = order_statuses.id  WHERE 1=1 AND orders.previousId = orders.id ");

        Map<String, Object> params = new HashMap<>();

        if (request.getContentSearch() != null && request.getSearchSelect() != null && !request.getContentSearch().isBlank()) {
            switch (request.getSearchSelect()) {
                case ORDER_ID:
                    sql.append("AND id LIKE :contentSearch ");
                    break;
                case CUSTOMER_NAME:
                    sql.append("AND fullName LIKE :contentSearch ");
                    break;
            }
            params.put("contentSearch", request.getContentSearch());
        }

        if (request.getPaymentMethod() != null) {
            sql.append("AND paymentMethod IN (<paymentMethod>) ");
            params.put("paymentMethod", request.getPaymentMethod());
        }

        if (request.getOrderStatus() != null) {
            sql.append("AND orderStatusId IN (<orderStatus>) ");
            params.put("orderStatus", request.getOrderStatus());
        }

        if (request.getTransactionStatus() != null) {
            sql.append("AND transactionStatusId IN (<transactionStatus>) ");
            params.put("transactionStatus", request.getTransactionStatus());
        }

        if (request.getStartDate() != null && request.getEndDate() != null) {
            sql.append("AND dateOrder BETWEEN :startDate AND :endDate ");
            params.put("startDate", request.getStartDate());
            params.put("endDate", request.getEndDate());
        }

        sql.append("ORDER BY dateOrder DESC ");
        sql.append("LIMIT :limit OFFSET :offset ");
        params.put("limit", request.getLength());
        params.put("offset", request.getStart());

        // Execute the query and return results
        return jdbi.withHandle(handle -> {
            Query query = handle.createQuery(sql);

            // Bind all single parameters
            params.forEach((key, value) -> {
                if (!(value instanceof List)) {
                    query.bind(key, value);
                }
            });

            // Bind lists (e.g., for IN clauses)
            params.forEach((key, value) -> {
                if (value instanceof List) {
                    query.bindList(key, (List<?>) value);
                }
            });
            return query.mapToBean(OrderDatatable.class).list();
        });
    }

    @Override
    public long count(OrderDatatableRequest request) {
        // Build the SQL query dynamically
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) ");
        sql.append("FROM orders WHERE 1=1 ");

        Map<String, Object> params = new HashMap<>();

        if (request.getContentSearch() != null && request.getSearchSelect() != null && !request.getContentSearch().isBlank()) {
            switch (request.getSearchSelect()) {
                case ORDER_ID:
                    sql.append("AND id LIKE :contentSearch ");
                    break;
                case CUSTOMER_NAME:
                    sql.append("AND fullName LIKE :contentSearch ");
                    break;
            }
            params.put("contentSearch", request.getContentSearch());
        }

        if (request.getPaymentMethod() != null) {
            sql.append("AND paymentMethod IN (<paymentMethod>) ");
            params.put("paymentMethod", request.getPaymentMethod());
        }

        if (request.getOrderStatus() != null) {
            sql.append("AND orderStatusId IN (<orderStatus>) ");
            params.put("orderStatus", request.getOrderStatus());
        }

        if (request.getTransactionStatus() != null) {
            sql.append("AND transactionStatusId IN (<transactionStatus>) ");
            params.put("transactionStatus", request.getTransactionStatus());
        }

        if (request.getStartDate() != null && request.getEndDate() != null) {
            sql.append("AND dateOrder BETWEEN :startDate AND :endDate ");
            params.put("startDate", request.getStartDate());
            params.put("endDate", request.getEndDate());
        }

        // Execute the query and return results
        return jdbi.withHandle(handle -> {
            Query query = handle.createQuery(sql);

            // Bind all single parameters
            params.forEach((key, value) -> {
                if (!(value instanceof List)) {
                    query.bind(key, value);
                }
            });

            // Bind lists (e.g., for IN clauses)
            params.forEach((key, value) -> {
                if (value instanceof List) {
                    query.bindList(key, (List<?>) value);
                }
            });
            return query.mapTo(Long.class).one();
        });
    }
}
