package mmierins.microlending.service.impl;

import mmierins.microlending.domain.*;
import mmierins.microlending.repository.ClientRepository;
import mmierins.microlending.repository.LoanApplicationRepository;
import mmierins.microlending.repository.LoanExtensionRepository;
import mmierins.microlending.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class TestSupporter {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    LoanApplicationRepository loanApplicationRepository;
    @Autowired
    LoanExtensionRepository loanExtensionRepository;
    @Autowired
    LoanRepository loanRepository;

    @Transactional
    public void cleanDb() {
        for (Client client : clientRepository.findAll()) {
            for (LoanApplication application : client.getLoanApplications()) {
                if (application.getLoan() != null) {
                    for (LoanExtension extension : application.getLoan().getExtensions()) {
                        extension.setLoan(null);
                        loanExtensionRepository.saveAndFlush(extension);
                    }
                    Loan loan = application.getLoan();
                    loan.setApplication(null);
                    loan.getExtensions().clear();
                    loanRepository.saveAndFlush(loan);
                }
                application.setClient(null);
                application.setLoan(null);
                loanApplicationRepository.saveAndFlush(application);
            }
            client.getLoanApplications().clear();
            clientRepository.saveAndFlush(client);
        }

        clientRepository.deleteAllInBatch();
        loanExtensionRepository.deleteAllInBatch();
        loanApplicationRepository.deleteAllInBatch();
        loanRepository.deleteAllInBatch();
    }


}
