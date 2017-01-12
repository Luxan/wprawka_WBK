import initializer.WebClientInitializer;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import models.Account;
import models.PasswordScraperParameters;
import request.ScraperRequest;
import request.ScraperRequestFactory;
import java.util.*;


class Scraper {

    private final WebClientInitializer webClientInitializer;
    private HtmlPage homePage;

    private Scraper(WebClientInitializer initializer) {
        this.webClientInitializer = initializer;
    }

    static Scraper initializeWithProxy(String proxyHostname, int proxyPort) {
        WebClientInitializer webClientInitializer = WebClientInitializer.getProxyInitializer(proxyHostname, proxyPort);
        webClientInitializer.setup();
        return new Scraper(webClientInitializer);
    }

    static Scraper initialize() {
        WebClientInitializer webClientInitializer = WebClientInitializer.getNonProxyInitializer();
        webClientInitializer.setup();
        return new Scraper(webClientInitializer);
    }

    void performLoadLogInPage() {
        ScraperRequest nikRequestGET = ScraperRequestFactory.createNikRequestGET(webClientInitializer.getWebClient());
        nikRequestGET.execute();
    }

    void performLogInIntoBankAccount(String nik, String password) {
        HtmlPage passwordPage = performSubmitLoginAndLoadPasswordPage(nik);
        homePage = performSubmitPasswordAndLoadHomePage(passwordPage, password);
    }

    private HtmlPage performSubmitLoginAndLoadPasswordPage(String nik) {
        HtmlPage passwordPage = submitLoginForm(nik);
        ScraperRequestResultValidator.assertIncorrectLogin(passwordPage);
        return passwordPage;
    }

    private HtmlPage submitLoginForm(String nik) {
        ScraperRequest nikRequestPOST = ScraperRequestFactory.createNikRequestPOST(webClientInitializer.getWebClient(), nik);
        nikRequestPOST.execute();
        return nikRequestPOST.getAnswerPage();
    }

    private HtmlPage performSubmitPasswordAndLoadHomePage(HtmlPage passwordPage, String password) {
        HtmlPage loggedInPage = submitPasswordForm(passwordPage, password);
        ScraperRequestResultValidator.assertIncorrectPassword(loggedInPage);
        ScraperRequestResultValidator.assertInvalidHomepage(loggedInPage);
        return loggedInPage;
    }

    private HtmlPage submitPasswordForm(HtmlPage passwordPage, String password) {
        ScraperRequest passwordRequestPOST = createPasswordSubmissionRequest(password, passwordPage);
        passwordRequestPOST.execute();
        return passwordRequestPOST.getAnswerPage();
    }

    private ScraperRequest createPasswordSubmissionRequest(String password, HtmlPage passwordPage) {
        PasswordScraperParameters params = PasswordParameterCreator.getPasswordParameters(password, passwordPage);
        return ScraperRequestFactory.createPasswordRequestPOST(webClientInitializer.getWebClient(), params);
    }

    List<Account> getAccounts() {
        return ScraperPageDataExtractor.extractAccountBalances(homePage);
    }

}
