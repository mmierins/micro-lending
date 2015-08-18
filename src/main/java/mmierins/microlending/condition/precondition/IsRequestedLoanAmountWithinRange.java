package mmierins.microlending.condition.precondition;

import mmierins.microlending.service.api.NumericRangeChecker;
import mmierins.microlending.condition.Condition;
import mmierins.microlending.condition.ConditionClassification;
import mmierins.microlending.domain.LoanApplication;
import mmierins.microlending.misc.AppConstants;
import mmierins.microlending.repository.ParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ConditionClassification(ConditionClassification.ConditionType.LOAN_ISSUING_PRECONDITION)
public class IsRequestedLoanAmountWithinRange implements Condition<LoanApplication> {

    private ParameterRepository parameterRepository;
    private NumericRangeChecker numericRangeChecker;

    @Autowired
    public IsRequestedLoanAmountWithinRange(ParameterRepository parameterRepository, NumericRangeChecker numericRangeChecker) {
        this.parameterRepository = parameterRepository;
        this.numericRangeChecker = numericRangeChecker;
    }

    @Override
    public boolean verify(LoanApplication data) {
        long min = parameterRepository.findByKey(AppConstants.ParameterKey.MIN_AMOUNT_OF_LOAN.name()).getValueAsLong();
        long max = parameterRepository.findByKey(AppConstants.ParameterKey.MAX_AMOUNT_OF_LOAN.name()).getValueAsLong();
        return numericRangeChecker.isValueWithinRange(data.getAmount(), min, max);
    }

    @Override
    public String getMessage() {
        return "The amount requested is out of allowed range";
    }

    @Override
    public int getCode() {
        return AppConstants.ResultCode.PARAMETER_OUT_OF_RANGE.getCode();
    }

}
