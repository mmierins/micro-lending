package mmierins.microlending.condition.precondition;

import mmierins.microlending.condition.Condition;
import mmierins.microlending.condition.ConditionClassification;
import mmierins.microlending.domain.nonpersistent.LoanExtensionApplication;
import mmierins.microlending.misc.AppConstants;
import mmierins.microlending.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ConditionClassification(ConditionClassification.ConditionType.LOAN_EXTENSION_PRECONDITION)
public class IsActiveLoanIssuedToClient implements Condition<LoanExtensionApplication> {

    private LoanRepository loanRepository;

    @Autowired
    public IsActiveLoanIssuedToClient(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public boolean verify(LoanExtensionApplication data) {
        long numActiveLoans = loanRepository.getCountOfActiveLoansByClient(data.getClient());
        return numActiveLoans > 0;
    }

    @Override
    public String getMessage() {
        return "No active loan issued for client";
    }

    @Override
    public int getCode() {
        return AppConstants.ResultCode.ACTIVE_LOAN_FOR_CLIENT_EXIST.getCode();
    }

}
