package request;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import models.PasswordRequestPostParameters;


public class RequestCreator {

    private static final String LOGIN_NIK_GET_URL = "https://www.centrum24.pl/centrum24-web/login";
    private static final String LOGIN_NIK_POST_URL = "https://www.centrum24.pl/centrum24-web/login/wicket:interface/:0:liveLoginBorder:authPanel:nikForm::IFormSubmitListener::";
    private static final String PASSWORD_POST_URL = "https://www.centrum24.pl/centrum24-web/";

    public static Request createNikRequestGET(WebClient webClient) {
        return Request.getBuilder()
                .setHttpMethod(HttpMethod.GET)
                .setWebClient(webClient)
                .setUrl(LOGIN_NIK_GET_URL)
                .build();
    }

    public static Request createNikRequestPOST(WebClient webClient, String nik) {
        return Request.getBuilder()
                .setHttpMethod(HttpMethod.POST)
                .setWebClient(webClient)
                .setUrl(LOGIN_NIK_POST_URL)
                .setParameter("nik", nik)
                .setParameter("nikForm_hf_0","")
                .setParameter("loginButton","DALEJ")
                .build();
    }

    public static Request createPasswordRequestPOST(WebClient webClient, PasswordRequestPostParameters params) {
        return Request.getBuilder()
                .setHttpMethod(HttpMethod.POST)
                .setWebClient(webClient)
                .setUrl(PASSWORD_POST_URL + params.getPostUrlStr())
                .setParameter("pinForm_hf_0", "")
                .setParameter("loginButton", "DALEJ")
                .setParameter("pinFragment:pin", params.getPassword())
                .build();
    }

    public static Request createMaskedPasswordRequestPOST(WebClient webClient, PasswordRequestPostParameters params) {
        return Request.getBuilder()
                .setHttpMethod(HttpMethod.POST)
                .setWebClient(webClient)
                .setUrl(PASSWORD_POST_URL + params.getPostUrlStr())
                .setParameter("pinForm_hf_0", "")
                .setParameter("loginButton", "DALEJ")
                .setParameter("pinFragment:pin", params.getPassword())
                .setParameter("pinFragment:pinLength", params.getMaskLengthStr())
                .setParameter("pinFragment:mask", params.getMaskNumberStr())
                .build();
    }

}
