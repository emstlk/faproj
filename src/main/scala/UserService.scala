import com.mchange.v2.c3p0.ComboPooledDataSource
import com.twitter.logging.Logger
import org.squeryl.adapters.PostgreSqlAdapter
import org.squeryl.SessionFactory
import org.squeryl.PrimitiveTypeMode._

/**
 * $Id$
 * $URL$
 * User: emaster
 * Date: 28.11.13
 * Time: 1:45
 */
class UserService {

  val log = Logger()

  Class.forName("org.postgresql.Driver")

  val dataSource = new ComboPooledDataSource()
  dataSource.setDriverClass("org.postgresql.Driver")
  dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/faproj")
  dataSource.setUser("faproject")
  dataSource.setPassword("xxxxxxxxx")
  dataSource.setInitialPoolSize(1)
  dataSource.setMinPoolSize(1)
  dataSource.setMaxPoolSize(5)

  SessionFactory.concreteFactory = Some(() => org.squeryl.Session.create(
    dataSource.getConnection(), new PostgreSqlAdapter)
  )

  def getUid(fbUserId: String) = {
    try {
      inTransaction {
        Dao.users.where(_.fbId === fbUserId).single
      }
    } catch {
      case e: Exception =>
        log.error(e, "User " + fbUserId + " not found")
        throw e
    }
  }

  def login(fbUserId: String) = {
    try {
      inTransaction {
        val cntUsers: Long = from(Dao.users)(r =>
          where(r.fbId === fbUserId) compute count
        ).single.measures

        if (cntUsers == 0) {
          val u = Dao.users.insert(User(fbId = fbUserId))
          log.info("Created user " + u.fbId)
        }
      }
      true
    } catch {
      case e: Exception =>
        log.error(e, "Error login user " + fbUserId)
        false
    }
  }

  def auth(fbUserId: String): Boolean = {
    log.info("Auth user " + fbUserId)
    inTransaction {
      val cntUsers: Long = from(Dao.users)(r =>
        where(r.fbId === fbUserId) compute count
      ).single.measures

      cntUsers != 0
    }
  }

  def getLastConfig(version: Byte): Option[String] = {
    val config = inTransaction {
      Dao.configs.where(
        _.id in from(Dao.configs)(rs => compute(max(rs.id)))
      ).single
    }

    if (version == config.id) None
    else Option(config.json)
  }


}
