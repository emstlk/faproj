import com.twitter.logging.Logger
import com.twitter.querulous.database.ApachePoolingDatabaseFactory
import com.twitter.querulous.evaluator.StandardQueryEvaluatorFactory
import com.twitter.querulous.query.SqlQueryFactory

/**
 * $Id$
 * $URL$
 * User: emaster
 * Date: 28.11.13
 * Time: 1:45
 */
class UserService {

  import com.twitter.conversions.time._

  val log = Logger()

  Class.forName("org.postgresql.Driver")
  val apachePoolingDatabaseFactory =
    new ApachePoolingDatabaseFactory(2, 2, None, 1.seconds, 10.millis, false, 5.minutes, Map.empty)
  val queryEvaluatorFactory =
    new StandardQueryEvaluatorFactory(apachePoolingDatabaseFactory, new SqlQueryFactory)
  val queryEvaluator = queryEvaluatorFactory("localhost:5432", "xxx", "yyy", "zzz", Map.empty[String, String], "jdbc:postgresql")
  val schema = "public"

  def login(fbUserId: String) = {
    try {
      queryEvaluator.select(s"select count(*) from $schema.users where fb_id = ?", fbUserId) {
        rs => if (rs.getInt(1) == 0) {
          queryEvaluator.execute(s"insert into $schema.users(fb_id) values(?)", fbUserId)
        }
      }
      true
    } catch {
      case e: Exception =>
        log.error(e, "Error login user " + fbUserId)
        false
    }
  }

  def auth(fbUserId: String) {
    log.info("Auth user " + fbUserId)
  }

}
