package mmierins.microlending.repository;

import mmierins.microlending.domain.Client;
import mmierins.microlending.domain.Loan;
import mmierins.microlending.service.impl.DomainTestDataGenerator;
import mmierins.microlending.MicroLendingApplication;
import mmierins.microlending.service.impl.TestSupporter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MicroLendingApplication.class)
@WebAppConfiguration
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class LoanRepositoryTest {

    @Autowired
    DomainTestDataGenerator domainTestDataGenerator;
    @Autowired
    LoanRepository loanRepository;

    @Autowired
    TestSupporter testSupporter;

    @Before
    public void init() {
        testSupporter.cleanDb();
    }

    @Test
    public void is_count_of_one_returned_when_one_active_loan_exists() {
        Client client = domainTestDataGenerator.createClientWithLoanInStatus(Loan.LoanStatus.ACTIVE);
        int count = loanRepository.getCountOfActiveLoansByClient(client);
        assertEquals(1, count);
    }

    @Test
    public void is_count_of_zero_returned_when_no_active_loan_exists() {
        Client client = domainTestDataGenerator.createClientWithLoanInStatus(Loan.LoanStatus.CLOSED);
        int count = loanRepository.getCountOfActiveLoansByClient(client);
        assertEquals(0, count);
    }

    @Test
    public void is_count_of_zero_returned_when_client_has_no_loans_in_system() {
        Client client = domainTestDataGenerator.createAndPersistClient("ip");
        int count = loanRepository.getCountOfActiveLoansByClient(client);
        assertEquals(0, count);
    }

    @Test
    public void is_active_loan_not_found_when_only_closed_loans_exist() {
        Client client = domainTestDataGenerator.createClientWithLoanInStatus(Loan.LoanStatus.CLOSED);
        Loan actual = loanRepository.findActiveLoanByClient(client);
        assertEquals(null, actual);
    }

    @Test
    public void is_active_and_closed_loans_found_when_queried_by_client() {
        Client client = domainTestDataGenerator.createClientWithLoanInStatus(Loan.LoanStatus.ACTIVE);
        domainTestDataGenerator.createLoanInStatusForClient(client, Loan.LoanStatus.CLOSED);
        Set<Loan> actual = loanRepository.findByClient(client);
        assertEquals(2, actual.size());
    }


}
