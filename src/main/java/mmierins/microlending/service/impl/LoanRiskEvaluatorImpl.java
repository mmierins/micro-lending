package mmierins.microlending.service.impl;

import mmierins.microlending.condition.Condition;
import mmierins.microlending.condition.ConditionClassification;
import mmierins.microlending.domain.nonpersistent.*;
import mmierins.microlending.service.api.*;
import mmierins.microlending.domain.LoanApplication;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoanRiskEvaluatorImpl implements LoanRiskEvaluator {

    List<Condition<LoanApplication>> conditions;
    ConditionEvaluator<LoanApplication> conditionEvaluator;

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    public LoanRiskEvaluatorImpl(
            @ConditionClassification(ConditionClassification.ConditionType.LOAN_ISSUING_RISK)
            List<Condition<LoanApplication>> conditions,
            ConditionEvaluator<LoanApplication> conditionEvaluator) {
        this.conditions = conditions;
        this.conditionEvaluator = conditionEvaluator;
    }

    public LoanRiskEvaluationResult evaluateRisks(LoanApplication application) {
        ConditionEvaluationResult result = conditionEvaluator.evaluate(application, conditions);
        if (result.getStatus() == ConditionEvaluationStatus.NONE_MATCHED) {
            return new LoanRiskEvaluationResult(LoanRiskEvaluationStatus.LOW_RISK, result.getMatchedConditions());
        } else if (result.getStatus() == ConditionEvaluationStatus.NONE_EVALUATED) {
            logger.warn("No loan risks were evaluated, probably some problem with risk bean autowiring");
            return new LoanRiskEvaluationResult(LoanRiskEvaluationStatus.LOW_RISK, result.getMatchedConditions());
        } else {
            return new LoanRiskEvaluationResult(LoanRiskEvaluationStatus.HIGH_RISK, result.getMatchedConditions());
        }
    }

}
