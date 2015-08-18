package mmierins.microlending.condition.risk;

import mmierins.microlending.condition.Condition;
import mmierins.microlending.condition.ConditionClassification;
import mmierins.microlending.domain.LoanApplication;
import mmierins.microlending.misc.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ConditionClassification(ConditionClassification.ConditionType.LOAN_ISSUING_RISK)
public class IsLoanWithMaxAmountRequestedDuringEarlyHours implements Condition<LoanApplication> {

    private IsApplicationMadeDuringEarlyHours isApplicationMadeDuringEarlyHours;
    private IsLoanWithMaxAmountRequested isLoanWithMaxAmountRequested;

    @Autowired
    public IsLoanWithMaxAmountRequestedDuringEarlyHours(
            IsApplicationMadeDuringEarlyHours isApplicationMadeDuringEarlyHours,
            IsLoanWithMaxAmountRequested isLoanWithMaxAmountRequested) {
        this.isApplicationMadeDuringEarlyHours = isApplicationMadeDuringEarlyHours;
        this.isLoanWithMaxAmountRequested = isLoanWithMaxAmountRequested;
    }

    @Override
    public boolean verify(LoanApplication data) {
        return isApplicationMadeDuringEarlyHours.verify(data) &&
               isLoanWithMaxAmountRequested.verify(data);
    }

    @Override
    public String getMessage() {
        return "Application made during early hours with max amount of loan";
    }

    @Override
    public int getCode() {
        return AppConstants.ResultCode.APPLICATION_WITH_MAX_AMOUNT_DURING_EARLY_HOURS.getCode();
    }

}
