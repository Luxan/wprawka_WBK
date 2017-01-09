import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by sgorokh on 03.01.17.
 */
public class htmlUnit {
    private final String nik = "23040540";
    private final String password = "ABCabc123";
    private final String wbkHostname = "www.centrum24.pl";
    private final String mainBankWebsiteUrl = "http://www.bzwbk.pl/";
    private final String loginNikGETUrl = "https://www.centrum24.pl/centrum24-web/login";
    private final String loginNikPOSTUrl = "https://www.centrum24.pl/centrum24-web/login/wicket:interface/:0:liveLoginBorder:authPanel:nikForm::IFormSubmitListener::";
    private final String loginPasswordPOSTUrl = "https://www.centrum24.pl/centrum24-web/";

    private final String proxyHostname = "127.0.0.1";
    private final int proxyPort = 8080;

    private HtmlPage homePage;

    private WebClientInitializer webClientInitializer;

    public htmlUnit() throws Exception {
        webClientInitializer = new WebClientInitializer(proxyHostname, proxyPort);
        webClientInitializer.setup();
    }

    private void performLogInIntoBankAcc() throws Exception {
        HtmlPage passwordPage = performSubmitLoginAndLoadPasswordPage();
        homePage = performSubmitPasswordAndLoadHomePage(passwordPage);
    }

    private HtmlPage performSubmitPasswordAndLoadHomePage(HtmlPage passwordPage) throws Exception {
        String passwordFormActionName = HtmlExtractor.extractPasswordFormActionName(passwordPage);
        URL passwordSubmissionRequestUrl = new URL(loginPasswordPOSTUrl + passwordFormActionName);
        RequestPOST passwordRequestPOST = new RequestPOST(webClientInitializer.getWebClient(), passwordSubmissionRequestUrl);

        setParametersForPasswordSubmissionRequest(passwordPage, passwordRequestPOST);
        setHeadersForPasswordSubmissionRequest(passwordPage, passwordRequestPOST);

        passwordRequestPOST.execute();
        HtmlPage loggedInPage = passwordRequestPOST.getAnsPage();
        HtmlPageValidator.validateHomepage(loggedInPage);
        return loggedInPage;
    }

    private void setHeadersForPasswordSubmissionRequest(HtmlPage passwordPage, RequestPOST passwordRequestPOST) {
        Set<NameValuePair> passwordHeaders = new HashSet<NameValuePair>();
        passwordHeaders.add(new NameValuePair("Referer", passwordPage.getUrl().toString()));
        passwordRequestPOST.setAdditionalHeaders(passwordHeaders);
    }

    private void setParametersForPasswordSubmissionRequest(HtmlPage passwordPage, RequestPOST passwordRequestPOST) throws Exception {
        Set<NameValuePair> passwordParameters = new HashSet<NameValuePair>();
        if (HtmlPageValidator.isMaskedPasswordRequired(passwordPage)){
            passwordParameters.addAll(getMaskedPasswordParameters(passwordPage));
        }
        else {
            passwordParameters.addAll(getNonMaskedPasswordParameters());
        }
        passwordParameters.addAll(getCommonParameters());
        passwordRequestPOST.setAdditionalParameters(passwordParameters);
    }

    private Set<NameValuePair> getCommonParameters() {
        Set<NameValuePair> params = new HashSet<NameValuePair>();
        params.add(new NameValuePair("pinForm_hf_0", ""));
        params.add(new NameValuePair("loginButton", "DALEJ"));
        return params;
    }

    private Set<NameValuePair> getNonMaskedPasswordParameters() {
        Set<NameValuePair> params = new HashSet<NameValuePair>();
        params.add(new NameValuePair("pinFragment:pin", password));
        return params;
    }

    private Set<NameValuePair> getMaskedPasswordParameters(HtmlPage passwordPage) throws Exception {
        Set<NameValuePair> params = new HashSet<NameValuePair>();
        List<Integer> passwordPositions = HtmlExtractor.extractPasswordPositions(passwordPage);
        String maskNumberStr = HtmlExtractor.extractPasswordMaskNumber(passwordPage);
        String passwordLengthNumberStr = HtmlExtractor.extractPasswordLengthNumber(passwordPage);
        String maskedPasswordStr = MaskedPasswordCreator.getMaskedPassword(password, passwordPositions);
        params.add(new NameValuePair("pinFragment:mask", maskNumberStr));
        params.add(new NameValuePair("pinFragment:pinLength", passwordLengthNumberStr));
        params.add(new NameValuePair("pinFragment:pin", maskedPasswordStr));
        return params;
    }

    private HtmlPage performSubmitLoginAndLoadPasswordPage() throws Exception {
        RequestPOST nikRequestPOST = createLoginSubmissionRequest();
        setHeadersForLoginSubmissionRequest(nikRequestPOST);
        setParametersForLoginSubmissionRequest(nikRequestPOST);
        nikRequestPOST.execute();
        HtmlPage passwordPage = nikRequestPOST.getAnsPage();
        HtmlPageValidator.validatePasswordPage(passwordPage);
        return passwordPage;
    }

    private RequestPOST createLoginSubmissionRequest() throws Exception {
        return new RequestPOST(webClientInitializer.getWebClient(), new URL(loginNikPOSTUrl));
    }

    private void setParametersForLoginSubmissionRequest(RequestPOST nikRequestPOST) throws Exception {
        Set<NameValuePair> nikParameters = new HashSet<NameValuePair>();
        nikParameters.add(new NameValuePair("nik", nik));
        nikParameters.add(new NameValuePair("nikForm_hf_0",""));
        nikParameters.add(new NameValuePair("loginButton","DALEJ"));
        nikRequestPOST.setAdditionalParameters(nikParameters);
    }

    private void setHeadersForLoginSubmissionRequest(RequestPOST nikRequestPOST) {
        nikRequestPOST.setHostHeader(wbkHostname);
        nikRequestPOST.setRefererHeader(loginNikGETUrl);
    }

    private void performLoadLogInPage() throws Exception {
        RequestGET nikRequestGET = new RequestGET(webClientInitializer.getWebClient(), new URL(loginNikGETUrl));
        nikRequestGET.setHostHeader(wbkHostname);
        nikRequestGET.setRefererHeader(mainBankWebsiteUrl);
        nikRequestGET.execute();
        HtmlPage nikPage = nikRequestGET.getAnsPage();

        HtmlPageValidator.validateNikPage(nikPage);
    }

    public static void main(String[] args){
        try {
            htmlUnit htmlUnit = new htmlUnit();
            htmlUnit.performLoadLogInPage();
            htmlUnit.performLogInIntoBankAcc();
            htmlUnit.printAccountBalances();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void printAccountBalances() throws Exception {
        Set<NameValuePair> accountBalances = getAccountBalances();
        for (NameValuePair account : accountBalances){
            System.out.println("Account name: " + account.getName() + "\n Balance: " + account.getValue());
        }
    }

    private Set<NameValuePair> getAccountBalances() throws Exception {
        return HtmlExtractor.extractAccountBalances(homePage);
    }
}
