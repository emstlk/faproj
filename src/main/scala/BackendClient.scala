import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.thrift.ThriftClientFramedCodec
import java.net.InetSocketAddress
import org.apache.thrift.protocol.TBinaryProtocol
import os.faproj.api.{BackendService$FinagleClient, BackendService}

/**
 * $Id$
 * $URL$
 * User: emaster
 * Date: 18.10.13
 * Time: 12:15
 */
object BackendClient {

  def main(args: Array[String]) {
    val service = ClientBuilder()
      .hosts(new InetSocketAddress(8888))
      .codec(ThriftClientFramedCodec())
      .hostConnectionLimit(1)
      .build()

    val client = new BackendService$FinagleClient(service, new TBinaryProtocol.Factory())

    client.incrementCounter() onSuccess {
      response => println("Counter: " + response)
    } ensure {
      service.close()
    }
  }

}
