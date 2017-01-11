import com.gargoylesoftware.htmlunit.html.HtmlPage;


public class HtmlPageValidator {

    private static void throwIfLoggedOut(HtmlPage htmlPage) {
        if (htmlPage.asText().contains("Nastąpiło poprawne wylogowanie"))
            throw new RuntimeException();
    }

    private static void throwIf404(HtmlPage htmlPage) {
        if (htmlPage.asText().contains("404"))
            throw new RuntimeException();
    }

    private static void throwIfLoginError(HtmlPage htmlPage) {
        if (htmlPage.asText().contains("Nieprawidłowa liczba znaków"))
            throw new RuntimeException();
    }

    private static void throwIfIncorrectPassword(HtmlPage htmlPage) {
        if (htmlPage.asText().contains("W polu 'pin' wprowadź od '4 do '20' znaków."))
            throw new RuntimeException();
        else if (htmlPage.asText().contains("Wprowadzono błędny NIK i/lub PIN"))
            throw new RuntimeException();
    }

    private static void checkPageForErrors(HtmlPage htmlPage) {
        throwIfLoggedOut(htmlPage);
        throwIf404(htmlPage);
        throwIfLoginError(htmlPage);
        throwIfIncorrectPassword(htmlPage);
    }

    public static void validateNikPage(HtmlPage htmlPage) {
        if (!htmlPage.asText().contains("Logowanie KROK 1")) {
            checkPageForErrors(htmlPage);
            throw new RuntimeException();
        }
    }

    public static void validatePasswordPage(HtmlPage passwordPage) {
        if (!passwordPage.asText().contains("Logowanie KROK 2")) {
            checkPageForErrors(passwordPage);
            throw new RuntimeException();
        }
    }

    public static boolean isMaskedPasswordRequired(HtmlPage passwordPage) {
        return passwordPage.getForms().stream()
                .filter(f -> f.getAttribute("class").equals("f1 masked_margin"))
                .findFirst()
                .orElse(null) != null;
    }

    public static void validateHomepage(HtmlPage loggedInPage) {
        if (!loggedInPage.asText().contains("Rachunki")) {
            checkPageForErrors(loggedInPage);
            throw new RuntimeException("Home Page is not valid!");
        }
    }

}
