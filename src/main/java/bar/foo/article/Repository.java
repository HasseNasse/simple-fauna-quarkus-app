package bar.foo.article;

import static com.faunadb.client.query.Language.Collection;
import static com.faunadb.client.query.Language.Create;
import static com.faunadb.client.query.Language.Exists;
import static com.faunadb.client.query.Language.Get;
import static com.faunadb.client.query.Language.If;
import static com.faunadb.client.query.Language.Index;
import static com.faunadb.client.query.Language.Let;
import static com.faunadb.client.query.Language.Match;
import static com.faunadb.client.query.Language.Replace;
import static com.faunadb.client.query.Language.Select;
import static com.faunadb.client.query.Language.Value;
import static com.faunadb.client.query.Language.Var;


import bar.foo.article.domain.ArticleComments;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.query.Expr;
import com.google.gson.Gson;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@RequestScoped
public class Repository {
  private static final String ARTICLE_COMMENTS_COLLECTION = "articleComments";
  private static final String ARTICLE_COMMENTS_INDEX = "articleCommentsIndex";
  private static final Gson gson = new Gson();

  @Inject
  Logger log;

  @ConfigProperty(name = "fauna.api.key")
  String apikey;

  FaunaClient client;

  @PostConstruct
  public void initConnection() {
    log.info("Initializing Fauna DB connection");
    client = FaunaClient.builder()
        .withSecret(apikey)
        .build();
  }

  public ArticleComments getArticleCommentsBySlug(String slug) throws ExecutionException, InterruptedException {
    log.info(String.format("Fetching article comments for slug: %s", slug));
    final var result = client.query(
        Select(
            Value("data"),
            Get(Match(Index(Value(ARTICLE_COMMENTS_INDEX)), Value(slug)))
        )
    ).thenApply(
        value -> gson.fromJson(value.toString(), ArticleComments.class)
    ).exceptionally(
        throwable -> null
    ).get();
    return result;
  }

  public ArticleComments getArticleComments(String slug) throws ExecutionException, InterruptedException {
    log.info(String.format("Fetching all article comments for article: %s ", slug));
    final var result = client.query(
        Select(
            Value("data"),
            Get(Match(Index(Value(ARTICLE_COMMENTS_INDEX)), Value(slug)))
        )
    ).thenApply(
        value -> gson.fromJson(value.toString(), ArticleComments.class)
    ).exceptionally(
        throwable -> null
    ).get();
    log.info(String.format("Fetched article comments for article: %s", slug));
    return result;
  }

  public void saveArticleComments(ArticleComments articleComments) throws ExecutionException, InterruptedException {
    log.info(String.format("Saving article comments for article: %s ", articleComments.slug));
    client.query(
        Let(
            "match", getSingleIdFromIndex(articleComments.slug, ARTICLE_COMMENTS_INDEX)
        ).in(
            If(
                Exists(Var("match")),
                Replace(Select(Value("ref"), Get(Var("match"))), articleComments.toExpr()),
                Create(Collection(ARTICLE_COMMENTS_COLLECTION), articleComments.toExpr())
            )
        )
    ).get();

    log.info(String.format("Successfully saved article comments for article: %s ", articleComments.slug));
  }

  private Expr getSingleIdFromIndex(String slug, String indexName) {
    return Match(
        Index(Value(indexName)), Value(slug)
    );
  }

}
