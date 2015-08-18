package mmierins.microlending.service.api;

import mmierins.microlending.domain.LoanApplication;
import mmierins.microlending.domain.nonpersistent.LoanRiskEvaluationResult;

public interface LoanRiskEvaluator {

    LoanRiskEvaluationResult evaluateRisks(LoanApplication application);

}
