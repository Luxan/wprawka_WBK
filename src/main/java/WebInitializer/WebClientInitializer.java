package WebInitializer;

import com.gargoylesoftware.htmlunit.*;


public class WebClientInitializer {
    protected WebClient webClient = new WebClient(BrowserVersion.FIREFOX_45);

    public static WebClientInitializer getProxyInitializer(String proxyHostname, int proxyPort) {
        return new WebClientInitializerWithProxy(proxyHostname, proxyPort);
    }

    public static WebClientInitializer getNonProxyInitializer() {
        return new WebClientInitializer();
    }

    public WebClient getWebClient() {
        return webClient;
    }

    public void setup() {
        setUpWebClient();
    }

    protected void setUpWebClient() {
        setWebClientOptions();
        enableCookieManager();
    }

    private void enableCookieManager() {
            webClient.getCookieManager().setCookiesEnabled(true);
    }

    private void setWebClientOptions() {
        WebClientOptions options = webClient.getOptions();
        options.setJavaScriptEnabled(false);
        options.setRedirectEnabled(true);
        options.setCssEnabled(false);
        options.setThrowExceptionOnScriptError(false);
        options.setThrowExceptionOnFailingStatusCode(false);
    }

}
