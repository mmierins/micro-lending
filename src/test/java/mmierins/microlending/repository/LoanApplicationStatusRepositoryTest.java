package mmierins.microlending.repository;

import mmierins.microlending.service.impl.DomainTestDataGenerator;
import mmierins.microlending.MicroLendingApplication;
import mmierins.microlending.domain.Client;
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
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MicroLendingApplication.class)
@WebAppConfiguration
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class LoanApplicationStatusRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private DomainTestDataGenerator domainTestDataGenerator;

    @Autowired
    TestSupporter testSupporter;

    private Client client1;
    private Client client2;

    @Before
    public void setupTestData() {
        testSupporter.cleanDb();

        client1 = domainTestDataGenerator.createAndPersistClient("ip1");
        client2 = domainTestDataGenerator.createAndPersistClient("ip2");

        domainTestDataGenerator.createAndPersistLoanApplication(client1, DomainTestDataGenerator.DAY, 0);
        domainTestDataGenerator.createAndPersistLoanApplication(client1, DomainTestDataGenerator.DAY, 10);
        domainTestDataGenerator.createAndPersistLoanApplication(client1, DomainTestDataGenerator.DAY, 20);
        domainTestDataGenerator.createAndPersistLoanApplication(client1, DomainTestDataGenerator.DAY - 1, 1);
        domainTestDataGenerator.createAndPersistLoanApplication(client1, DomainTestDataGenerator.DAY + 1, 1);

        domainTestDataGenerator.createAndPersistLoanApplication(client2, DomainTestDataGenerator.DAY, 1);

        clientRepository.saveAndFlush(client1);
        clientRepository.saveAndFlush(client2);
    }

    @Test
    public void is_all_loan_applications_found_when_present() {
        int countOfApplications =
                loanApplicationRepository.
                findByClientAndDateApplied(
                        client1,
                        new GregorianCalendar(
                                DomainTestDataGenerator.YEAR,
                                DomainTestDataGenerator.MONTH,
                                DomainTestDataGenerator.DAY, 0, 0).
                                getTime()
                );

        assertEquals(3, countOfApplications);
    }

    @Test
    public void is_zero_loan_applications_returned_when_no_applications_present_on_date() {
        int countOfApplications =
                loanApplicationRepository.
                findByClientAndDateApplied(
                        client2,
                        new GregorianCalendar(
                                DomainTestDataGenerator.YEAR,
                                DomainTestDataGenerator.MONTH,
                                DomainTestDataGenerator.DAY + 1, 0, 0).
                                getTime()
                );

        assertEquals(0, countOfApplications);
    }

}
