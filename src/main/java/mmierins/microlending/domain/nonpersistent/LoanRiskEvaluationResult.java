package mmierins.microlending.domain.nonpersistent;

import mmierins.microlending.condition.Condition;
import mmierins.microlending.domain.LoanApplication;

import java.util.Set;

public class LoanRiskEvaluationResult {

    private LoanRiskEvaluationStatus status;
    private Set<Condition<LoanApplication>> matchedConditions;

    public LoanRiskEvaluationResult(LoanRiskEvaluationStatus status, Set<Condition<LoanApplication>> matchedConditions) {
        this.status = status;
        this.matchedConditions = matchedConditions;
    }

    public LoanRiskEvaluationStatus getStatus() {
        return status;
    }

    public Set<Condition<LoanApplication>> getMatchedConditions() {
        return matchedConditions;
    }

}
