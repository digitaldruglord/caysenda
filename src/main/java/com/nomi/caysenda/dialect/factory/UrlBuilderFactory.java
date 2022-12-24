package com.nomi.caysenda.dialect.factory;

import com.nomi.caysenda.dialect.utils.UrlBuilderUtilsDialect;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class UrlBuilderFactory implements IExpressionObjectFactory {
    private static final String TEMPORAL_EVALUATION_VARIABLE_NAME = "urlbuilder";
    private static final Map<String, Object> EXECUTION_ATTRIBUTES = null;
    @Override
    public Set<String> getAllExpressionObjectNames() {
        return Collections.singleton(TEMPORAL_EVALUATION_VARIABLE_NAME);
    }

    @Override
    public UrlBuilderUtilsDialect buildObject(IExpressionContext iExpressionContext, String s) {
        return new UrlBuilderUtilsDialect();
    }

    @Override
    public boolean isCacheable(String s) {
        return true;
    }
}
