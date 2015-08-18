package mmierins.microlending.service.api;

import mmierins.microlending.domain.LoanApplication;
import mmierins.microlending.domain.nonpersistent.ApplicationResult;

public interface LoanIssuer {

    ApplicationResult issueLoan(LoanApplication application);

}
