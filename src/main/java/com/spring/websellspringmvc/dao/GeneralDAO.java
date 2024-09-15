package com.spring.websellspringmvc.dao;

import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class GeneralDAO {
    Jdbi jdbi;

    public static void customExecute(Consumer<Handle> function) {

    }

    public static <T> List<T> executeQueryWithSingleTable(String sql, Class<T> type, Object... params) {
//        Handle handle = jdbi.open();
//        Query query = handle.createQuery(sql);
//        query.setFetchSize(Integer.MIN_VALUE);
//
//        if (params != null) {
//            for (int i = 0; i < params.length; i++) {
//                query.bind(i, params[i]);
//            }
//        }
//        return query.mapToBean(type).list();
        return null;
    }

    public List<Map<String, Object>> executeQueryWithJoinTables(String sql, Object... params) {
        return jdbi.withHandle(handle -> {
            Query query = handle.createQuery(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    query.bind(i, params[i]);
                }
            }
            return query.mapToMap().list();
        });
    }

    //Use for delete, insert and update statements
    public void executeAllTypeUpdate(String sql, Object... params) {
        Handle handle = jdbi.open();
        try {
            handle.useTransaction(handleInner -> {
                try {
                    handleInner.getConnection().setAutoCommit(false);
                    handleInner.execute(sql, params);
                    handleInner.getConnection().commit();
                } catch (Exception exception) {
                    handle.rollback();
                }

            });
        } catch (Exception exception) {
            handle.rollback();
        }
    }

    public int executeInsert(String sql, Object... params) {
        Handle handle = jdbi.open();
        Update insert = handle.createUpdate(sql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                insert.bind(i, params[i]);
            }
        }
        return insert.executeAndReturnGeneratedKeys("id") // "id" is the column name of the generated key
                .mapTo(Integer.class)
                .one();
    }

    public <T> T executeSelectOne(String sql, Class<T> type, Object... params) {
        List<T> list = executeQueryWithSingleTable(sql, type, params);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
