import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.http.Http
import com.twitter.finagle.Service
import com.twitter.logging.Logger
import com.twitter.util.Future
import java.net.InetSocketAddress
import java.util.concurrent.atomic.AtomicLong
import org.jboss.netty.buffer.ChannelBuffers
import org.jboss.netty.handler.codec.http._
import org.jboss.netty.util.CharsetUtil

/**
 * $Id$
 * $URL$
 * User: emaster
 * Date: 29.09.13
 * Time: 23:44
 */
object Backend extends App {

  val log = Logger()

  val counter = new AtomicLong()

  def nextId() = "" + counter.incrementAndGet()

  val service = new Service[HttpRequest, HttpResponse] {
    def apply(request: HttpRequest) = Future {
      val result = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)
      result.setContent(ChannelBuffers.copiedBuffer(nextId(), CharsetUtil.UTF_8))
      result
    }
  }

  val server = ServerBuilder()
    .codec(Http())
    .bindTo(new InetSocketAddress(8888))
    .name("TestServer")
    .build(service)

}
