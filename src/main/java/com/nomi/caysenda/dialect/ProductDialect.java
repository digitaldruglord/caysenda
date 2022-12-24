package com.nomi.caysenda.dialect;

import com.nomi.caysenda.dialect.factory.ProductUtilsFactory;
import com.nomi.caysenda.dialect.factory.UrlBuilderFactory;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExecutionAttributeDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Map;

public class ProductDialect extends AbstractDialect implements IExpressionObjectDialect, IExecutionAttributeDialect {
    private final IExpressionObjectFactory URLBUILDER_EXPRESSION_OBJECTS_FACTORY = new ProductUtilsFactory();

    public ProductDialect(String name) {
        super(name);
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return URLBUILDER_EXPRESSION_OBJECTS_FACTORY;
    }

    @Override
    public Map<String, Object> getExecutionAttributes() {
        return null;
    }
}
