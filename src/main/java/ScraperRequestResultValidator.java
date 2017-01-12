import com.gargoylesoftware.htmlunit.html.HtmlPage;
import exceptions.InvalidLogin;
import exceptions.InvalidPassword;


class ScraperRequestResultValidator {

    static void assertIncorrectLogin(HtmlPage htmlPage) {
        String body = htmlPage.asText();
        boolean isNotAskingForPassword = !body.contains("Logowanie KROK 2");
        boolean isInvalidLogin = body.contains("Nieprawidłowa liczba znaków");
        if (isNotAskingForPassword || isInvalidLogin)
            throw new InvalidLogin();
    }

    static void assertIncorrectPassword(HtmlPage htmlPage) {
        String body = htmlPage.asText();
        boolean invalidPinLength = body.contains("W polu 'pin' wprowadź od '4 do '20' znaków.");
        boolean invalidPin = body.contains("Wprowadzono błędny NIK i/lub PIN");
        if (invalidPinLength || invalidPin)
            throw new InvalidPassword();
    }

    static void assertInvalidHomepage(HtmlPage loggedInPage) {
        if (!loggedInPage.asText().contains("Rachunki")) {
            throw new RuntimeException("Home Page is not valid!");
        }
    }

}
