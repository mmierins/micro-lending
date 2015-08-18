package mmierins.microlending.condition;

import mmierins.microlending.domain.LoanApplication;
import mmierins.microlending.condition.risk.IsApplicationMadeDuringEarlyHours;
import org.junit.Before;
import org.junit.Test;

import static mmierins.microlending.service.impl.DomainTestDataGenerator.*;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class IsApplicationMadeDuringEarlyHoursTest {

    private IsApplicationMadeDuringEarlyHours isApplicationMadeDuringEarlyHours;
    private LoanApplication application;

    @Before
    public void init() {
        isApplicationMadeDuringEarlyHours = new IsApplicationMadeDuringEarlyHours();
        application = new LoanApplication();
    }

    @Test
    public void is_verified_when_sharp_at_midnight() {
        application.setDateApplied(AT_MIDNIGHT);
        assertTrue(isApplicationMadeDuringEarlyHours.verify(application));
    }

    @Test
    public void is_not_verified_when_sharp_at_six_am() {
        application.setDateApplied(SIX_AM);
        assertFalse(isApplicationMadeDuringEarlyHours.verify(application));
    }

    @Test
    public void is_verified_when_between_midnight_and_six_am() {
        application.setDateApplied(BETWEEN_MIDNIGHT_AND_SIX_AM);
        assertTrue(isApplicationMadeDuringEarlyHours.verify(application));
    }

    @Test
    public void is_not_verified_when_before_midnight() {
        application.setDateApplied(BEFORE_MIDNIGHT);
        assertFalse(isApplicationMadeDuringEarlyHours.verify(application));
    }

}
