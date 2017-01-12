package initializer;

import com.gargoylesoftware.htmlunit.*;


public class WebClientInitializer {

    private final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_45);

    public static WebClientInitializer getNonProxyInitializer() {
        return new WebClientInitializer();
    }

    public static WebClientInitializer getProxyInitializer(String hostname, int port) {
        WebClientInitializer initializer = new WebClientInitializer();
        initializer.setUpWebClientProxyOptions(hostname, port);
        return initializer;
    }

    private void setUpWebClientProxyOptions(String hostname, int port) {
        ProxyConfig proxyConfig = new ProxyConfig(hostname, port);
        webClient.getOptions().setProxyConfig(proxyConfig);
    }

    public WebClient getWebClient() {
        return webClient;
    }

    public void setup() {
        setUpWebClient();
    }

    private void setUpWebClient() {
        setWebClientOptions();
        enableCookieManager();
    }

    private void setWebClientOptions() {
        WebClientOptions options = webClient.getOptions();
        options.setJavaScriptEnabled(false);
        options.setRedirectEnabled(true);
        options.setCssEnabled(false);
        options.setThrowExceptionOnScriptError(false);
        options.setThrowExceptionOnFailingStatusCode(false);
    }

    private void enableCookieManager() {
        webClient.getCookieManager().setCookiesEnabled(true);
    }

}
