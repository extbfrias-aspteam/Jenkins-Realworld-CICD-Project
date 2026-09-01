package net.cero.validators;

import lombok.extern.log4j.Log4j2;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

/**
 * Clase empleada para validar el formato correcto de las fechas que vengan en variables string
 */
@Log4j2
public class CheckDateValidator implements ConstraintValidator<CheckDateFormat, String> {

    private String pattern;

    @Override
    public void initialize(CheckDateFormat constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        if ( object == null ) {
            return true;
        }

        try {
            DateTimeFormatter sdf = DateTimeFormatter
                    .ofPattern(pattern)
                    .withResolverStyle (ResolverStyle.STRICT);
            LocalDate date = LocalDate.parse(object,sdf);
            log.info("Valor parseado: {}",date);
            return true;
        } catch (Exception e) {
            log.info("No paso la validación el patron {} de fecha: {}",this.pattern,object);
            return false;
        }
    }
}
