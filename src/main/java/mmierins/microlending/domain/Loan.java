package mmierins.microlending.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import mmierins.microlending.misc.AppConstants;
import mmierins.microlending.misc.MoneySerializer;
import mmierins.microlending.misc.View;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Loan extends IdEntity {

    public enum LoanStatus {
        ACTIVE,
        CLOSED
    }

    @JsonView(View.History.class)
    @JsonFormat(
            pattern= AppConstants.DATE_FORMATTING_PATTERN,
            timezone=AppConstants.DATE_FORMATTING_TIMEZONE)
    private Date dateIssued;

    @JsonView(View.History.class)
    @JsonSerialize(using = MoneySerializer.class)
    private  long amount;
    @JsonView(View.History.class)
    private int term;
    private double interestRate;
    @Enumerated(EnumType.STRING)
    @JsonView(View.History.class)
    private LoanStatus status;

    @OneToMany
    @JsonView(View.History.class)
    private Set<LoanExtension> extensions = new HashSet<>();

    @OneToOne
    private LoanApplication application;

    public Date getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(Date dateIssued) {
        this.dateIssued = dateIssued;
    }

    public LoanApplication getApplication() {
        return application;
    }

    public void setApplication(LoanApplication application) {
        this.application = application;
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

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public Set<LoanExtension> getExtensions() {
        return extensions;
    }

    public void setExtensions(Set<LoanExtension> extensions) {
        this.extensions = extensions;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

}
