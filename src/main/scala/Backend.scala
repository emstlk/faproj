import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.thrift.ThriftServerFramedCodec
import com.twitter.logging.Logger
import com.twitter.util.Future
import java.net.InetSocketAddress
import java.util.concurrent.atomic.AtomicLong
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

  val counter = new AtomicLong()
  var message = ""

  def main(args: Array[String]) {
    val processor = new BackendService[Future] {
      def getCounter(): Future[Long] = Future(counter.get())

      def getString(): Future[String] = Future.value(message)

      def incrementCounter(): Future[Long] = Future(counter.incrementAndGet())

      def saveString(msg: String): Future[Unit] = Future {
        message = msg
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
