package mmierins.microlending.acceptance;

import com.jayway.restassured.RestAssured;
import mmierins.microlending.MicroLendingApplication;
import mmierins.microlending.misc.AppConstants;
import mmierins.microlending.service.impl.TestSupporter;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.restassured.RestAssured.given;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MicroLendingApplication.class)
@WebIntegrationTest("server.port:0")
public class AcceptanceTests {

    private static final int LOAN_AMOUNT = 100;
    private static final int LOAN_TERM = 30;
    private static final String REQ_BODY = String.format(
            "{ \"amount\": %d, \"term\": %d }", LOAN_AMOUNT, LOAN_TERM);

    @Value("${local.server.port}")
    int port;

    @Autowired
    TestSupporter testSupporter;

    @Before
    public void init() {
        RestAssured.port = port;
        testSupporter.cleanDb();
    }

    private void setupLoanForClient() {
        given().
            contentType("application/json").
            body(REQ_BODY).
        when().
            post("/loans");
    }

    private void setupLoanExtensionForClient() {
        setupLoanForClient();

        given().
        when().
            patch("/loans/extension");
    }

    @Test
    public void is_loan_issued_when_a_new_client_is_applying_for_loan() {
        given().
            contentType("application/json").
                body(REQ_BODY).
        when().
            post("/loans").
        then().
            statusCode(HttpStatus.CREATED.value()).
            body("message", Matchers.is("Loan issued succesfully"));
    }

    @Test
    public void is_loan_denied_when_a_client_already_has_one() {
        setupLoanForClient();

        given().
            contentType("application/json").
                body(REQ_BODY).
                when().
            post("/loans").
        then().
            statusCode(HttpStatus.PRECONDITION_FAILED.value()).
            body("message", Matchers.is("Loan not issued; application does not meet the required criteria"));
    }

    @Test
    public void is_loan_extended_when_loan_exists() {
        setupLoanForClient();

        given().
        when().
                patch("/loans/extension").
        then().
            statusCode(HttpStatus.OK.value()).
            body("message", Matchers.is("Loan extension performed succesfully"));
    }

    @Test
    public void is_loan_extension_denied_when_loan_does_not_exist() {
        given().
        when().
                patch("/loans/extension").
        then().
            statusCode(HttpStatus.PRECONDITION_FAILED.value()).
            body("message", Matchers.is("Loan extension denied"));
    }

    @Test
    public void is_multiple_loan_extensions_performed() {
        setupLoanForClient();

        for (int i = 0; i < 3; i++) {
            given().
            when().
                patch("/loans/extension").
            then().
                statusCode(HttpStatus.OK.value()).
                body("message", Matchers.is("Loan extension performed succesfully"));
        }
    }

    @Test
    public void is_empty_loan_history_returned_when_client_has_no_loans_in_system() {
        given().
        when().
            get("/loans/history").
        then().
            statusCode(HttpStatus.OK.value()).
            body("code", Matchers.is(AppConstants.ResultCode.LOAN_HISTORY_RETRIEVED_SUCCESFULLY.getCode())).
            body("loanHistory", Matchers.empty());
    }

    @Test
    public void is_loan_appearing_in_history_when_client_has_loan() {
        setupLoanForClient();

        given().
        when().
            get("/loans/history").
        then().
            statusCode(HttpStatus.OK.value()).
            body("code", Matchers.is(AppConstants.ResultCode.LOAN_HISTORY_RETRIEVED_SUCCESFULLY.getCode())).
            body("loanHistory[0].amount", Matchers.is("100.00")).
            body("loanHistory[0].term", Matchers.is(30)).
            body("loanHistory[0].status", Matchers.is("ACTIVE")).
            body("loanHistory[0].extensions", Matchers.empty());
    }

    @Test
    public void is_loan_extension_appearing_in_history_when_client_has_loan_extension() {
        setupLoanExtensionForClient();

        given().
        when().
            get("/loans/history").
        then().
            statusCode(HttpStatus.OK.value()).
            body("code", Matchers.is(AppConstants.ResultCode.LOAN_HISTORY_RETRIEVED_SUCCESFULLY.getCode())).
            body("loanHistory[0].amount", Matchers.is("100.00")).
            body("loanHistory[0].term", Matchers.is(30)).
            body("loanHistory[0].status", Matchers.is("ACTIVE")).
            body("loanHistory[0].extensions[0].term", Matchers.is(7));
    }

}
