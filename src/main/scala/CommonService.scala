import com.twitter.logging.Logger
import java.sql.Timestamp
import java.util.Calendar
import org.squeryl.PrimitiveTypeMode._
import os.faproj.api.BattleResult
import os.faproj.api
import scala.compat.Platform.currentTime

/**
 * $Id$
 * $URL$
 * User: emaster
 * Date: 11.02.14
 * Time: 13:36
 */
class CommonService {

  val log = Logger()

  val MAX_LIFE_POINTS = 20

  private def now = new Timestamp(currentTime)

  def getCreature(creatureId: Int, uid: Int) = {
    inTransaction {
      Dao.creatures.where(_.id ===(creatureId, uid)).singleOption
    }
  }

  def saveCreature(uid: Int, c: api.Creature) = {
    inTransaction {
      getCreature(c.id, uid) match {
        case Some(_) =>
          update(Dao.creatures)(e =>
            where(e.id ===(c.id, uid))
              set(e.attack := c.attack,
              e.defence := c.defence,
              e.accuracy := c.accuracy,
              e.evasion := c.evasion,
              e.life := c.life,
              e.initiative := c.initiative,
              e.updated := now)
          )
          getCreature(c.id, uid).get

        case None =>
          Dao.creatures.insert(
            Creature(c.id, uid, c.attack, c.defence, c.accuracy, c.evasion, c.life, c.initiative)
          )
      }
    }
  }

  def beginBattle(attacker: Creature, defender: Creature) = {
    inTransaction {
      Dao.battles.insert(
        Battle(attacker = attacker.uid, defender = defender.uid, result = BattleResult.Unknown.value)
      ).id
    }
  }

  def endBattle(battleId: Int, attacker: Creature, defender: Creature, result: BattleResult) {
    inTransaction {
      update(Dao.battles)(b =>
        where(b.id === battleId)
          set(b.result := result.value,
          b.updated := now)
      )
    }
  }

  def getLifePoints(creatureId: Int, uid: Int) = {
    inTransaction {
      getCreature(creatureId, uid) match {
        case Some(c) =>
          val minutes = (currentTime - c.updated.getTime).toInt / 1000 / 60
          Math.min(c.life + minutes, MAX_LIFE_POINTS)

        case None =>
          MAX_LIFE_POINTS
      }
    }
  }

  def getCountBattlesForLastDay(user: User) = {
    inTransaction {
      val cal = Calendar.getInstance()
      cal.setTimeInMillis(now.getTime)
      cal.set(Calendar.MILLISECOND, 0)
      cal.set(Calendar.SECOND, 0)
      cal.set(Calendar.MINUTE, 0)
      cal.set(Calendar.HOUR_OF_DAY, 0)
      val startDay = new Timestamp(cal.getTimeInMillis)

      from(Dao.userToBattlesAttacker.rightTable)(
        b => where(b.created > startDay) compute count
      ).single.measures
    }
  }

}
