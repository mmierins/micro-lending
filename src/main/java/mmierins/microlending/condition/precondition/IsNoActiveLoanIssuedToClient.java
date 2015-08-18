package mmierins.microlending.condition.precondition;

import mmierins.microlending.condition.Condition;
import mmierins.microlending.condition.ConditionClassification;
import mmierins.microlending.domain.LoanApplication;
import mmierins.microlending.domain.nonpersistent.LoanExtensionApplication;
import mmierins.microlending.misc.AppConstants;
import mmierins.microlending.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ConditionClassification(ConditionClassification.ConditionType.LOAN_ISSUING_PRECONDITION)
public class IsNoActiveLoanIssuedToClient implements Condition<LoanApplication>  {

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public boolean verify(LoanApplication data) {
        int numActiveLoans = loanRepository.getCountOfActiveLoansByClient(data.getClient());
        return numActiveLoans == 0;
    }

    @Override
    public String getMessage() {
        return "Active loan already issued for client";
    }

    @Override
    public int getCode() {
        return AppConstants.ResultCode.NO_ACTIVE_LOAN_FOR_CLIENT_EXIST.getCode();
    }

}
