package org.ErgUrlWallet.utils

import org.ergoplatform.appkit.{BlockchainContext, ErgoClient, NetworkType, RestApiErgoClient}

object Client {
  lazy val clients: Seq[Client] = Nodes.urls.map(url => new Client(s"http://$url"))
}
class Client(url: String) {
  private val restApiErgoClient: ErgoClient = RestApiErgoClient.create(url, NetworkType.MAINNET, "")
  def usingContext[T](f: BlockchainContext => T): T = {
    restApiErgoClient.execute { ctx =>
      f(ctx)
    }
  }
}
