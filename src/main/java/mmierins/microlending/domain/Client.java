package mmierins.microlending.domain;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Client extends IdEntity {

    private String ip;

    @OneToMany
    private Set<LoanApplication> loanApplications = new HashSet<>();

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Set<LoanApplication> getLoanApplications() {
        return loanApplications;
    }

    public void setLoanApplications(Set<LoanApplication> loanApplications) {
        this.loanApplications = loanApplications;
    }

}
