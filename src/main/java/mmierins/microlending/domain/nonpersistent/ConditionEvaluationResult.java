package mmierins.microlending.domain.nonpersistent;

import mmierins.microlending.condition.Condition;

import java.util.HashSet;
import java.util.Set;

public class ConditionEvaluationResult<T> {

    private Set<Condition<T>> evaluatedConditions = new HashSet<>();
    private Set<Condition<T>> matchedConditions = new HashSet<>();
    private Set<Condition<T>> unmatchedConditions = new HashSet<>();
    private ConditionEvaluationStatus status = ConditionEvaluationStatus.NONE_EVALUATED;

    public Set<Condition<T>> getEvaluatedConditions() {
        return evaluatedConditions;
    }

    public Set<Condition<T>> getMatchedConditions() {
        return matchedConditions;
    }

    public ConditionEvaluationStatus getStatus() {
        return status;
    }

    public void setStatus(ConditionEvaluationStatus status) {
        this.status = status;
    }

    public Set<Condition<T>> getUnmatchedConditions() {
        return unmatchedConditions;
    }

}
