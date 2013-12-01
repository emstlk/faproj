import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.thrift.ThriftServerFramedCodec
import com.twitter.logging.Logger
import com.twitter.util.Future
import java.net.InetSocketAddress
import org.apache.thrift.protocol.TBinaryProtocol
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
    val userService = new UserService()

    val processor = new BackendService[Future] {
      def auth(uid: String) = Future(userService.auth(uid))

      def login(uid: String) = {
        if (Option(uid).isEmpty || uid.isEmpty) Future.value(false)
        else Future(userService.login(uid))
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
