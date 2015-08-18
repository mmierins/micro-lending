package mmierins.microlending.misc;

public class AppConstants {

    public static final String DATE_FORMATTING_PATTERN = "yyyy-MM-dd hh:mm";
    public static final String DATE_FORMATTING_TIMEZONE = "EET";

    public enum ParameterKey {
        MAX_APPLICATIONS_PER_DAY,
        MAX_AMOUNT_OF_LOAN,
        MIN_AMOUNT_OF_LOAN,
        BASE_LOAN_INTEREST_RATE,
        LOAN_EXTENSION_INTEREST_RATE,
        MAX_ACTIVE_LOANS_PER_CLIENT,
        MIN_LOAN_TERM,
        MAX_LOAN_TERM,
        MIN_LOAN_EXTENSION_TERM,
        MAX_LOAN_EXTENSION_TERM,
        DEF_LOAN_EXTENSION_TERM
    }

    public enum ResultCode {
        ACTIVE_LOAN_FOR_CLIENT_EXIST(1),
        NO_ACTIVE_LOAN_FOR_CLIENT_EXIST(2),
        PARAMETER_OUT_OF_RANGE(3),
        MAX_APPLICATIONS_FOR_CLIENT_PER_DAY_REACHED(20),
        APPLICATION_WITH_MAX_AMOUNT_DURING_EARLY_HOURS(21),
        MAX_LOAN_AMOUNT_REQUESTED(22),
        APPLICATION_MADE_DURING_EARLY_HOURS(23),
        LOAN_ISSUED_SUCCCESFULLY(30),
        LOAN_EXTENDED_SUCCESFULLY(31),
        LOAN_HISTORY_RETRIEVED_SUCCESFULLY(32),
        EXCEPTION_OCCURED(40);

        int code;

        ResultCode(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        @Override
        public String toString() {
            return String.valueOf(code);
        }
    }

}
