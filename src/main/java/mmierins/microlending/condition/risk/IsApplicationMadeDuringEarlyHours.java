package mmierins.microlending.condition.risk;

import mmierins.microlending.condition.Condition;
import mmierins.microlending.domain.LoanApplication;
import mmierins.microlending.misc.AppConstants;
import mmierins.microlending.misc.DateUtils;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class IsApplicationMadeDuringEarlyHours implements Condition<LoanApplication> {

    @Override
    public boolean verify(LoanApplication data) {
        LocalTime midnight = LocalTime.of(0, 0);
        LocalTime sixAM = LocalTime.of(6, 0);
        LocalTime applicationTime = DateUtils.convertFromDate(data.getDateApplied());
        return applicationTime.equals(midnight) ||
                (applicationTime.isAfter(midnight) && applicationTime.isBefore(sixAM));
    }

    @Override
    public String getMessage() {
        return "Application made during early hours";
    }

    @Override
    public int getCode() {
        return AppConstants.ResultCode.APPLICATION_MADE_DURING_EARLY_HOURS.getCode();
    }

}
