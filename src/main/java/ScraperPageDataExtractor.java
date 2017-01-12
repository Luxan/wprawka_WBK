import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import models.Account;

import java.util.List;
import java.util.stream.Collectors;


class ScraperPageDataExtractor {

    static String extractPasswordFormActionName(HtmlPage htmlPage) {
        return findPasswordForm(htmlPage).getAttribute("action");
    }

    static String extractPasswordMaskNumber(HtmlPage htmlPage) {
        HtmlForm passwordForm = findPasswordForm(htmlPage);
        return findMaskNumber(passwordForm);
    }

    private static HtmlForm findPasswordForm(HtmlPage htmlPage) {
        return htmlPage.getForms().stream()
            .filter(f -> f.getAttribute("class")
            .contains("f1"))
            .findFirst()
            .orElseThrow(RuntimeException::new);
    }

    private static String findMaskNumber(HtmlForm passwordForm) {
        return passwordForm.getInputsByName("pinFragment:mask").get(0).getAttribute("value");
    }

    static List<Account> extractAccountBalances(HtmlPage homePage) {
        return homePage.getElementsByTagName("tr").stream()
            .filter(tr -> tr.getAttribute("class").equals("even"))
            .map(ScraperPageDataExtractor::accountRowToAccount)
            .collect(Collectors.toList());
    }

    private static Account accountRowToAccount(DomElement tr) {
        Account account = new Account();
        account.name = tr.getFirstElementChild().asText();
        account.balance = tr.getFirstElementChild().getNextElementSibling().asText();
        return account;
    }

}
