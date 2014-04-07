import java.sql.Timestamp
import org.squeryl.annotations.Column
import org.squeryl.dsl.CompositeKey2
import org.squeryl.{KeyedEntity, Schema}
import org.squeryl.PrimitiveTypeMode._
import scala.compat.Platform._

case class User(id: Int = 0,
                @Column(name = "fb_id", length = 30)
                fbId: String,
                created: Timestamp = new Timestamp(currentTime)) extends KeyedEntity[Int]

case class Config(id: Int = 0,
                  json: String) extends KeyedEntity[Int]

case class Creature(@Column(name = "creature_id")
                    creatureId: Int = 0,
                    uid: Int = 0,
                    attack: Int,
                    defence: Int,
                    accuracy: Int,
                    evasion: Int,
                    life: Int,
                    initiative: Int,
                    created: Timestamp = new Timestamp(currentTime),
                    updated: Timestamp = new Timestamp(currentTime)) extends KeyedEntity[CompositeKey2[Int, Int]] {

  def id = compositeKey(creatureId, uid)
}

case class Battle(id: Int = 0,
                  attacker: Int,
                  defender: Int,
                  result: Int, // BattleResult
                  created: Timestamp = new Timestamp(currentTime),
                  updated: Timestamp = new Timestamp(currentTime)) extends KeyedEntity[Int]

object Dao extends Schema {

  override def name = Some("public")

  val users = table[User]("users")
  on(users)(t => declare(
    t.id is(primaryKey, autoIncremented(users.name + "_id_seq")),
    t.fbId is(unique, indexed(users.name + "_fbid_idx"))
  ))

  val configs = table[Config]("configs")
  on(configs)(t => declare(
    t.id is(primaryKey, autoIncremented(configs.name + "_id_seq"))
  ))

  val creatures = table[Creature]("creatures")
  on(creatures)(t => declare(
    t.id is primaryKey
  ))
  val userToCreatures = oneToManyRelation(users, creatures).via((u, c) => u.id === c.uid)

  val battles = table[Battle]("battles")
  on(battles)(t => declare(
    t.id is(primaryKey, autoIncremented(battles.name + "_id_seq"))
  ))
  val userToBattlesAttacker = oneToManyRelation(users, battles).via((u, b) => u.id === b.attacker)
  val userToBattlesDefender = oneToManyRelation(users, battles).via((u, b) => u.id === b.defender)

}
