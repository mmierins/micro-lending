package mmierins.microlending.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import mmierins.microlending.misc.AppConstants;
import mmierins.microlending.misc.View;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class LoanExtension extends IdEntity {

    @ManyToOne
    private Loan loan;
    @JsonView(View.History.class)
    @JsonFormat(
            pattern= AppConstants.DATE_FORMATTING_PATTERN,
            timezone=AppConstants.DATE_FORMATTING_TIMEZONE)
    private Date dateExtended;
    @JsonView(View.History.class)
    private int term;
    private double interest;

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public Date getDateExtended() {
        return dateExtended;
    }

    public void setDateExtended(Date dateExtended) {
        this.dateExtended = dateExtended;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

}
