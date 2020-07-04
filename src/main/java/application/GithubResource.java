package application;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/github")
public class GithubResource {

  @Inject
  private GithubService service;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getGithubData() {

    GithubDTO githubResponse = this.service.getApiData();

    return Response.ok(githubResponse).build();
  }
}
