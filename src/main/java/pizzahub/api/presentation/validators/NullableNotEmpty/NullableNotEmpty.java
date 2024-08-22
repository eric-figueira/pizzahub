package pizzahub.api.presentation.validators.NullableNotEmpty;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NullableNotEmptyValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface NullableNotEmpty {
    String message() default "Field must be either null or non-empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
