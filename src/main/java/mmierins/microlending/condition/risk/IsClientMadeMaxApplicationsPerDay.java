package mmierins.microlending.condition.risk;

import mmierins.microlending.condition.Condition;
import mmierins.microlending.condition.ConditionClassification;
import mmierins.microlending.domain.LoanApplication;
import mmierins.microlending.repository.LoanApplicationRepository;
import mmierins.microlending.repository.ParameterRepository;
import mmierins.microlending.misc.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ConditionClassification(ConditionClassification.ConditionType.LOAN_ISSUING_RISK)
public class IsClientMadeMaxApplicationsPerDay implements Condition<LoanApplication> {

    private ParameterRepository parameterRepository;
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    public IsClientMadeMaxApplicationsPerDay(ParameterRepository parameterRepository,
                                             LoanApplicationRepository loanApplicationRepository) {
        this.parameterRepository = parameterRepository;
        this.loanApplicationRepository = loanApplicationRepository;
    }

    @Override
    public boolean verify(LoanApplication data) {
        Long maxApplicationsPerDay =
                parameterRepository
                        .findByKey(AppConstants.ParameterKey
                                .MAX_APPLICATIONS_PER_DAY
                                .name())
                        .getValueAsLong();

        int numCurApplications = loanApplicationRepository
                .findByClientAndDateApplied(data.getClient(), data.getDateApplied());
        return numCurApplications > maxApplicationsPerDay;
    }

    @Override
    public String getMessage() {
        return "Client has made max applications allowed per day";
    }

    @Override
    public int getCode() {
        return AppConstants.ResultCode.MAX_APPLICATIONS_FOR_CLIENT_PER_DAY_REACHED.getCode();
    }
}
