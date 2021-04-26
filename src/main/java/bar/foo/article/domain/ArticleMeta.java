package bar.foo.article.domain;

import static com.faunadb.client.query.Language.Obj;
import static com.faunadb.client.query.Language.Value;


import com.faunadb.client.query.Expr;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ArticleMeta {
  public String slug;
  public int views;
  public int favourites;
  public int shares;
  public int comments;

  public ArticleMeta(String slug) {
    this.slug = slug;
  }

  public ArticleMeta() {
  }

  public Expr toExpr() {
    return Obj("data", Obj(
        "slug", Value(this.slug),
        "views", Value(this.views),
        "favourites", Value(this.favourites),
        "shares", Value(this.shares),
        "comments", Value(this.comments)
    ));
  }
}
