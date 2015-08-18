package mmierins.microlending.domain.nonpersistent;

import com.fasterxml.jackson.annotation.JsonView;
import mmierins.microlending.domain.Loan;
import mmierins.microlending.misc.View;

import java.util.Set;

public class LoanHistoryResult extends GenericRestServiceResult {

    @JsonView(View.History.class)
    private Set<Loan> loanHistory;

    public LoanHistoryResult(Set<Loan> loanHistory) {
        this.loanHistory = loanHistory;
    }

    public LoanHistoryResult(String message) {
        super(message);
    }

    public Set<Loan> getLoanHistory() {
        return loanHistory;
    }

    public void setLoanHistory(Set<Loan> loanHistory) {
        this.loanHistory = loanHistory;
    }

}
