import java.sql.Timestamp
import org.squeryl.annotations.Column
import org.squeryl.{KeyedEntity, Schema}
import org.squeryl.PrimitiveTypeMode._
import scala.compat.Platform._

case class User(id: Int = 0,
                @Column(name = "fb_id", length = 30)
                fbId: String,
                created: Timestamp = new Timestamp(currentTime)
                 ) extends KeyedEntity[Int]

case class Config(id: Int = 0, json: String) extends KeyedEntity[Int]

class Dao(schemaName: String) extends Schema {

  override def name = Some(schemaName)

  val users = table[User]("users")

  on(users)(t => declare(
    t.id is(primaryKey, autoIncremented(users.name + "_id_seq")),
    t.fbId is(unique, indexed(users.name + "_fbid_idx"))
  ))

  val configs = table[Config]("configs")

  on(configs)(t => declare(
    t.id is(primaryKey, autoIncremented(configs.name + "_id_seq"))
  ))
}
