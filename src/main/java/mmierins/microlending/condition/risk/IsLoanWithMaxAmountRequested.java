package mmierins.microlending.condition.risk;

import mmierins.microlending.condition.Condition;
import mmierins.microlending.domain.LoanApplication;
import mmierins.microlending.repository.ParameterRepository;
import mmierins.microlending.misc.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IsLoanWithMaxAmountRequested implements Condition<LoanApplication> {

    private ParameterRepository parameterRepository;

    @Autowired
    public IsLoanWithMaxAmountRequested(ParameterRepository parameterRepository) {
        this.parameterRepository = parameterRepository;
    }

    @Override
    public boolean verify(LoanApplication data) {
        Long maxAmountOfLoan =
                parameterRepository
                        .findByKey(AppConstants.ParameterKey.MAX_AMOUNT_OF_LOAN.name())
                        .getValueAsLong();

        return data.getAmount() >= maxAmountOfLoan;
    }

    @Override
    public String getMessage() {
        return "Loan with maximum amount requested";
    }

    @Override
    public int getCode() {
        return AppConstants.ResultCode.MAX_LOAN_AMOUNT_REQUESTED.getCode();
    }
}
