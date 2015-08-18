package mmierins.microlending.service.api;

import mmierins.microlending.domain.nonpersistent.ApplicationResult;
import mmierins.microlending.domain.nonpersistent.LoanExtensionApplication;

public interface LoanExtender {

    ApplicationResult extendLoan(LoanExtensionApplication application);

}
