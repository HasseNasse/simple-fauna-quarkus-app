package bar.foo.article;

import static org.assertj.core.api.Assertions.assertThat;


import bar.foo.article.Repository;
import io.quarkus.test.junit.QuarkusTest;
import java.util.concurrent.ExecutionException;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class RepositoryTest {

  @Inject
  Repository repository;

  @Test
  public void shouldFetchAllArticleComments() throws ExecutionException, InterruptedException {
    final var articleComments = repository.getArticleComments("test");
    assertThat(articleComments).isNotNull();
  }

}
