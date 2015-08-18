package mmierins.microlending.repository;

import mmierins.microlending.domain.Client;
import mmierins.microlending.domain.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Transactional(readOnly = true)
    @Query( "SELECT COUNT(loan) " +
            "FROM Loan loan " +
            "JOIN loan.application application " +
            "WHERE " +
            "loan.status = 'ACTIVE' " +
            "AND " +
            "application.client = ?1")
    int getCountOfActiveLoansByClient(Client client);

    @Transactional(readOnly = true)
    @Query( "SELECT loan " +
            "FROM Loan loan " +
            "JOIN loan.application application " +
            "WHERE " +
            "loan.status = 'ACTIVE' " +
            "AND " +
            "application.client = ?1")
    Loan findActiveLoanByClient(Client client);

    @Transactional(readOnly = true)
    @Query( "SELECT loan " +
            "FROM Loan loan " +
            "JOIN loan.application application " +
            "JOIN application.client client " +
            "WHERE client = ?1")
    Set<Loan> findByClient(Client client);

}
