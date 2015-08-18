package mmierins.microlending.condition;

import mmierins.microlending.domain.LoanApplication;
import mmierins.microlending.domain.Parameter;
import mmierins.microlending.repository.ParameterRepository;
import mmierins.microlending.misc.AppConstants;
import mmierins.microlending.condition.risk.IsLoanWithMaxAmountRequested;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IsLoanWithMaxAmountRequestedTest {

    private IsLoanWithMaxAmountRequested isLoanWithMaxAmountRequested;

    private static final Parameter MAX_AMOUNT_OF_LOAN =
        new Parameter(AppConstants.ParameterKey.MAX_AMOUNT_OF_LOAN.name(), "500");

    @Before
    public void init() {
        ParameterRepository parameterRepositoryMock = mock(ParameterRepository.class);
        when(parameterRepositoryMock.findByKey(AppConstants.ParameterKey.MAX_AMOUNT_OF_LOAN.name())).thenReturn(MAX_AMOUNT_OF_LOAN);
        isLoanWithMaxAmountRequested = new IsLoanWithMaxAmountRequested(parameterRepositoryMock);
    }

    @Test
    public void is_condition_verified_when_max_amount() {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setAmount(500);
        assertTrue(isLoanWithMaxAmountRequested.verify(loanApplication));
    }

    @Test
    public void is_condition_nonverified_when_less_than_max_amount() {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setAmount(300);
        assertFalse(isLoanWithMaxAmountRequested.verify(loanApplication));
    }

}
