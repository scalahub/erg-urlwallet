package org.ErgUrlWallet.utils

import org.ergoplatform.appkit.{BlockchainContext, ErgoClient, NetworkType, RestApiErgoClient}

object Client {
  lazy val clients: Seq[Client] = Nodes.urls.map(url => new Client(s"http://$url"))
  def usingContext[T](f: BlockchainContext => T): T = {
    // ToDo: instead of head, try one by one till success
    clients.head.usingContext(f)
  }
}
class Client(url: String) {
  private val restApiErgoClient: ErgoClient = RestApiErgoClient.create(url, NetworkType.MAINNET, "")
  private def usingContext[T](f: BlockchainContext => T): T = {
    restApiErgoClient.execute { ctx =>
      f(ctx)
    }
  }
}
