package gov.tn.dhs.opa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("box")
public class AppProperties {

    @Value("${server.port}")
    private String serverPort;

    @Value("${runstatus}")
    private String runStatus;

    @Value("${opa.baseurl}")
    private String baseurl;

    @Value("${opa.bearerTokenUrl}")
    private String bearerTokenUrl;

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(String runStatus) {
        this.runStatus = runStatus;
    }

    public String getBearerTokenUrl() {
        return bearerTokenUrl;
    }

    public void setBearerTokenUrl(String bearerTokenUrl) {
        this.bearerTokenUrl = bearerTokenUrl;
    }

    public String getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }

}
