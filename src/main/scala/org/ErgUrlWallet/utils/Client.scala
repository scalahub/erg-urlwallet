package org.ErgUrlWallet.utils

import org.ergoplatform.appkit.{BlockchainContext, ErgoClient, NetworkType, RestApiErgoClient}

object Client {
  var optClient:Option[ErgoClient] = None

  def setClient(url:String):Int = {
    val client = RestApiErgoClient.create(url, NetworkType.MAINNET, "")
    optClient = Some(client)
    client.execute(ctx => ctx.getHeight)
  }

  def usingClient[T](f: BlockchainContext => T): T= {
    optClient.fold(throw new Exception("Set client first")){client =>
      client.execute{ctx =>
        f(ctx)
      }
    }
  }
}
