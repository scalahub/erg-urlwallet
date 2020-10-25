package org.UrlWallet

class SendUtil(
    coinReader: CoinReader,
    coinWriter: CoinWriter,
    coinUtil: CoinUtil
) {

  val formatUtil = new FormatUtil(coinUtil.decimals)

  private def getEncodedTxForPush(
      key: CoinPrivateKey,
      srcCoins: Array[InputBox],
      dest: Array[OutputBox],
      txFee: Long,
      changeAddress: String,
      allowTokenBurn: Boolean
  ) = {
    val s = coinUtil.sendTx(
      srcCoins,
      dest,
      txFee,
      changeAddress,
      allowTokenBurn,
      srcCoins.map(_ => key)
    )
    val hash = s.hash
    (s.getEncoded, hash)
  }

  protected def makeTxAndSend(
      key: CoinPrivateKey,
      srcCoins: Array[InputBox],
      dest: Array[OutputBox],
      txFee: Long,
      changeAddress: String,
      allowTokenBurn: Boolean
  ) = {
    val (hex, hash) = getEncodedTxForPush(
      key,
      srcCoins,
      dest,
      txFee,
      changeAddress,
      allowTokenBurn
    )
    coinWriter.pushTx(hex)
    hash
  }

  val minFeeDecimal = formatUtil.insertDecimalL(coinUtil.minFee)

  def sendCoinsWithKey(
      key: CoinPrivateKey,
      recipientAddressesAmounts: Array[(String, String, Long, String)],
      txFee: String,
      additionalBoxId: Option[String],
      copyRegistersTo: Option[Int],
      allowTokenBurn: Boolean,
      optimizeInputs: Boolean
  ) =
    try {
      val txFeeLong = txFee.toLong
      if (txFeeLong < coinUtil.minFee)
        throw new Exception(s"Minimum fee is $minFeeDecimal")
      val myAddress = key.getAddress

      val (inputs, isConfirmed): (Array[InputBox], Boolean) =
        coinReader.getUnspentBoxes(myAddress)
      if (inputs.isEmpty) throw new Exception("No inputs available")
      if (!isConfirmed)
        throw new Exception(
          "Unconfirmed transaction. Please wait for a few minutes"
        )

      val noInput: Option[InputBox] = None

      val recipientAddressesAmountsCopyFrom
          : Array[(String, String, Long, String, Option[InputBox])] =
        recipientAddressesAmounts.map {
          case (address, amount, tokenAmount, tokenId) =>
            (address, amount, tokenAmount, tokenId, noInput)
        }

      val (amounts, tokens) = recipientAddressesAmountsCopyFrom.map {
        case (_, amount, tokenAmount, tokenId, _) =>
          (BigInt(amount), (tokenId, BigInt(tokenAmount)))
      }.unzip

      val additionalInput: Option[InputBox] =
        additionalBoxId.map(boxId => coinReader.getBoxById(boxId))

      val copyBoxRegistersTo: Option[(InputBox, Int)] = for {
        outputIndex <- copyRegistersTo
        box <- additionalInput
      } yield (box, outputIndex)

      copyBoxRegistersTo.foreach {
        case (additionalInput, outputIndex)
            if outputIndex < recipientAddressesAmountsCopyFrom.size =>
          val (address, amount, token, tokenId, _) =
            recipientAddressesAmountsCopyFrom(outputIndex)
          if (address != additionalInput.address)
            throw new Exception(
              s"Address of output ${outputIndex} does not match additional input address"
            )
          recipientAddressesAmountsCopyFrom(outputIndex) =
            (address, amount, token, tokenId, Some(additionalInput))
        case (_, outputIndex) =>
          throw new Exception(
            s"No output at index $outputIndex for copying additional input"
          )
      }

      val selected =
        if (optimizeInputs)
          selectInputs(inputs, additionalInput, amounts.sum + txFeeLong, tokens)
        else inputs

      val allInputs: Array[InputBox] = selected ++ additionalInput

      val outputBoxes: Array[OutputBox] =
        recipientAddressesAmountsCopyFrom.map {
          case (address, amount, tokenAmount, tokenId, copyFrom) =>
            if (!coinUtil.isAddressValid(address))
              throw new Exception(s"Invalid address: $address")
            coinUtil.getOutputBox(
              address,
              amount.toLong,
              allInputs,
              tokenAmount,
              tokenId,
              copyFrom
            )
        }

      val txHash = makeTxAndSend(
        key,
        allInputs,
        outputBoxes,
        txFeeLong,
        myAddress,
        allowTokenBurn
      )
      (txHash, inputs, outputBoxes)
    } catch {
      case e: java.net.UnknownHostException =>
        throw new Exception("Coin writer unreachable")
      case e: Exception =>
        // e.printStackTrace
        val message =
          if (e.getMessage.startsWith("Cost of transaction"))
            "Please check \"Optimize Inputs\" and retry"
          else e.getMessage
        throw new Exception(message)
    }

  private def selectInputs(
      inputBoxes: Array[InputBox],
      additionalInput: Option[InputBox],
      amountTotal: BigInt,
      tokens: Seq[(String, BigInt)]
  ): Array[InputBox] = {
    // logic is as follows
    // 1. minimum number of inputs
    // 2. ergs from inputBoxes
    // 3. tokens from inputBoxes and additionalInput
    //
    //    val tokensNeeded: Map[String, BigInt] = tokens.filter(_._2 > 0).groupBy(_._1).map{
    //      case (tokenId, amounts) => tokenId -> amounts.map(_._2).sum
    //    }.map{
    //      case (tokenId, amount) if additionalInput.exists(box => box.tokens.exists(_.id == tokenId)) =>
    //        (tokenId, amount - additionalInput.get.tokens.find(_.id == tokenId).get.value)
    //      case (tokenId, amount) => (tokenId, amount)
    //    }.filter(_._2 > 0).toMap
    //    val boxesWithTokens = inputBoxes.filter(inputBox => inputBox.tokens.exists(token => tokensNeeded.contains(token.id)))
    //    val amountNeeded = amountTotal - additionalInput.map(_.amount).sum

    val sortedInputs = inputBoxes.sortBy(-_.amount)
    var accumAmount: BigInt = 0
    var accumBoxes: Array[InputBox] = Array()
    val accum = sortedInputs.map { input =>
      accumAmount += input.amount
      accumBoxes +:= input
      (accumAmount, accumBoxes)
    }
    accum
      .find {
        case (amount, _) => amount >= amountTotal
      }
      .map(_._2)
      .getOrElse(inputBoxes)
  }

}
