package bar.foo.article;

import bar.foo.article.domain.Comment;
import java.util.concurrent.ExecutionException;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/articles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Resource {

  @Inject
  Repository repository;

  @Inject
  Service service;

  @GET
  @Path("{slug}/comments")
  public Response getComment(@PathParam("slug") String slug) throws ExecutionException, InterruptedException {
    final var articleComments = repository.getArticleComments(slug);
    return Response.ok(articleComments).build();
  }

  @POST
  @Path("{slug}/comments")
  public Response addComment(@PathParam("slug") String slug, @Valid Comment comment) throws ExecutionException, InterruptedException {
    service.handleComment(slug, comment);
    return Response.ok().build();
  }

}
