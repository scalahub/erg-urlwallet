package org.UrlWallet

case class CoinBalance(value:BigInt, tokens:Array[CoinToken], isConfirmed:Boolean) {
  val tokensString: String = {
    tokens.map{token =>
      token.id+":"+token.value
    }.foldLeft(""){
      case (str1, str2) => str1 + " " + str2
    }
  }.trim
}
