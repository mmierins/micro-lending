package mmierins.microlending.service.impl;

import mmierins.microlending.condition.Condition;
import mmierins.microlending.condition.ConditionClassification;
import mmierins.microlending.domain.nonpersistent.*;
import mmierins.microlending.service.api.*;
import mmierins.microlending.domain.Loan;
import mmierins.microlending.domain.LoanExtension;
import mmierins.microlending.misc.AppConstants;
import mmierins.microlending.misc.DateUtils;
import mmierins.microlending.repository.LoanExtensionRepository;
import mmierins.microlending.repository.LoanRepository;
import mmierins.microlending.repository.ParameterRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoanExtenderImpl implements LoanExtender {

    @Autowired
    private ConditionEvaluator<LoanExtensionApplication> conditionEvaluator;
    @Autowired
    @ConditionClassification(ConditionClassification.ConditionType.LOAN_EXTENSION_PRECONDITION)
    private List<Condition<LoanExtensionApplication>> preconditions;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    ParameterRepository parameterRepository;
    @Autowired
    LoanExtensionRepository loanExtensionRepository;
    @Autowired
    DateTimeProvider dateTimeProvider;

    private Logger logger = Logger.getLogger(getClass());

    private LoanExtension createAndPersistLoanExtension(LoanExtensionApplication application) {
        logger.info("Creating a new LoanExtension");

        LoanExtension extension = new LoanExtension();
        Loan loan = loanRepository.findActiveLoanByClient(application.getClient());
        extension.setLoan(loan);
        extension.setTerm(application.getTerm());
        extension.setInterest(parameterRepository.findByKey(AppConstants.ParameterKey.LOAN_EXTENSION_INTEREST_RATE.name()).getValueAsDouble());
        extension.setDateExtended(DateUtils.convertToDate(dateTimeProvider.getCurrentDateTime()));
        logger.info(String.format("Loan.ID=%d, term=%d, interestRate=%f", loan.getId(), extension.getTerm(), extension.getInterest()));
        loan.getExtensions().add(extension);
        loanExtensionRepository.save(extension);

        logger.info(String.format("LoanExtension persisted with ID=%d", extension.getId()));
        return extension;
    }

    @Override
    public ApplicationResult extendLoan(LoanExtensionApplication application) {
        logger.info("Evaluating loan extension preconditions");
        ConditionEvaluationResult<LoanExtensionApplication> preconditionsCheck =
                conditionEvaluator.evaluate(application, preconditions);

        if (preconditionsCheck.getStatus() != ConditionEvaluationStatus.ALL_MATCHED) {
            logger.info("Rejecting LoanExension since >0 preconditions were not satisfied");
            Condition<LoanExtensionApplication> firstViolated = preconditionsCheck.getUnmatchedConditions().iterator().next();
            return new ApplicationResult(
                    ApplicationStatus.REJECTED,
                    "Loan extension denied",
                    firstViolated.getMessage(),
                    firstViolated.getCode()
                    );
        } else {
            logger.info("Extending Loan since preconditions were satisfied");
            createAndPersistLoanExtension(application);
            return new ApplicationResult(
                    ApplicationStatus.ACCEPTED,
                    "Loan extension performed succesfully",
                    null,
                    AppConstants.ResultCode.LOAN_EXTENDED_SUCCESFULLY.getCode()
            );
        }

    }

}
