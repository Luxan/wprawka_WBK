import com.gargoylesoftware.htmlunit.InteractivePage;
import com.gargoylesoftware.htmlunit.activex.javascript.msxml.XMLDOMElement;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import org.jcp.xml.dsig.internal.dom.DOMEnvelopedTransform;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by sgorokh on 05.01.17.
 */
public class HtmlExtractor {
    HtmlPage htmlPage;

    private HtmlExtractor(HtmlPage htmlPage) {
        this.htmlPage = htmlPage;
    }

    static public String extractPasswordFormActionName(HtmlPage htmlPage) throws Exception {
        HtmlForm passwordForm = findPasswordForm(htmlPage);
        return passwordForm.getAttribute("action");
    }

    public static String extractPasswordMaskNumber(HtmlPage htmlPage) throws Exception {
        HtmlForm passwordForm = findPasswordForm(htmlPage);
        return findMaskNumber(passwordForm);
    }
    public static String extractPasswordLengthNumber(HtmlPage htmlPage) throws Exception {
        HtmlForm passwordForm = findPasswordForm(htmlPage);
        return findLengthNumber(passwordForm);
    }
    public static List<Integer> extractPasswordPositions(HtmlPage htmlPage) throws Exception {
        HtmlForm passwordForm = findPasswordForm(htmlPage);
        List<Integer> findPasswordLetterPositions = findPasswordLetterPositions(passwordForm);
        validateSizeOfPassLetterPos(findPasswordLetterPositions);
        return findPasswordLetterPositions;
    }

    private static void validateSizeOfPassLetterPos(List<Integer> findPasswordLetterPositions) throws Exception {
        int size = findPasswordLetterPositions.size();
        if (size < 10){
            throw new Exception("Found wrong number of password letter positions. Expected [10] Found [" + Integer.toString(size) + "]");
        }
    }

    private static HtmlForm findPasswordForm(HtmlPage htmlPage) throws Exception {
        List<HtmlForm> forms = htmlPage.getForms();
        for (HtmlForm form : forms){
            if (form.getAttribute("class").equals("f1 left_margin") ||
                    form.getAttribute("class").equals("f1 masked_margin")){
                return form;
            }
        }
        throw new Exception("Cannot Find Password Form!");
    }

    private static List<Integer> findPasswordLetterPositions(HtmlForm passwordForm) {
        DomNodeList<HtmlElement> labels = passwordForm.getElementsByTagName("label");
        List<Integer> positions = new ArrayList<Integer>();
        int position = 1;
        for (HtmlElement label : labels){
            if (label.getAttribute("for").equals("pass" + Integer.toString(position))){
                positions.add(Integer.parseInt(label.asText()));
                position++;
            }
        }
        return positions;
    }

    private static String findLengthNumber(HtmlForm passwordForm) throws Exception {
        List<HtmlInput> lengthInputs = passwordForm.getInputsByName("pinFragment:pinLength");
        if (lengthInputs.size() == 1){
            return lengthInputs.get(0).getAttribute("value");
        }
        throw new Exception("Cannot find password length number!");
    }

    private static String findMaskNumber(HtmlForm passwordForm) throws Exception {
        List<HtmlInput> maskInputs = passwordForm.getInputsByName("pinFragment:mask");
        if (maskInputs.size() == 1){
            return maskInputs.get(0).getAttribute("value");
        }
        throw new Exception("Cannot find password mask number");
    }

    public static Set<NameValuePair> extractAccountBalances(HtmlPage homePage) throws Exception {
        Set<NameValuePair> accountData = new HashSet<NameValuePair>();

        Set<DomElement> accounts = getAccounts(homePage);
        for (DomElement accountInfo : accounts) {
            String name = getAccountName(accountInfo);
            String balanceStr = getAccountBalance(accountInfo);
            accountData.add(new NameValuePair(name, balanceStr));
        }
        return accountData;
    }

    private static String getAccountName(DomElement accountInformationBlock) throws Exception {
        Iterable<DomElement> informationDetails = accountInformationBlock.getChildElements();
        for (DomElement detail : informationDetails){
            if (detail.getAttribute("class").equals("name")){
                return detail.asText();
            }
        }
        throw new Exception("Cannot find name of account!");
    }

    private static String getAccountBalance(DomElement accountInformationBlock) throws Exception {
        Iterable<DomElement> informationDetails = accountInformationBlock.getChildElements();
        for (DomElement detail : informationDetails){
            if (detail.getAttribute("class").equals("money")){
                return detail.asText();
            }
        }
        throw new Exception("Cannot find balance of account!");
    }

    private static Set<DomElement> getAccounts(HtmlPage homePage) throws Exception {
        Set<DomElement> accounts = new HashSet<DomElement>();
        DomNodeList<DomElement> trs = homePage.getElementsByTagName("tr");
        for (DomElement tr : trs){
            if (tr.getAttribute("class").equals("even")){
                accounts.add(tr);
            }
        }
        return accounts;
    }
}
