package mmierins.microlending.service.api;

public interface NumericRangeChecker {

    boolean isValueWithinRange(long curValue, long min, long max);

}
