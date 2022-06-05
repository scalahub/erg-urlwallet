package org.UrlWallet

case class CoinBalance(value: BigInt, tokens: Array[CoinToken], isConfirmed: Boolean, optUSD: Option[String] = None) {
  val tokensString: String = {
    val optUsdValue = if (optUSD.isDefined) s"USD:${optUSD.get} " else ""
    tokens
      .map { token =>
        token.id + ":" + token.value
      }
      .foldLeft(optUsdValue) {
        case (str1, str2) => str1 + " " + str2
      }
  }.trim
}
