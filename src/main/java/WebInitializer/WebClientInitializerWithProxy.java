package WebInitializer;

import com.gargoylesoftware.htmlunit.ProxyConfig;

/**
 * Created by sgorokh on 11.01.17.
 */
public class WebClientInitializerWithProxy extends WebClientInitializer {
    private final String proxyHostname;
    private final int proxyPort;

    public WebClientInitializerWithProxy(String proxyHostname, int proxyPort) {
        this.proxyHostname = proxyHostname;
        this.proxyPort = proxyPort;
    }

    @Override
    protected void setUpWebClient() {
        setUpWebClientProxyOptions();
        super.setUpWebClient();
    }

    private void setUpWebClientProxyOptions() {
        ProxyConfig proxyConfig = new ProxyConfig(proxyHostname, proxyPort);
        webClient.getOptions().setProxyConfig(proxyConfig);
    }
}
