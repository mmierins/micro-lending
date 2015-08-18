package mmierins.microlending.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class LoanApplication extends IdEntity {

    public enum LoanApplicationStatus {
        REJECTED,
        ACCEPTED
    }

    @ManyToOne
    private Client client;
    @OneToOne
    private Loan loan;

    private long amount;
    private int term;
    private Date dateApplied;
    @Enumerated(EnumType.STRING)
    private LoanApplicationStatus loanApplicationStatus;

    public LoanApplicationStatus getLoanApplicationStatus() {
        return loanApplicationStatus;
    }

    public void setLoanApplicationStatus(LoanApplicationStatus loanApplicationStatus) {
        this.loanApplicationStatus = loanApplicationStatus;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public Date getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(Date dateApplied) {
        this.dateApplied = dateApplied;
    }

}
