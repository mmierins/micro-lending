package mmierins.microlending.condition;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

@Target({ElementType.FIELD,
        ElementType.METHOD,
        ElementType.TYPE,
        ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ConditionClassifications.class)
@Qualifier
public @interface ConditionClassification {

    ConditionType value();

    enum ConditionType {
        LOAN_ISSUING_PRECONDITION,
        LOAN_ISSUING_RISK,
        LOAN_EXTENSION_PRECONDITION
    }

}
