package application;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Path("/github")
public class GithubResource {

  // TODO: perhaps read from properties file?
  private static final String URL = System.getenv("IS_CONTAINERIZED") == null ?
      "https://api.github.com/repos/cwansart/testcontainers-university" :
      "http://mockserver:1080/repos/cwansart/testcontainers-university";

  private final WebTarget target;

  public GithubResource() throws NoSuchAlgorithmException, KeyManagementException {

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

    this.target = ClientBuilder.newBuilder().sslContext(sc).build().target(URL);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getGithubData() {

    GithubDTO githubResponse = this.target.request(MediaType.APPLICATION_JSON).get(
        new GenericType<GithubDTO>() {});

    return Response.ok(githubResponse).build();
  }
}
