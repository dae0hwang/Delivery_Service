package com.example.apideliveryservice.annotation;

import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

public class PasswordConstraintsValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        PasswordValidator passwordValidator = new PasswordValidator(
            Arrays.asList(
                //Length rule. Min 8 max 128 characters
                new LengthRule(8, 128),
                //At least one upper case letter
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                //At least one lower case letter
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                //At least one number
                new CharacterRule(EnglishCharacterData.Digit, 1),
                //At least one special characters
                new CharacterRule(EnglishCharacterData.Special, 1),
                new WhitespaceRule()
            )
        );
        RuleResult result = passwordValidator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        //Sending one message each time failed validation.
        constraintValidatorContext.buildConstraintViolationWithTemplate(
                "password is At least 8 characters, at least 1 uppercase, lowercase,"
                    + " number, and special character each")
            .addConstraintViolation()
            .disableDefaultConstraintViolation();
        return false;
    }
}
