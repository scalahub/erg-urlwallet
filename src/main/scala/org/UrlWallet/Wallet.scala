package org.UrlWallet

import org.ErgUrlWallet.{ErgReader, ErgWallet}

object Wallet extends ErgWallet {
  def isValidInput(inputBoxId:String) = ErgReader.getBoxById(inputBoxId).spendingTxId.isEmpty
}
