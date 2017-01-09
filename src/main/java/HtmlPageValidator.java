import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.List;

/**
 * Created by sgorokh on 09.01.17.
 */
public class HtmlPageValidator {

    private HtmlPageValidator() {}

    static public void validateNikPage(HtmlPage htmlPage) throws Exception {
        if (!htmlPage.asText().contains("Logowanie KROK 1")){
            throw new Exception("Nik Page is not valid!");
        }
    }

    public static void validatePasswordPage(HtmlPage passwordPage) throws Exception {
        if (!passwordPage.asText().contains("Logowanie KROK 2")){
            throw new Exception("Password Page is not valid!");
        }
    }

    public static boolean isMaskedPasswordRequired(HtmlPage passwordPage) throws Exception {
        List<HtmlForm> forms = passwordPage.getForms();
        for (HtmlForm form : forms) {
            if (form.getAttribute("id").equals("pinForm")){
                if (form.getAttribute("class").equals("f1 masked_margin")){
                    return true;
                }
                else{
                    return false;
                }
            }
        }
        throw new Exception("Cannot determine whether masked password required or not!");
    }

    public static void validateHomepage(HtmlPage loggedInPage) throws Exception {
        if (!loggedInPage.asText().contains("Rachunki")){
            throw new Exception("Home Page is not valid!");
        }
    }
}
