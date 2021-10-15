package dk.sunepoulsen.itdeveloper.validation.validators;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalTime;

@Slf4j
public class ValidTimeIntervalValidator implements ConstraintValidator<ValidTimeInterval, Object> {
    ValidTimeInterval constraintAnnotation;

    @Override
    public void initialize(ValidTimeInterval constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (constraintAnnotation.properties().length != 2) {
            return false;
        }

        try {
            Object startTimeObject = findMethod(value, constraintAnnotation.properties()[0]).invoke(value);
            Object endTimeObject = findMethod(value, constraintAnnotation.properties()[1]).invoke(value);

            if (!(startTimeObject instanceof LocalTime startTime)) {
                log.info("{}.{} is not of type LocalDate", value.getClass().getName(), constraintAnnotation.properties()[0]);
                return false;
            }
            if (!(endTimeObject instanceof LocalTime endTime)) {
                log.info("{}.{} is not of type LocalDate", value.getClass().getName(), constraintAnnotation.properties()[1]);
                return false;
            }

            return startTime.equals(endTime) || startTime.isBefore(endTime);
        }
        catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
            log.info("Unable to call method: {}", ex.getMessage(), ex);
            return false;
        }
    }

    private Method findMethod(Object value, String propName) throws NoSuchMethodException {
        String methodName = "get";
        methodName = methodName + propName.substring(0, 1).toUpperCase();
        methodName = methodName + propName.substring(1);

        return value.getClass().getDeclaredMethod(methodName);
    }
}
