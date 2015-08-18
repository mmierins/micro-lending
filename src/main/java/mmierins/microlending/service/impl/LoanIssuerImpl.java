package mmierins.microlending.service.impl;

import mmierins.microlending.domain.nonpersistent.*;
import mmierins.microlending.condition.Condition;
import mmierins.microlending.condition.ConditionClassification;
import mmierins.microlending.domain.Loan;
import mmierins.microlending.domain.LoanApplication;
import mmierins.microlending.misc.AppConstants;
import mmierins.microlending.misc.DateUtils;
import mmierins.microlending.repository.LoanApplicationRepository;
import mmierins.microlending.repository.LoanRepository;
import mmierins.microlending.repository.ParameterRepository;
import mmierins.microlending.service.api.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoanIssuerImpl implements LoanIssuer {

    @Autowired
    @ConditionClassification(ConditionClassification.ConditionType.LOAN_ISSUING_PRECONDITION)
    private List<Condition<LoanApplication>> preconditions;
    @Autowired
    private LoanRiskEvaluator loanRiskEvaluator;
    @Autowired
    private ConditionEvaluator<LoanApplication> conditionEvaluator;
    @Autowired
    private ParameterRepository parameterRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private LoanApplicationRepository loanApplicationRepository;
    @Autowired
    private DateTimeProvider dateTimeProvider;

    private Logger logger = Logger.getLogger(getClass());

    private Loan createAndPersistLoan(LoanApplication application) {
        logger.info(
                String.format(
                        "Creating a new Loan, LoanApplication.ID=%d",
                        application.getId()
                )
        );
        Loan loan = new Loan();
        loan.setTerm(application.getTerm());
        loan.setAmount(application.getAmount());
        loan.setApplication(application);
        loan.setStatus(Loan.LoanStatus.ACTIVE);
        loan.setInterestRate(parameterRepository.findByKey(AppConstants.ParameterKey.BASE_LOAN_INTEREST_RATE.name()).getValueAsDouble());
        loan.setApplication(application);
        loan.setDateIssued(DateUtils.convertToDate(dateTimeProvider.getCurrentDateTime()));
        application.setLoan(loan);
        loanRepository.save(loan);
        loanApplicationRepository.save(application);
        logger.info(String.format("Loan persisted with ID=%d", loan.getId()));
        return loan;
    }

    private void updateLoanApplicationWithStatus(LoanApplication application, LoanApplication.LoanApplicationStatus status) {
        application.setLoanApplicationStatus(status);
    }

    @Override
    public ApplicationResult issueLoan(LoanApplication application) {
        logger.info("Evaluating loan issuing preconditions");
        ConditionEvaluationResult<LoanApplication> preconditionsCheck =
                conditionEvaluator.evaluate(application, preconditions);

        if (preconditionsCheck.getStatus() != ConditionEvaluationStatus.ALL_MATCHED) {
            logger.info("Rejecting LoanApplication since preconditions were not satisfied");
            updateLoanApplicationWithStatus(application, LoanApplication.LoanApplicationStatus.REJECTED);
            Condition<LoanApplication> firstViolated = preconditionsCheck.getUnmatchedConditions().iterator().next();
            return new ApplicationResult(
                    ApplicationStatus.REJECTED,
                    "Loan not issued; application does not meet the required criteria",
                    firstViolated.getMessage(),
                    firstViolated.getCode()
            );
        } else {
            logger.info("Evaluating loan issuing risks");
            LoanRiskEvaluationResult riskEvalResult = loanRiskEvaluator.evaluateRisks(application);

            if (riskEvalResult.getStatus() == LoanRiskEvaluationStatus.LOW_RISK) {
                logger.info("Issuing Loan since risks are low");
                createAndPersistLoan(application);
                updateLoanApplicationWithStatus(application,LoanApplication.LoanApplicationStatus.ACCEPTED);
                return new ApplicationResult(
                        ApplicationStatus.ACCEPTED,
                        "Loan issued succesfully",
                        "",
                        AppConstants.ResultCode.LOAN_ISSUED_SUCCCESFULLY.getCode()
                );
            } else {
                logger.info("Rejecting Loan since risks are high");
                updateLoanApplicationWithStatus(application,LoanApplication.LoanApplicationStatus.REJECTED);
                Condition<LoanApplication> firstViolated = riskEvalResult.getMatchedConditions().iterator().next();
                return new ApplicationResult(
                        ApplicationStatus.REJECTED,
                        "Loan not issued; risk is too high",
                        firstViolated.getMessage(),
                        firstViolated.getCode()
                );
            }
        }
    }

}
