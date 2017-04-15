package fr.trendev.postalhelper.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NotNull
@Size(min = 1)
@Target(value = {ElementType.METHOD, ElementType.FIELD,
    ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
public @interface NotEmpty {

    @Target(value = {ElementType.METHOD, ElementType.FIELD,
        ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
        ElementType.PARAMETER})
    @Retention(value = RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {

        public NotEmpty[] value();
    }

    public String message() default "Value cannot be empty";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

}
