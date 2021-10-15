package dk.sunepoulsen.itdeveloper.validation;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Created by sunepoulsen on 14/06/2017.
 */
public class TimeLogValidation<T> {
    private Validator validator;

    public TimeLogValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public Set<ConstraintViolation<T>> validate( T value ) {
        return validator.validate( value );
    }

    public void validateAndThrow( T value ) throws TimeLogValidateException {
        Set<ConstraintViolation<T>> violations = validate( value );
        if( violations.isEmpty() ) {
            return;
        }

        throw new TimeLogValidateException( violations );
    }

    public static <T> void validateValue( T value ) throws TimeLogValidateException {
        new TimeLogValidation<T>().validateAndThrow( value );
    }
}
