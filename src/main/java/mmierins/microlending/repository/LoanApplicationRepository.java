package mmierins.microlending.repository;

import mmierins.microlending.domain.Client;
import mmierins.microlending.domain.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

    @Transactional(readOnly = true)
    @Query( "SELECT COUNT(application) " +
            "FROM Client c " +
            "JOIN c.loanApplications application " +
            "WHERE c = ?1 " +
            "AND " +
            "YEAR(application.dateApplied) = YEAR(?2) " +
            "AND " +
            "MONTH(application.dateApplied) = MONTH(?2) " +
            "AND " +
            "DAY_OF_MONTH(application.dateApplied) = DAY_OF_MONTH(?2)"
    )
    int findByClientAndDateApplied(Client client, Date dateApplied);

}
