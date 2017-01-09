import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Set;

/**
 * Created by sgorokh on 05.01.17.
 */
public abstract class Request {
    private WebClient webClient;
    private WebRequest webRequest;
    private HtmlPage ansPage;

    public Request(WebClient webClient, URL url, HttpMethod httpMethod) throws Exception {
        assert webClient != null : "WebClient cannot be null!";
        assert url != null : "Url Cannot be null!";
        assert httpMethod != null : "httpMethod Cannot be null!";

        try{
            this.webClient = webClient;
            webRequest = new WebRequest(url, httpMethod);
            webRequest.setRequestParameters(new ArrayList<NameValuePair>());
            setStandardHeaders();
        }catch (Exception e){
            throw new Exception("Cannot create Request: " + e.getMessage());
        }
    }
    protected void addHeader(String name, String value) {
        webRequest.getAdditionalHeaders().put(name,value);
    }

    protected abstract void setStandardHeaders();

    public void setAdditionalParameters(Set<NameValuePair> additionalParameters) {
        for (NameValuePair nameValuePair : additionalParameters) {
            webRequest.getRequestParameters().add(nameValuePair);
        }
    }
    public void setAdditionalHeaders(Set<NameValuePair> additionalHeaders) {
        for (NameValuePair nameValuePair : additionalHeaders) {
            webRequest.getAdditionalHeaders().put(nameValuePair.getName(), nameValuePair.getValue());
        }
    }
    public void execute() throws Exception {
        try{
            ansPage = webClient.getPage(webRequest);
        }catch (Exception e){
            throw new Exception("Cannot execute request: " + e.getMessage());
        }
    }
    public HtmlPage getAnsPage(){
        return ansPage;
    }
    public void setHostHeader(String host) {
        addHeader("Host",host);
    }
    public void setRefererHeader(String referer) {
        addHeader("Referer",referer);
    }
}
