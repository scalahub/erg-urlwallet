package org.ErgUrlWallet

import org.ErgUrlWallet.utils.Nodes
import org.UrlWallet.{CoinWriter, Curl}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ErgWriter extends CoinWriter {
  override def pushTx(jsonTx: String): Unit = {
    Nodes.urls.foreach { url =>
      Future(Curl.post(s"http://$url/transactions", jsonTx))
    }
  }
}
