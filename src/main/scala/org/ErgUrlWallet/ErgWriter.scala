package org.ErgUrlWallet

import org.UrlWallet.{CoinWriter, Curl}

object ErgWriter extends CoinWriter {
  override def pushTx(jsonTx: String): Unit = {
    Curl.post("http://88.198.13.202:9052/transactions", jsonTx)
  }
}
