import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import java.net.URL;
import java.util.Set;

/**
 * Created by sgorokh on 05.01.17.
 */
public class RequestGET extends Request {
    public RequestGET(WebClient webClient, URL url) throws Exception {
        super(webClient, url, HttpMethod.GET);
    }
    protected void setStandardHeaders() {
        addHeader("User-Agent","Mozilla/5.0 (X11; Linux x86_64; rv:50.0) Gecko/20100101 Firefox/50.0");
        addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        addHeader("Accept-Language","en-US,en;q=0.5");
        addHeader("Accept-Encoding","gzip, deflate, br");
        addHeader("Connection","keep-alive");
        addHeader("Upgrade-Insecure-Requests","1");
    }
}
