package domain;

import application.GithubDTO;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@ApplicationScoped
public class GithubService {

  private WebTarget target;

  @PostConstruct
  public void init() throws NoSuchAlgorithmException, KeyManagementException {

    boolean isContainerized = System.getProperty("isContainerized") != null;

    String serviceHost = isContainerized ?
        "http://mockserver:1080" :
        "https://api.github.com";
    String url = serviceHost + "/repos/cwansart/testcontainers-university";

    // Since we may access an https route and the local Java's keystore may not contain the key, we ignore the
    // certificate validation.
    TrustManager[] noopTrustManager = new TrustManager[]{
        new X509TrustManager() {
          @Override
          public X509Certificate[] getAcceptedIssuers() {
            return null;
          }

          @Override
          public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
          }

          @Override
          public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
          }
        }
    };

    SSLContext sc = SSLContext.getInstance("ssl");
    sc.init(null, noopTrustManager, null);

    this.target = ClientBuilder.newBuilder().sslContext(sc).build().target(url);
  }

  public GithubDTO getApiData() {

    return this.target.request(MediaType.APPLICATION_JSON).get(
        new GenericType<GithubDTO>() {});
  }
}
