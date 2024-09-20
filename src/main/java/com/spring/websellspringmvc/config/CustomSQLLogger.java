package com.spring.websellspringmvc.config;

import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.statement.SqlLogger;
import org.jdbi.v3.core.statement.StatementContext;

@Slf4j
public class CustomSQLLogger implements SqlLogger {

    @Override
    public void logBeforeExecution(StatementContext context) {
        log.info("Executing SQL: {}", context.getRenderedSql());
    }
}
