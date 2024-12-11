package com.example.payment.validation;

import com.example.payment.data.dto.transaction.TransactionRequest;
import com.example.payment.data.model.transaction.TransactionType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.regex.Pattern;

public class TransactionRequestValidator implements ConstraintValidator<ValidTransactionRequest, TransactionRequest> {

    private static final String EMAIL_REGEX = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final String PHONE_REGEX = "\\+?[0-9]{1,4}?[-.\\s]?\\(?[0-9]{1,3}?\\)"
            + "?[-.\\s]?[0-9]{1,4}[-.\\s]?[0-9]{1,4}[-.\\s]?[0-9]{1,9}";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);


    @Override
    public boolean isValid(final TransactionRequest request,
                           final ConstraintValidatorContext constraintValidatorContext) {
        try {
            if (request.type() == null) {
                return false;
            }

            UUID.fromString(request.uuid());

            if (!StringUtils.hasText(request.customerEmail())) {
                return false;
            }
            if (!EMAIL_PATTERN.matcher(request.customerEmail()).find()) {
                return false;
            }

            if (!StringUtils.hasText(request.customerPhone())) {
                return false;
            }
            if (!PHONE_PATTERN.matcher(request.customerPhone()).find()) {
                return false;
            }

            if (!StringUtils.hasText(request.referenceId())) {
                return false;
            }

            if (request.type() != TransactionType.REVERSAL) {
                if (request.amount() == null) {
                    return false;
                }
                if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
                    return false;
                }
            }
        } catch (Throwable e) {
            return false;
        }
        return true;
    }
}
