package org

package object UrlWallet {

  trait CoinUtil {
    val browseURL:String
    val decimals:Int // 8 for BTC, 18 for ETH
    val minFee:Long
    val symbol:String // BTC/LTC/BTC, etc
    val name:String // Bitcoin etc
    val minValue:Long
    def getOutputBox(address:String, amount:BigInt, inputBoxes:Seq[InputBox], token:BigInt, tokenId:String, copyRegs:Option[InputBox]):OutputBox
    def isAddressValid(address:String):Boolean
    def getPrivateKey(walletImportFormat:String):CoinPrivateKey
    def getKeyFromInt(bigInt: BigInt):CoinPrivateKey

    /* coinKeys must match the inputs in coinTx (both size of array and sequence of elements) */
    def sendTx(src:Array[InputBox], dest:Array[OutputBox], txFee:Long, changeAddress:String, allowTokenBurn:Boolean, coinKeys:Array[CoinPrivateKey]):CoinSignedTx
  }

  trait OutputBox

  trait CoinReader {
    type IsConfirmed = Boolean // Boolean tells if any are awaiting confirmations. Need this for some wallets (including Ergo) whose explorer doesn't yet support unconfirmed boxes
    def getUnspentBoxes(address:String):(Array[InputBox], IsConfirmed)
    def getBalance(address:String): CoinBalance = {
      val (unspentBoxes, isConfirmed) = getUnspentBoxes(address)
      val value = unspentBoxes.map(_.amount).sum
      val tokens = unspentBoxes.flatMap(_.tokens).groupBy(_.id).map{
        case (id, array) => array.reduceLeft(_ + _)
      }.toArray
      CoinBalance(value, tokens, isConfirmed)
    }
    def getBoxById(boxId:String):InputBox
  }

  trait CoinWriter {
    def pushTx(hex:String):Unit
  }

  trait CoinSignedTx {
    def hash:String
    def getEncoded:String
    override def toString = getEncoded
  }

  trait CoinPrivateKey {
    def getAddress:String
    def getPrivateKeyWIF:String
    def sign(message:String):String
  }
}
