package org.UrlWallet

class SendUtil(coinReader:CoinReader, coinWriter:CoinWriter, coinUtil:CoinUtil) {

  val formatUtil = new FormatUtil(coinUtil.decimals)

  private def getEncodedTxForPush(key:CoinPrivateKey, srcCoins:Array[InputBox], dest:Array[OutputBox], txFee:Long, changeAddress:String, allowTokenBurn:Boolean) = {
    val s = coinUtil.sendTx(srcCoins, dest, txFee, changeAddress, allowTokenBurn, srcCoins.map(_ => key))
    val hash = s.hash
    (s.getEncoded, hash)
  }

  protected def makeTxAndSend(key:CoinPrivateKey, srcCoins:Array[InputBox], dest:Array[OutputBox], txFee:Long, changeAddress:String, allowTokenBurn:Boolean) = {
    val (hex, hash) = getEncodedTxForPush(key, srcCoins, dest, txFee, changeAddress, allowTokenBurn)
    coinWriter.pushTx(hex)
    hash
  }

  val minFeeDecimal = formatUtil.insertDecimalL(coinUtil.minFee)

  def sendCoinsWithKey(key:CoinPrivateKey, recipientAddressesAmounts:Array[(String, String, Long, String)], txFee:String, additionalBoxId:Option[String], copyRegistersTo:Option[Int], allowTokenBurn:Boolean) = try {
    val txFeeLong = txFee.toLong
    if (txFeeLong < coinUtil.minFee) throw new Exception(s"Minimum fee is $minFeeDecimal")
    val myAddress = key.getAddress

    val (inputs, isConfirmed): (Array[InputBox], Boolean) = coinReader.getUnspentBoxes(myAddress)
    if (inputs.isEmpty) throw new Exception("No inputs available")
    if (!isConfirmed) throw new Exception("Unconfirmed transaction. Please wait for a few minutes")

    val additionalInput: Option[InputBox] = additionalBoxId.map(boxId => coinReader.getBoxById(boxId))
    val allInputs = inputs ++ additionalInput

    val copyBoxRegistersTo: Option[(InputBox, Int)] = for {
      i <- copyRegistersTo
      box <- additionalInput
    } yield (box, i)

    val noInput:Option[InputBox] = None

    val recipientAddressesAmountsCopyFrom: Array[(String, String, Long, String, Option[InputBox])] = recipientAddressesAmounts.map{
      case (address, amount, token, tokenId) => (address, amount, token, tokenId, noInput)
    }

    copyBoxRegistersTo.foreach{
      case (inputBox, i) if i < recipientAddressesAmountsCopyFrom.size =>
        val (address, amount, token, tokenId, _) = recipientAddressesAmountsCopyFrom(i)
        if (address != inputBox.address) throw new Exception(s"Address of output ${i} does not match input address")
        recipientAddressesAmountsCopyFrom(i) = (address, amount, token, tokenId, Some(inputBox))
      case (_, i) => throw new Exception(s"No output at index ${i} for copying input")
    }

    val outputBoxes: Array[OutputBox] = recipientAddressesAmountsCopyFrom.map{
      case (address, amount, token, tokenId, copyFrom) =>
        if (!coinUtil.isAddressValid(address)) throw new Exception(s"Invalid address: $address")
        coinUtil.getOutputBox(address, amount.toLong, allInputs, token, tokenId, copyFrom)
    }

    val txHash = makeTxAndSend(key, allInputs, outputBoxes, txFeeLong, myAddress, allowTokenBurn)
    (txHash, inputs, outputBoxes)
  } catch { 
      case e:java.net.UnknownHostException => throw new Exception("Coin writer unreachable")
  }
}