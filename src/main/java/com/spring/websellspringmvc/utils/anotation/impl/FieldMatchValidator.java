package com.spring.websellspringmvc.utils.anotation.impl;

import com.spring.websellspringmvc.utils.anotation.FieldMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.BeanUtils;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.first();
        this.secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        try {
            final String firstValue = BeanUtils.getProperty(value, firstFieldName);
            final String secondValue = BeanUtils.getProperty(value, secondFieldName);

            return firstValue != null && firstValue.equals(secondValue);
        } catch (Exception e) {
            // If an exception occurs, consider the validation failed
            return false;
        }
    }
}
