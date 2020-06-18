package org.UrlWallet

trait CoinToken{
  val id:String
  val value:BigInt
  def +(that:CoinToken):CoinToken
}
