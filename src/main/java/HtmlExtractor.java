import com.gargoylesoftware.htmlunit.html.*;
import models.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class HtmlExtractor {

    public static String extractPasswordFormActionName(HtmlPage htmlPage) {
        return findPasswordForm(htmlPage).getAttribute("action");
    }

    public static String extractPasswordMaskNumber(HtmlPage htmlPage) {
        HtmlForm passwordForm = findPasswordForm(htmlPage);
        return findMaskNumber(passwordForm);
    }

    public static String extractPasswordLengthNumber(HtmlPage htmlPage) {
        HtmlForm passwordForm = findPasswordForm(htmlPage);
        return findLengthNumber(passwordForm);
    }

    public static List<Integer> extractPasswordPositions(HtmlPage htmlPage) {
        HtmlForm passwordForm = findPasswordForm(htmlPage);
        return findPasswordLetterPositions(passwordForm);
    }

    private static HtmlForm findPasswordForm(HtmlPage htmlPage) {
        return htmlPage.getForms().stream().filter(f -> f.getAttribute("class").contains("f1")).findFirst().orElse(null);
    }

    private static List<Integer> findPasswordLetterPositions(HtmlForm passwordForm) {
        return passwordForm.getElementsByTagName("label").stream()
                .filter(l -> l.getAttribute("for").contains("pass"))
                .map(e -> Integer.parseInt(e.asText()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static String findLengthNumber(HtmlForm passwordForm) {
        return passwordForm.getInputsByName("pinFragment:pinLength").get(0).getAttribute("value");
    }

    private static String findMaskNumber(HtmlForm passwordForm) {
        return passwordForm.getInputsByName("pinFragment:mask").get(0).getAttribute("value");
    }

    public static List<Account> extractAccountBalances(HtmlPage homePage) {
        return homePage.getElementsByTagName("tr").stream()
                .filter(tr -> tr.getAttribute("class").equals("even"))
                .map(tr -> new Account(
                                tr.getFirstElementChild().asText(),
                                tr.getFirstElementChild().getNextElementSibling().asText()))
                .collect(Collectors.toList());
    }

}
