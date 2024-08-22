package pizzahub.api.presentation.validators.NullableNotEmpty;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;
import java.util.Map;

public class NullableNotEmptyValidator implements ConstraintValidator<NullableNotEmpty, Object> {
    @Override
    public void initialize(NullableNotEmpty constraint) {}

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return true;

        if (value instanceof String) return !((String) value).isEmpty();
        if (value instanceof Collection) return !((Collection<?>) value).isEmpty();
        if (value instanceof Map) return !((Map<?, ?>) value).isEmpty();

        return true;
    }
}
