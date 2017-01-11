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


public class Request {

    private WebClient webClient;
    private WebRequest webRequest;
    private HtmlPage ansPage;

    protected Request(WebClient webClient, WebRequest webRequest) {
        this.webClient = webClient;
        this.webRequest = webRequest;
    }

    public void execute() {
        try {
            ansPage = webClient.getPage(webRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HtmlPage getAnswerPage() {
        return ansPage;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {

        private WebClient webClient;
        private WebRequest webRequest;
        private String url;
        private HttpMethod httpMethod;
        private List<NameValuePair> parameters = new ArrayList<>();
        protected Map<String, String> headers = new HashMap<>();

        protected void setStandardHeaders() {}

        public Builder setParameter(String name, String value) {
            this.parameters.add(new NameValuePair(name, value));
            return this;
        }

        protected Builder setHttpMethod(HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setWebClient(WebClient webClient) {
            this.webClient = webClient;
            return this;
        }

        public Request build() {
            setStandardHeaders();
            try {
                webRequest = new WebRequest(new URL(url), httpMethod);
                webRequest.setRequestParameters(parameters);
                webRequest.setAdditionalHeaders(headers);
                return new Request(webClient, webRequest);
            } catch (MalformedURLException e) {
                throw new RuntimeException();
            }
        }

    }

}
