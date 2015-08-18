package mmierins.microlending.service.impl;

import mmierins.microlending.domain.Client;
import mmierins.microlending.domain.Loan;
import mmierins.microlending.domain.LoanApplication;
import mmierins.microlending.domain.Parameter;
import mmierins.microlending.misc.AppConstants;
import mmierins.microlending.misc.DateUtils;
import mmierins.microlending.repository.ClientRepository;
import mmierins.microlending.repository.LoanApplicationRepository;
import mmierins.microlending.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.GregorianCalendar;

@Component
@Transactional
public class DomainTestDataGenerator {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    LoanApplicationRepository loanApplicationRepository;
    @Autowired
    LoanRepository loanRepository;

    public static final int YEAR = 2015;
    public static final int MONTH = 0;
    public static final int DAY = 10;

    public static int MAX_AMOUNT_OF_LOAN = 500;
    public static int LESS_THAN_MAX_AMOUNT_OF_LOAN = MAX_AMOUNT_OF_LOAN - 1;

    public static Date AT_MIDNIGHT = DateUtils.convertToDate(LocalDateTime.of(2015, 1, 1, 0, 0));
    public static final Date SIX_AM = DateUtils.convertToDate(LocalDateTime.of(2015, 1, 1, 6, 0));
    public static final Date BETWEEN_MIDNIGHT_AND_SIX_AM = DateUtils.convertToDate(LocalDateTime.of(2015, 1, 1, 1, 0));
    public static final Date BEFORE_MIDNIGHT = DateUtils.convertToDate(LocalDateTime.of(2015, 1, 1, 23, 59));

    public static final Parameter MAX_AMOUNT_OF_LOAN_PARAM =
            new Parameter(
                    AppConstants.ParameterKey.MAX_AMOUNT_OF_LOAN.name(),
                    String.valueOf(MAX_AMOUNT_OF_LOAN)
            );
    public static final int MAX_APPLICATIONS_PER_DAY = 3;
    public static final Parameter MAX_APPLICATIONS_PER_DAY_PARAM =
            new Parameter(
                    AppConstants.ParameterKey.MAX_APPLICATIONS_PER_DAY.name(),
                    String.valueOf(MAX_APPLICATIONS_PER_DAY)
            );

    public Client createClientWithLoanInStatus(Loan.LoanStatus status) {
        Client client = createAndPersistClient("ip1");
        createLoanInStatusForClient(client, status);
        return client;
    }

    public Loan createLoanInStatusForClient(Client client, Loan.LoanStatus status) {
        Loan loan = createAndPersistLoan(status);
        createAndPersistLoanApplication(client, loan);
        return loan;
    }

    private Loan createAndPersistLoan(Loan.LoanStatus status) {
        Loan loan = new Loan();
        loan.setStatus(status);
        loanRepository.saveAndFlush(loan);
        return loan;
    }

    private LoanApplication createAndPersistLoanApplication(Client client, Loan loan) {
        LoanApplication application = new LoanApplication();
        application.setClient(client);
        application.setLoan(loan);
        loan.setApplication(application);
        client.getLoanApplications().add(application);
        loanApplicationRepository.saveAndFlush(application);
        return application;
    }

    public LoanApplication createAndPersistLoanApplication(Client client, int day, int hour) {
        LoanApplication application = new LoanApplication();
        application.setDateApplied(new GregorianCalendar(YEAR, MONTH, day, hour, 0).getTime());
        application.setClient(client);
        client.getLoanApplications().add(application);
        loanApplicationRepository.saveAndFlush(application);
        return application;
    }

    public Client createAndPersistClient(String ip) {
        Client client = new Client();
        client.setIp(ip);
        clientRepository.saveAndFlush(client);
        return client;
    }

}
