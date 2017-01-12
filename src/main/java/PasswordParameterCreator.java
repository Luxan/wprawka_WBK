import com.gargoylesoftware.htmlunit.html.HtmlPage;
import models.*;


class PasswordParameterCreator {

    static PasswordScraperParameters getPasswordParameters(String password, HtmlPage passwordPage) {
        PasswordScraperParameters params = new PasswordScraperParameters();
        if (isMaskedPasswordRequired(passwordPage)){
            params.maskNumberStr = ScraperPageDataExtractor.extractPasswordMaskNumber(passwordPage);
            params.password = getMaskedPassword(password, Integer.parseInt(params.maskNumberStr));
        }
        else
            params.password = password;
        params.postUrlStr = ScraperPageDataExtractor.extractPasswordFormActionName(passwordPage);
        return params;
    }

    private static String getMaskedPassword(String password, int maskNumber) {
        String maskedPassword = "";
        for (int i = 0; i < password.length(); i++){
            if ((maskNumber >> i) % 2 == 1) {
                maskedPassword += password.charAt(i);
            }
        }
        return maskedPassword;
    }

    private static boolean isMaskedPasswordRequired(HtmlPage passwordPage) {
        return passwordPage.getForms().stream()
                .anyMatch(f -> f.getAttribute("class").equals("f1 masked_margin"));
    }

}
