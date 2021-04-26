package bar.foo.article;

import static org.assertj.core.api.Assertions.assertThat;


import bar.foo.article.Repository;
import bar.foo.article.Service;
import bar.foo.article.domain.Comment;
import io.quarkus.test.junit.QuarkusTest;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;


@QuarkusTest
public class ServiceTest {

  @Inject
  Repository repository;

  @Inject
  Service service;

  @Test
  public void shouldAddComment() throws ExecutionException, InterruptedException {
    final var comment = new Comment();
    comment.name = "John Doe";
    comment.comment = "Sample comment";
    comment.email = "john.doe@test.co";
    comment.createdAt = "2010-06-15T00:00:12";

    service.handleComment("test", comment);
    final var testArticleComments = repository.getArticleCommentsBySlug("test");
    assertThat(testArticleComments.comments[testArticleComments.comments.length - 1]).isEqualToComparingFieldByField(comment);
  }


  @Test
  public void shouldCreateANewArticleMetaAndAddComment() throws ExecutionException, InterruptedException {
    // Given
    final var identifier = UUID.randomUUID();
    final var slug = String.format("test_slug_%s", identifier);
    final var testComment = new Comment();
    testComment.name = "John Doe";
    testComment.comment = "Sample comment";
    testComment.email = "john.doe@test.co";
    testComment.createdAt = "2010-06-15T00:00:12";

    // When
    service.handleComment(slug, testComment);
    final var testArticleComments = repository.getArticleComments(slug);

    // Then
    assertThat(testArticleComments.comments[0]).isEqualToComparingFieldByField(testComment);
  }
}
