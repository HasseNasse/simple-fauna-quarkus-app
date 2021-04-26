package bar.foo.article.domain;

import static com.faunadb.client.query.Language.Obj;
import static com.faunadb.client.query.Language.Value;


import com.faunadb.client.query.Expr;
import io.quarkus.runtime.annotations.RegisterForReflection;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@RegisterForReflection
public class Comment {
  @NotEmpty
  public String comment;
  @NotEmpty
  public String name;
  @Email
  public String email;
  @Pattern(regexp = "\\d{4}-[01]\\d-[0-3]\\dT[0-2]\\d:[0-5]\\d:[0-5]\\d([+-][0-2]\\d:[0-5]\\d|)")
  public String createdAt;

  public Comment() {
  }

  public Expr toExpr() {
    return Obj(
        "comment", Value(this.comment),
        "name", Value(this.name),
        "email", Value(this.email),
        "createdAt", this.createdAt != null
            ? Value(this.createdAt)
            : Value("0000-00-00T00:00:00")
    );
  }

}
