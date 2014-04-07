import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.thrift.ThriftServerFramedCodec
import com.twitter.logging.Logger
import com.twitter.util.Future
import java.net.InetSocketAddress
import org.apache.thrift.protocol.TBinaryProtocol
import os.faproj.api
import os.faproj.api.{BackendService$FinagleService, BackendService}

/**
 * $Id$
 * $URL$
 * User: emaster
 * Date: 29.09.13
 * Time: 23:44
 */
object Backend {

  val log = Logger()

  def main(args: Array[String]) {
    val userService = new UserService
    val commonService = new CommonService

    val processor = new BackendService[Future] {
      def auth(uid: String) = Future(userService.auth(uid))

      def login(uid: String) = {
        if (Option(uid).isEmpty || uid.isEmpty) Future.value(false)
        else Future(userService.login(uid))
      }

      def getLastConfig(version: Byte) = Future(userService.getLastConfig(version).getOrElse(""))

      def beginBattle(b: api.Battle) = Future {
        val attacker = commonService.saveCreature(
          userService.getUid(b.attacker.uid).id,
          b.attacker
        )

        val defender = commonService.saveCreature(
          userService.getUid(b.defender.uid).id,
          b.defender
        )

        commonService.beginBattle(attacker, defender)
      }

      def endBattle(b: api.Battle) = Future {
        require(b.battleId.isDefined, "battleId is empty")

        val attacker = commonService.saveCreature(
          userService.getUid(b.attacker.uid).id,
          b.attacker
        )

        val defender = commonService.saveCreature(
          userService.getUid(b.defender.uid).id,
          b.defender
        )

        b.battleId.map {
          commonService.endBattle(_, attacker, defender, b.result)
        }
      }

      def getLifePoints(uid: String, creatureId: Int) = Future {
        commonService.getLifePoints(creatureId, userService.getUid(uid).id)
      }

      def getCountBattlesForLastDay(uid: String) = Future {
        commonService.getCountBattlesForLastDay(userService.getUid(uid)).toInt
      }
    }

    val service = new BackendService$FinagleService(processor, new TBinaryProtocol.Factory())

    ServerBuilder()
      .name("FaServer")
      .bindTo(new InetSocketAddress(8888))
      .codec(ThriftServerFramedCodec())
      .build(service)
  }

}
