package org.ErgUrlWallet

import org.ErgUrlWallet.utils.Client
import org.UrlWallet._
import org.apache.commons.codec.binary.Hex
import org.ergoplatform.appkit.impl.ErgoTreeContract
import org.ergoplatform.appkit.{InputBox => AppkitInputBox, _}
import org.ergoplatform.{ErgoAddress, ErgoAddressEncoder, appkit}

import scala.{BigInt => ScalaBigInt}
import scala.util.Try

object ErgUtil extends CoinUtil {
  lazy val decimals: Int = 9

  lazy val formatUtil = new FormatUtil(decimals)

  lazy val minFee: Long = 1000000L

  lazy val symbol: String = "ERG"

  lazy val name: String = "Ergo"

  lazy val minAmt: Long = 1000000L

  lazy val defaultAmt: Long = 1000000L

  val browseURL = "https://explorer.ergoplatform.com/en/addresses/"

  def getAddressFromString(address: String): Try[ErgoAddress] = {
    Client.clients.head.usingContext { ctx =>
      val addressEncoder =
        new ErgoAddressEncoder(ctx.getNetworkType.networkPrefix)
      addressEncoder.fromString(address)
    }
  }

  override def isAddressValid(address: String): Boolean = {
    Client.clients.head.usingContext { ctx =>
      val addressEncoder =
        new ErgoAddressEncoder(ctx.getNetworkType.networkPrefix)
      try {
        addressEncoder.fromString(address).get.script
        true
      } catch {
        case e: Throwable =>
          e.printStackTrace
          false
      }
    }
  }

  override def getOutputBox(
      address: String,
      amount: BigInt,
      inputBoxes: Seq[InputBox],
      token: BigInt,
      tokenId: String,
      copyRegsFromInput: Option[InputBox]
  ): ErgOutputBox = {

    val registers: Seq[ErgoValue[_]] = copyRegsFromInput
      .map {
        case e: ErgInputBox => e.getRegistersAsSeq
        case any =>
          throw new Exception(
            s"Unknown InputBox type ${any.getClass.getCanonicalName}"
          )
      }
      .getOrElse(Nil)

    val tokens: Seq[ErgToken] = if (token > 0) {
      def ergInputBox: ErgInputBox = inputBoxes(0).asInstanceOf[ErgInputBox]
      val actualTokenId            = if (tokenId == "new") ergInputBox.id else tokenId
      Seq(ErgToken(actualTokenId, token))
    } else Nil
    ErgOutputBox(address, registers, amount, tokens)
  }

  override def getPrivateKey(walletImportFormat: String): ErgPrivateKey =
    ErgPrivateKey(BigInt(Hex.decodeHex(walletImportFormat.toCharArray)))

  override def getKeyFromInt(bigInt: ScalaBigInt): ErgPrivateKey =
    ErgPrivateKey(bigInt)

