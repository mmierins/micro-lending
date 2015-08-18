package mmierins.microlending.condition;

import mmierins.microlending.domain.LoanApplication;
import mmierins.microlending.repository.ParameterRepository;
import mmierins.microlending.misc.AppConstants;
import mmierins.microlending.condition.risk.IsApplicationMadeDuringEarlyHours;
import mmierins.microlending.condition.risk.IsLoanWithMaxAmountRequested;
import mmierins.microlending.condition.risk.IsLoanWithMaxAmountRequestedDuringEarlyHours;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static mmierins.microlending.service.impl.DomainTestDataGenerator.*;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IsLoanWithMaxAmountRequestedDuringEarlyHoursTest {

    private IsLoanWithMaxAmountRequested isLoanWithMaxAmountRequested;
    private IsApplicationMadeDuringEarlyHours isApplicationMadeDuringEarlyHours;
    private IsLoanWithMaxAmountRequestedDuringEarlyHours isLoanWithMaxAmountRequestedDuringEarlyHours;

    private LoanApplication createLoanApplication(int amount, Date date) {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setAmount(amount);
        loanApplication.setDateApplied(date);
        return loanApplication;
    }

    @Before
    public void init() {
        ParameterRepository parameterRepositoryMock = mock(ParameterRepository.class);
        when(parameterRepositoryMock.findByKey(AppConstants.ParameterKey.MAX_AMOUNT_OF_LOAN.name())).thenReturn(MAX_AMOUNT_OF_LOAN_PARAM);
        isLoanWithMaxAmountRequested = new IsLoanWithMaxAmountRequested(parameterRepositoryMock);
        isApplicationMadeDuringEarlyHours = new IsApplicationMadeDuringEarlyHours();
        isLoanWithMaxAmountRequestedDuringEarlyHours = new IsLoanWithMaxAmountRequestedDuringEarlyHours(isApplicationMadeDuringEarlyHours, isLoanWithMaxAmountRequested);
//        maxAmountAfterMidnightRule.setRulesValidationResult(new RulesValidationResult());
    }

    @Test
    public void is_max_amount_after_midnight_detected() {
        LoanApplication loanApplication = createLoanApplication(MAX_AMOUNT_OF_LOAN, AT_MIDNIGHT);
        assertTrue(isLoanWithMaxAmountRequestedDuringEarlyHours.verify(loanApplication));
    }

    @Test
    public void is_max_amount_before_midnight_detected() {
        LoanApplication loanApplication = createLoanApplication(MAX_AMOUNT_OF_LOAN, BEFORE_MIDNIGHT);
        assertFalse(isLoanWithMaxAmountRequestedDuringEarlyHours.verify(loanApplication));
    }

    @Test
    public void is_less_than_max_amount_after_midnight_detected() {
        LoanApplication loanApplication = createLoanApplication(LESS_THAN_MAX_AMOUNT_OF_LOAN, AT_MIDNIGHT);
        assertFalse(isLoanWithMaxAmountRequestedDuringEarlyHours.verify(loanApplication));
    }

    @Test
    public void is_less_than_max_amount_before_midnight_detected() {
        LoanApplication loanApplication = createLoanApplication(LESS_THAN_MAX_AMOUNT_OF_LOAN, BEFORE_MIDNIGHT);
        assertFalse(isLoanWithMaxAmountRequestedDuringEarlyHours.verify(loanApplication));
    }

}
