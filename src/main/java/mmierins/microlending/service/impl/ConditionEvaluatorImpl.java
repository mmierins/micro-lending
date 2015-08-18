package mmierins.microlending.service.impl;

import mmierins.microlending.condition.Condition;
import mmierins.microlending.domain.nonpersistent.ConditionEvaluationStatus;
import mmierins.microlending.service.api.ConditionEvaluator;
import mmierins.microlending.domain.nonpersistent.ConditionEvaluationResult;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

@Component
public class ConditionEvaluatorImpl<T> implements ConditionEvaluator<T> {

    private Logger logger = Logger.getLogger(getClass());

    @Override
    public ConditionEvaluationResult evaluate(T data, Collection<Condition<T>> conditions) {
        ConditionEvaluationResult<T> result = new ConditionEvaluationResult<>();

        logger.info(String.format("ConditionEvaluator initialized with %d conditions", conditions.size()));
        logger.debug(Arrays.toString(conditions.toArray()));

        for (Condition<T> condition : conditions) {
            if (condition.verify(data)) {
                result.getMatchedConditions().add(condition);
            } else {
                result.getUnmatchedConditions().add(condition);
            }
            result.getEvaluatedConditions().add(condition);
        }

        logger.info(String.format("ConditionEvaluator found %d matched conditions", result.getMatchedConditions().size()));
        logger.debug(Arrays.toString(result.getMatchedConditions().toArray()));

        if (result.getEvaluatedConditions().size() == 0) {
            result.setStatus(ConditionEvaluationStatus.NONE_EVALUATED);
        } else if (result.getMatchedConditions().size() == result.getEvaluatedConditions().size()) {
            result.setStatus(ConditionEvaluationStatus.ALL_MATCHED);
        } else if (result.getMatchedConditions().size() > 0) {
            result.setStatus(ConditionEvaluationStatus.SOME_MATCHED);
        } else {
            result.setStatus(ConditionEvaluationStatus.NONE_MATCHED);
        }

        logger.info(String.format("Returning %s as a condition evaluation status", result.getStatus()));

        return result;
    }

}
