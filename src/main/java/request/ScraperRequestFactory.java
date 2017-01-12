package request;

import com.gargoylesoftware.htmlunit.WebClient;
import models.*;


public class ScraperRequestFactory {

    private static final String LOGIN_NIK_GET_URL = "https://www.centrum24.pl/centrum24-web/login";
    private static final String LOGIN_NIK_POST_URL = "https://www.centrum24.pl/centrum24-web/login/wicket:interface/:0:liveLoginBorder:authPanel:nikForm::IFormSubmitListener::";
    private static final String PASSWORD_POST_URL = "https://www.centrum24.pl/centrum24-web/";

    public static ScraperRequest createNikRequestGET(WebClient webClient) {
        return ScraperRequest.getBuilder()
            .setGetMethod()
            .setWebClient(webClient)
            .setUrl(LOGIN_NIK_GET_URL)
            .build();
    }

    public static ScraperRequest createNikRequestPOST(WebClient webClient, String nik) {
        return ScraperRequest.getBuilder()
            .setPostMethod()
            .setWebClient(webClient)
            .setUrl(LOGIN_NIK_POST_URL)
            .setParameter("nik", nik)
            .setParameter("nikForm_hf_0","")
            .setParameter("loginButton","DALEJ")
            .build();
    }

    public static ScraperRequest createPasswordRequestPOST(WebClient webClient, PasswordScraperParameters params) {
        return ScraperRequest.getBuilder()
            .setPostMethod()
            .setWebClient(webClient)
            .setUrl(PASSWORD_POST_URL + params.postUrlStr)
            .setParameter("pinForm_hf_0", "")
            .setParameter("loginButton", "DALEJ")
            .setParameter("pinFragment:pin", params.password)
            .setParameter("pinFragment:pinLength", "20")
            .setParameter("pinFragment:mask", params.maskNumberStr)
            .build();
    }

}
