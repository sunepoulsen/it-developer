package dk.sunepoulsen.itdeveloper.validation;

import lombok.Data;
import lombok.Getter;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by sunepoulsen on 14/06/2017.
 */
public class TimeLogValidateException extends RuntimeException {
    @Data
    public class Item {
        private String message;
        private Path propertyPath;

        public <T> Item( ConstraintViolation<T> violation ) {
            this.message = violation.getMessage();
            this.propertyPath = violation.getPropertyPath();
        }
    }

    @Getter
    private List<Item> items;

    public <T> TimeLogValidateException( Set<ConstraintViolation<T>> violations ) {
        super();
        this.items = violations.stream().map( violation -> new Item( violation ) ).collect( Collectors.toList() );
    }
}
