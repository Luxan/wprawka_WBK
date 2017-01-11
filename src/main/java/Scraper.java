import WebInitializer.WebClientInitializer;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import models.Account;
import models.PasswordRequestPostParameters;
import request.Request;
import request.RequestCreator;
import java.util.*;


public class Scraper {

    private static final String POXY_HOSTNAME = "127.0.0.1";
    private static final int PROXY_PORT = 8080;
    private HtmlPage homePage;
    private WebClientInitializer webClientInitializer;

    private Scraper(WebClientInitializer initializer) {
        this.webClientInitializer = initializer;
    }

    public static Scraper initializeWithProxy() {
        WebClientInitializer webClientInitializer = WebClientInitializer.getProxyInitializer(POXY_HOSTNAME, PROXY_PORT);
        webClientInitializer.setup();
        return new Scraper(webClientInitializer);
    }

    public static Scraper initialize() {
        WebClientInitializer webClientInitializer = WebClientInitializer.getNonProxyInitializer();
        webClientInitializer.setup();
        return new Scraper(webClientInitializer);
    }

    public void performLogInIntoBankAccount(String nik, String password) {
        HtmlPage passwordPage = performSubmitLoginAndLoadPasswordPage(nik);
        homePage = performSubmitPasswordAndLoadHomePage(passwordPage, password);
    }

    private HtmlPage performSubmitPasswordAndLoadHomePage(HtmlPage passwordPage, String password) {
        HtmlPage loggedInPage = submitPasswordForm(passwordPage, password);
        HtmlPageValidator.validateHomepage(loggedInPage);
        return loggedInPage;
    }

    private HtmlPage submitPasswordForm(HtmlPage passwordPage, String password) {
        String passwordFormActionName = HtmlExtractor.extractPasswordFormActionName(passwordPage);
        Request passwordRequestPOST = createPasswordSubmissionRequest(password, passwordFormActionName, passwordPage);
        passwordRequestPOST.execute();
        return passwordRequestPOST.getAnswerPage();
    }

    private Request createPasswordSubmissionRequest(String password, String url, HtmlPage passwordPage) {
        if (HtmlPageValidator.isMaskedPasswordRequired(passwordPage))
            return createMaskedPasswordSubmissionRequest(password, url, passwordPage);
        else {
            PasswordRequestPostParameters params = new PasswordRequestPostParameters(password, url);
            return RequestCreator.createPasswordRequestPOST(webClientInitializer.getWebClient(), params);
        }
    }

    private Request createMaskedPasswordSubmissionRequest(String password, String url, HtmlPage passwordPage) {
        PasswordRequestPostParameters passwordParameters = getPasswordParameters(password, url, passwordPage);
        return RequestCreator.createMaskedPasswordRequestPOST(webClientInitializer.getWebClient(), passwordParameters);
    }

    private PasswordRequestPostParameters getPasswordParameters(String password, String url, HtmlPage passwordPage) {
        List<Integer> passwordPositions = HtmlExtractor.extractPasswordPositions(passwordPage);
        String maskedPasswordStr = MaskedPasswordCreator.getMaskedPassword(password, passwordPositions);
        String passwordLengthNumberStr = HtmlExtractor.extractPasswordLengthNumber(passwordPage);
        String maskNumberStr = HtmlExtractor.extractPasswordMaskNumber(passwordPage);
        return new PasswordRequestPostParameters(maskedPasswordStr, url, maskNumberStr, passwordLengthNumberStr);
    }

    private HtmlPage performSubmitLoginAndLoadPasswordPage(String nik) {
        HtmlPage passwordPage = submitLoginForm(nik);
        HtmlPageValidator.validatePasswordPage(passwordPage);
        return passwordPage;
    }

    private HtmlPage submitLoginForm(String nik) {
        Request nikRequestPOST = RequestCreator.createNikRequestPOST(webClientInitializer.getWebClient(), nik);
        nikRequestPOST.execute();
        return nikRequestPOST.getAnswerPage();
    }

    public void performLoadLogInPage() {
        HtmlPage nikPage = loadLoginPage();
        HtmlPageValidator.validateNikPage(nikPage);
    }

    private HtmlPage loadLoginPage() {
        Request nikRequestGET = RequestCreator.createNikRequestGET(webClientInitializer.getWebClient());
        nikRequestGET.execute();
        return nikRequestGET.getAnswerPage();
    }

    public List<Account> getAccounts() {
        return HtmlExtractor.extractAccountBalances(homePage);
    }

}
