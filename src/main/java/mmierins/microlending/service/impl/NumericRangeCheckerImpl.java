package mmierins.microlending.service.impl;

import mmierins.microlending.service.api.NumericRangeChecker;
import org.springframework.stereotype.Component;

@Component
public class NumericRangeCheckerImpl implements NumericRangeChecker {

    public boolean isValueWithinRange(long curValue, long min, long max) {
        return curValue >= min && curValue <= max;
    }

}
