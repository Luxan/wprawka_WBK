package request;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


public class ScraperRequest {

    private final WebClient webClient;
    private final WebRequest webRequest;
    private HtmlPage ansPage;

    private ScraperRequest(WebClient webClient, WebRequest webRequest) {
        this.webClient = webClient;
        this.webRequest = webRequest;
    }

    public void execute() {
        try {
            ansPage = webClient.getPage(webRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HtmlPage getAnswerPage() {
        return ansPage;
    }

    static Builder getBuilder() {
        return new Builder();
    }

    static class Builder {

        private WebClient webClient;
        private WebRequest webRequest;
        private String url;
        private HttpMethod httpMethod;
        private final List<NameValuePair> parameters = new ArrayList<>();
        private final Map<String, String> headers = new HashMap<>();

        Builder setPostMethod() {
            this.httpMethod = HttpMethod.POST;
            return this;
        }

        Builder setGetMethod() {
            this.httpMethod = HttpMethod.GET;
            return this;
        }

        Builder setParameter(String name, String value) {
            this.parameters.add(new NameValuePair(name, value));
            return this;
        }

        Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        Builder setWebClient(WebClient webClient) {
            this.webClient = webClient;
            return this;
        }

        ScraperRequest build() {
            try {
                webRequest = new WebRequest(new URL(url), httpMethod);
                webRequest.setRequestParameters(parameters);
                webRequest.setAdditionalHeaders(headers);
                return new ScraperRequest(webClient, webRequest);
            } catch (MalformedURLException e) {
                throw new RuntimeException();
            }
        }

    }

}
