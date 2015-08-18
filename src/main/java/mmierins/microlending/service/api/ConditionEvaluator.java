package mmierins.microlending.service.api;

import mmierins.microlending.condition.Condition;
import mmierins.microlending.domain.nonpersistent.ConditionEvaluationResult;

import java.util.Collection;

public interface ConditionEvaluator<T> {

    ConditionEvaluationResult<T> evaluate(T data, Collection<Condition<T>> conditions);

}