  override def sendTx(
      inputBoxes: Array[InputBox],
      outputBoxes: Array[OutputBox],
      txFee: Long,
      changeAddress: String,
      allowTokenBurn: Boolean,
      coinKeys: Array[CoinPrivateKey]
  ): CoinSignedTx = {
    if (inputBoxes.isEmpty) throw new Exception("No funds available")

    val idx = scala.util.Random.nextInt(Client.clients.length)
    Client.clients(idx).usingContext { implicit ctx =>
      val txB = ctx.newTxBuilder()
      val addressEncoder =
        new ErgoAddressEncoder(ctx.getNetworkType.networkPrefix)

      def getOutBoxFromErgOutBox(ergOutputBox: ErgOutputBox): OutBox = {
        val ErgOutputBox(address, regs, value, tokens) = ergOutputBox
        val contract =
          new ErgoTreeContract(addressEncoder.fromString(address).get.script)
        val outBoxBuilder = outBoxBuilderWithTokens(
          txB.outBoxBuilder().value(value.toLong).contract(contract)
        )(tokens)
        (if (regs.isEmpty) outBoxBuilder else outBoxBuilder.registers(regs: _*))
          .build()
      }

      val ergOutputBoxes: Array[ErgOutputBox] = outputBoxes.map {
        case ergOutputBox: ErgOutputBox => ergOutputBox
        case any =>
          throw new Exception(
            s"Unsupported output box type: ${any.getClass.getCanonicalName}"
          )
      }

      val outBoxes: Array[OutBox] = ergOutputBoxes.map(getOutBoxFromErgOutBox)

      val ergInputBoxes: Array[ErgInputBox] = inputBoxes.map {
        case ergoInputBox: ErgInputBox => ergoInputBox
        case any =>
          throw new Exception(
            s"Unsupported input box type: ${any.getClass.getCanonicalName}"
          )
      }

      val totalIn  = ergInputBoxes.map(_.amount).sum
      val totalOut = outBoxes.map(_.getValue).sum + txFee

      val change = totalIn - totalOut
      if (change < 0)
        throw new Exception(s"Insufficient funds. Short by ${formatUtil
          .formatLongNumber(change.abs.toLong)} $symbol")

      // token validation
      val newTokenId = ergInputBoxes(0).id
      val outTokens: Seq[ErgToken] = ergOutputBoxes
        .flatMap(_.tokens)
        .filterNot(_.id == newTokenId)
        .groupBy(_.id)
        .map {
          case (id, array) => ErgToken(id, array.map(_.value).sum)
        }
        .toSeq

      val inTokens: Seq[ErgToken] = ergInputBoxes
        .flatMap(_.tokens)
        .groupBy(_.id)
        .map {
          case (id, array) => ErgToken(id, array.map(_.value).sum)
        }
        .toSeq

      // first require that outTokens is a subset of inTokens
      outTokens.foreach { outToken =>
        inTokens.find(_.id == outToken.id) match {
          case Some(inToken) if inToken.value >= outToken.value => // ok
          case Some(_) =>
            throw new Exception(s"Insufficient tokens with id ${outToken.id}")
          case _ => throw new Exception(s"No tokens with id ${outToken.id}")
        }
      }

      // then put balance tokens in change
      val changeTokens: Seq[ErgToken] = inTokens
        .map { inToken =>
          val balance: BigInt = outTokens
            .find(_.id == inToken.id)
            .map { outToken =>
              inToken.value - outToken.value
            }
            .getOrElse(inToken.value)
          ErgToken(inToken.id, balance)
        }
        .filter(_.value > 0)

      val (actualChange, actualTxFee): (BigInt, Long) =
        if (change < minFee) (0, txFee + change.toLong) else (change, txFee)

      if (!allowTokenBurn && changeTokens.nonEmpty && actualChange == 0)
        throw new Exception("This transaction causes token burn")

      val toBurn: Seq[ErgoToken] =
        if (allowTokenBurn && changeTokens.nonEmpty && actualChange == 0)
          changeTokens.map(changeToken => new ErgoToken(changeToken.id, changeToken.value.toLong))
        else Nil

      val (optChangeOutBox, optChangeErgOutputBox): (
          Option[OutBox],
          Option[ErgOutputBox]
      ) = if (actualChange > 0) {
        val ergOutBox = ErgOutputBox(changeAddress, Nil, change, changeTokens)
        (Some(getOutBoxFromErgOutBox(ergOutBox)), Some(ergOutBox))
      } else (None, None)

      val appkitInputBoxes: Array[appkit.InputBox] =
        ctx.getBoxesById(ergInputBoxes.map(_.id): _*)

      val inputs = new java.util.ArrayList[appkit.InputBox]()

      appkitInputBoxes.foreach(inputs.add)
      val allOutBoxes: Array[OutBox] = outBoxes ++ optChangeOutBox

      /// changeAddress

      val txBuilder: UnsignedTransactionBuilder =
        ctx
          .newTxBuilder()
          .boxesToSpend(inputs)
          .outputs(allOutBoxes: _*)
          .fee(actualTxFee)
          .sendChangeTo(getAddressFromString(changeAddress).get)
          .tokensToBurn(toBurn: _*)
      val txToSign: UnsignedTransaction = txBuilder.build()

      val proverBuilder = ctx.newProverBuilder()
      coinKeys.foreach {
        case ErgPrivateKey(bigInt) =>
          proverBuilder.withDLogSecret(bigInt.bigInteger)
        case any =>
          throw new Exception(
            s"Unsupported private key type: ${any.getClass()}"
          )
      }
      val prover                      = proverBuilder.build()
      val signedTx: SignedTransaction = prover.sign(txToSign)

      ctx.sendTransaction(signedTx)
      println("pushingTx " + signedTx.getId)

      val ergoTransactionOutputs: java.util.List[org.ergoplatform.appkit.InputBox] =
        signedTx.getOutputsToSpend

      val x: Option[(ErgOutputBox, AppkitInputBox)] =
        optChangeErgOutputBox.map { changeErgOutputBox =>
          val changeErgoTransactionOutput: org.ergoplatform.appkit.InputBox =
            ergoTransactionOutputs.get(ergOutputBoxes.length)
          (changeErgOutputBox, changeErgoTransactionOutput)
        }
      SentCache.addTx(
        changeAddress,
        ergInputBoxes.map(_.id),
        ctx.getHeight,
        x
      )
      ErgSignedTx(signedTx)

    }
  }

  def outBoxBuilderWithTokens(
      outBoxBuilder: OutBoxBuilder
  )(tokens: Seq[ErgToken]) = {
    if (tokens.isEmpty) outBoxBuilder
    else {
      outBoxBuilder.tokens(
        tokens.map(token => token.toErgoToken): _*
      )
    }
  }

}
