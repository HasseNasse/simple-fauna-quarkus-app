package bar.foo.article.domain;

import static com.faunadb.client.query.Language.Arr;
import static com.faunadb.client.query.Language.Obj;
import static com.faunadb.client.query.Language.Value;


import com.faunadb.client.query.Expr;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.Arrays;
import java.util.stream.Collectors;

@RegisterForReflection
public class ArticleComments {
  public String slug;
  public Comment[] comments;

  public ArticleComments(String slug) {
    this.slug = slug;
  }

  public ArticleComments() {
  }

  public Expr toExpr() {
    final var commentsExpr = Arrays
        .asList(this.comments).stream()
        .map(comment -> comment.toExpr())
        .collect(Collectors.toList());

    return Obj("data", Obj(
        "slug", Value(this.slug),
        "comments", Arr(commentsExpr)
    ));
  }

}

