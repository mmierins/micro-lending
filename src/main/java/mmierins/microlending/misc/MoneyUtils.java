package mmierins.microlending.misc;

public class MoneyUtils {

    public static long eurosFromCents(long cents) {
        return cents / 100;
    }

    public static long centsFromEuros(long euros) {
        return euros * 100;
    }

}
