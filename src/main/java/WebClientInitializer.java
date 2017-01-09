import com.gargoylesoftware.htmlunit.*;

/**
 * Created by sgorokh on 05.01.17.
 */
public class WebClientInitializer {
    private WebClient webClient = new WebClient(BrowserVersion.FIREFOX_45);
    private String proxyHostname = "";
    private int proxyPort = 0;

    public WebClientInitializer(String proxyHostname, int proxyPort) {
        this.proxyHostname = proxyHostname;
        this.proxyPort = proxyPort;
    }

    public WebClientInitializer() {}

    public WebClient getWebClient(){
        return webClient;
    }

    public void setup() throws Exception {
        try{
            if (proxyHostname == "" || proxyPort == 0)
            {
                setUpWebClient();
            }
            else
            {
                setUpWebClientWithProxy();
            }
        }catch (Exception e){
            throw new Exception("Cannot set up WebClient: " + e.getMessage());
        }
    }

    private void setUpWebClient() throws Exception {
        setWebClientNonProxyOptions();
        setWebClientRefreshOptions();
        enableCookieManager();
    }

    private void setUpWebClientWithProxy() throws Exception {
        setWebClientOptionsWithProxy();
        setWebClientRefreshOptions();
        enableCookieManager();
    }

    private void setUpWebClientProxyOptions() throws Exception {
        try{
            ProxyConfig proxyConfig = new ProxyConfig(proxyHostname, proxyPort);
            webClient.getOptions().setProxyConfig(proxyConfig);
        }catch (Exception e){
            throw new Exception("Cannot set up Proxy Options for WebClient: " + e.getMessage());
        }
    }

    private void enableCookieManager() throws Exception {
        try{
            webClient.getCookieManager().setCookiesEnabled(true);
            webClient.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            webClient.addRequestHeader("Upgrade-Insecure-Requests", "1");
        }
        catch (Exception e){
            throw new Exception("Cannot enable Cookie Manager: " + e.getMessage());
        }
    }

    private void setWebClientRefreshOptions() throws Exception {
        try {
            webClient.setRefreshHandler(new WaitingRefreshHandler(10000));
        }catch (Exception e){
            throw new Exception("Cannot set up refresh options for WebClient: " + e.getMessage());
        }
    }

    private void setWebClientOptionsWithProxy() throws Exception {
        try{
            setUpWebClientProxyOptions();
            setWebClientNonProxyOptions();
        }catch (Exception e){
            throw new Exception("Cannot set up WebClient with proxy: " + e.getMessage());
        }
    }

    private void setWebClientNonProxyOptions() throws Exception {
        try{
            WebClientOptions webClientOptions = webClient.getOptions();
            webClientOptions.setJavaScriptEnabled(false);
            webClientOptions.setRedirectEnabled(true);
            webClientOptions.setCssEnabled(false);
            webClientOptions.setThrowExceptionOnScriptError(false);
            webClientOptions.setThrowExceptionOnFailingStatusCode(false);
        }catch (Exception e){
            throw new Exception("Cannot set up non proxy options for WebClient: " + e.getMessage());
        }
    }
}
