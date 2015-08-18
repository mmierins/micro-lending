package mmierins.microlending.condition;

import mmierins.microlending.domain.Client;
import mmierins.microlending.domain.LoanApplication;
import mmierins.microlending.repository.LoanApplicationRepository;
import mmierins.microlending.repository.ParameterRepository;
import mmierins.microlending.misc.AppConstants;
import mmierins.microlending.condition.risk.IsClientMadeMaxApplicationsPerDay;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static mmierins.microlending.service.impl.DomainTestDataGenerator.*;
import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IsClientMadeMaxApplicationsPerDayTest {

    private IsClientMadeMaxApplicationsPerDay isClientMadeMaxApplicationsPerDay;
    private LoanApplicationRepository loanApplicationRepository;
    private ParameterRepository parameterRepositoryMock;
    private LoanApplication application;



    @Before
    public void init() {
        parameterRepositoryMock = mock(ParameterRepository.class);
        loanApplicationRepository = mock(LoanApplicationRepository.class);

        when(parameterRepositoryMock.findByKey(
                AppConstants.ParameterKey.MAX_APPLICATIONS_PER_DAY.name())).
        thenReturn(MAX_APPLICATIONS_PER_DAY_PARAM);

        application = new LoanApplication();
        application.setClient(new Client());
        application.setDateApplied(new Date());
    }

    @Test
    public void is_condition_verified_when_max_applicatons() {
        when(loanApplicationRepository.findByClientAndDateApplied(any(), any())).
        thenReturn(4 /* e.g. 3 already exist + the one that is made */);

        isClientMadeMaxApplicationsPerDay = new IsClientMadeMaxApplicationsPerDay(parameterRepositoryMock, loanApplicationRepository);
        assertTrue(isClientMadeMaxApplicationsPerDay.verify(application));
    }

    @Test
     public void is_condition_not_verified_when_less_than_max_applicatons() {
        when(loanApplicationRepository.findByClientAndDateApplied(any(), any())).
        thenReturn(MAX_APPLICATIONS_PER_DAY-1);

        isClientMadeMaxApplicationsPerDay = new IsClientMadeMaxApplicationsPerDay(parameterRepositoryMock, loanApplicationRepository);
        assertFalse(isClientMadeMaxApplicationsPerDay.verify(application));
    }

}
