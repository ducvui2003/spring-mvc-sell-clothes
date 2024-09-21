package com.spring.websellspringmvc.utils.anotation.impl;

import com.spring.websellspringmvc.utils.anotation.PasswordValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordValid, String> {
	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		if (password == null || password.isEmpty()) return false;

		// Define your password validation logic here
		boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
		boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
		boolean hasDigit = password.chars().anyMatch(Character::isDigit);
		boolean hasSpecialChar = password.chars().anyMatch(ch -> "!@#$%^&*()_+[]{}|;:,.<>?".indexOf(ch) >= 0);
		boolean isValidLength = password.length() >= 8;

		return hasUppercase && hasLowercase && hasDigit && hasSpecialChar && isValidLength;
	}

	@Override
	public void initialize(PasswordValid constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}
}
