package mmierins.microlending.service.impl;

import com.fasterxml.jackson.annotation.JsonView;
import mmierins.microlending.domain.nonpersistent.LoanExtensionApplication;
import mmierins.microlending.domain.Client;
import mmierins.microlending.domain.Loan;
import mmierins.microlending.domain.LoanApplication;
import mmierins.microlending.domain.nonpersistent.*;
import mmierins.microlending.misc.View;
import mmierins.microlending.misc.AppConstants;
import mmierins.microlending.misc.DateUtils;
import mmierins.microlending.misc.MoneyUtils;
import mmierins.microlending.repository.ClientRepository;
import mmierins.microlending.repository.LoanApplicationRepository;
import mmierins.microlending.repository.LoanRepository;
import mmierins.microlending.repository.ParameterRepository;
import mmierins.microlending.service.api.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Set;

@RestController
public class RestLoanService {

    @Autowired
    private IPAddressResolver ipAddressResolver;
    @Autowired
    private ClientAuthenticator clientAuthenticator;
    @Autowired
    private LoanIssuer loanIssuer;
    @Autowired
    private LoanExtender loanExtender;
    @Autowired
    private LoanApplicationRepository loanApplicationRepository;
    @Autowired
    private DateTimeProvider dateTimeProvider;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    ParameterRepository parameterRepository;

    private Logger logger = Logger.getLogger(getClass());

    private void setResponseStatus(HttpServletResponse response, HttpStatus status) {
        response.setStatus(status.value());
    }

    private LoanApplication fillAndPersistLoanApplication(Client client, LoanApplication application) {
        logger.info(
                String.format(
                        "Creating a new LoanApplication, client ID=%d, amount=%d, term=%d",
                        client.getId(), MoneyUtils.centsFromEuros(application.getAmount()), application.getTerm()
                )
        );

        application.setDateApplied(DateUtils.convertToDate(dateTimeProvider.getCurrentDateTime()));
        application.setAmount(MoneyUtils.centsFromEuros(application.getAmount()));
        client.getLoanApplications().add(application);
        application.setClient(client);
        loanApplicationRepository.save(application);

        logger.info(String.format("LoanApplication persisted with ID=%d", application.getId()));

        return application;
    }

    @RequestMapping(value = "/loans", method = RequestMethod.POST)
    public GenericRestServiceResult applyForLoan(@RequestBody LoanApplication application,
            HttpServletRequest request, HttpServletResponse response) {
        logger.info("Starting processing loan issue request");

        GenericRestServiceResult result;

        try {
            String ipAddress = ipAddressResolver.resolveIP(request);
            Client client = clientAuthenticator.getOrCreateNewClientByIP(ipAddress);
            fillAndPersistLoanApplication(client, application);
            ApplicationResult issuingResult = loanIssuer.issueLoan(application);

            result = new GenericRestServiceResult(
                    issuingResult.getMessage(),
                    issuingResult.getDescription(),
                    issuingResult.getCode()
            );
            if (issuingResult.getStatus() == ApplicationStatus.ACCEPTED) {
                setResponseStatus(response, HttpStatus.CREATED);
            } else {
                setResponseStatus(response, HttpStatus.PRECONDITION_FAILED);
            }
        } catch (Exception ex) {
            String message = "Error while processing loan issuing request";
            logger.error(message, ex);
            result = new GenericRestServiceResult(message);
            setResponseStatus(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("Processing of loan issuing request is finished");

        return result;
    }

    @RequestMapping(value = "/loans/extension", method = RequestMethod.PATCH)
    public GenericRestServiceResult extendLoan(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Starting processing loan issue request");

        GenericRestServiceResult result;

        try {
            String ipAddress = ipAddressResolver.resolveIP(request);
            Client client = clientAuthenticator.getOrCreateNewClientByIP(ipAddress);
            LoanExtensionApplication application = new LoanExtensionApplication();
            application.setClient(client);
            int term = parameterRepository.findByKey(AppConstants.ParameterKey.DEF_LOAN_EXTENSION_TERM.name()).getValueAsLong().intValue();
            application.setTerm(term);
            ApplicationResult applicationResult = loanExtender.extendLoan(application);

            result = new GenericRestServiceResult(
                    applicationResult.getMessage(),
                    applicationResult.getDescription(),
                    applicationResult.getCode()
            );
            if (applicationResult.getStatus() == ApplicationStatus.ACCEPTED) {
                setResponseStatus(response, HttpStatus.OK);
            } else {
                setResponseStatus(response, HttpStatus.PRECONDITION_FAILED);
            }
        } catch (Exception ex) {
            String message = "Error while processing loan extension request";
            logger.error(message, ex);
            result = new GenericRestServiceResult(message);
            setResponseStatus(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("Processing of loan issuing request is finished");

        return result;
    }

    @RequestMapping(value = "/loans/history", method = RequestMethod.GET)
    @JsonView(View.History.class)
    public LoanHistoryResult showHistory(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Starting processing history of loan request");

        LoanHistoryResult result;

        try {
            String ipAddress = ipAddressResolver.resolveIP(request);
            Client client = clientRepository.findByIp(ipAddress);
            if (client == null) {
                logger.info(String.format("Client with ip %s not found in the system", ipAddress));
                result = new LoanHistoryResult(Collections.emptySet());
            } else {
                logger.info(String.format(
                        "Client with ip %s found in the system, retrieving history of loans", ipAddress));
                Set<Loan> loans = loanRepository.findByClient(client);
                result = new LoanHistoryResult(loans);
            }
            result.setCode(AppConstants.ResultCode.LOAN_HISTORY_RETRIEVED_SUCCESFULLY.getCode());
        } catch (Exception ex) {
            String message = "Error while retrieving loan history";
            logger.error(message, ex);
            result = new LoanHistoryResult(message);
            setResponseStatus(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("Processing of history of loan request is finished");

        return result;
    }

}
