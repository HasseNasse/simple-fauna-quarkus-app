package bar.foo.article;

import bar.foo.article.domain.ArticleComments;
import bar.foo.article.domain.Comment;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class Service {

  @Inject
  Repository repository;


  public void handleComment(String slug, Comment comment) throws ExecutionException, InterruptedException {
    final var articleCommentsBySlug = getOrCreateArticleCommentsBySlug(slug);

    final var currentArticleComments = articleCommentsBySlug.comments != null
        ? Arrays.copyOf(articleCommentsBySlug.comments, articleCommentsBySlug.comments.length + 1)
        : new Comment[1];
    currentArticleComments[currentArticleComments.length - 1] = comment;
    articleCommentsBySlug.comments = currentArticleComments;

    // Persist comment for the article
    repository.saveArticleComments(articleCommentsBySlug);
  }

  private ArticleComments getOrCreateArticleCommentsBySlug(String slug) throws ExecutionException, InterruptedException {
    var articleComments = Optional
        .ofNullable(repository.getArticleCommentsBySlug(slug))
        .orElse(new ArticleComments(slug));
    return articleComments;
  }

}
