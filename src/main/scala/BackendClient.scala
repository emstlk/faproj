import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.thrift.ThriftClientFramedCodec
import java.net.{InetAddress, InetSocketAddress}
import org.apache.thrift.protocol.TBinaryProtocol
import os.faproj.api.BackendService$FinagleClient
import com.twitter.conversions.time._

/**
 * $Id$
 * $URL$
 * User: emaster
 * Date: 18.10.13
 * Time: 12:15
 */
object BackendClient {

  def main(args: Array[String]): Unit = {

    val service = ClientBuilder()
      .hosts(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 8888))
      .codec(ThriftClientFramedCodec())
      .hostConnectionLimit(1)
      .tcpConnectTimeout(1.seconds)
      .build()

    val client = new BackendService$FinagleClient(service, new TBinaryProtocol.Factory())

    /*client.incrementCounter() onSuccess {
      response => println("Counter: " + response)
    } onFailure {
      e => e.printStackTrace()
    } ensure {
      service.close()
    }*/
  }

}
