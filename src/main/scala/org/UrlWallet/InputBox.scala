package org.UrlWallet

trait InputBox {
  val amount:BigInt
  val address:String
  val isSpent:Boolean
  val tokens:Seq[CoinToken]
  def getTokenStr(seprInt:String, seprExt:String) = {
    if (tokens.size == 0) "" else {
      tokens.map{
        case token =>
          token.value + seprInt+token.id
      }.reduceLeft(_ + seprExt +_ )
    }
  }
  private val shortLength = 20
  def getAddressShortened = address.take(shortLength) + (if (address.size > shortLength) "..." else "")
}
