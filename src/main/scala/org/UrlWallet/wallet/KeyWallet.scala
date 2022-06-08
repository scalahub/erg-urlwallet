package org.UrlWallet.wallet

import org.UrlWallet.{CoinUtil, FormatUtil, SendUtil}

class KeyWallet(sendUtil: SendUtil, coinUtil: CoinUtil) {
  val formatUtil = new FormatUtil(coinUtil.decimals)
  def processSend(
      wifKey: String,
      addressesToSend: Array[String],
      amtsToSend: Array[String],
      tokensToSend: Array[Long],
      tokenIdsToSend: Array[String],
      fee: String,
      inputBoxId: String,
      copyTo: String,
      allowTokenBurn: Boolean,
      optimizeInputs: Boolean,
      inputBoxIndex: String
  ) = {
    if (addressesToSend.size != amtsToSend.size)
      throw new Exception("Addresses and amounts number mismatch")
    if (wifKey == null) throw new Exception("private key not found")
    val toSends: Array[(String, String, Long, String)] = {
      ((addressesToSend zip amtsToSend) zip (tokensToSend zip tokenIdsToSend)) map {
        case (("", _), (_, _)) =>
          throw new Exception("to address cannot be empty")
        case ((null, _), (_, _)) =>
          throw new Exception("to address cannot be empty")
        case ((_, null), (_, _)) =>
          throw new Exception("amount cannot be empty")
        case ((addr, amt), (token, tokenId)) =>
          if (!coinUtil.isAddressValid(addr))
            throw new Exception("invalid address: " + addr)
          val satoshis =
            try { formatUtil.removeDecimalS(amt) }
            catch {
              case any: Throwable =>
                throw new Exception("Invalid amount: " + amt)
            }
          (addr, satoshis, token, tokenId)
      }
    }

    val inputBoxIdOpt = Option(inputBoxId)

    val copyRegToOpt: Option[Int] = inputBoxIdOpt.flatMap { _ =>
      copyTo match {
        case "first"  => Some(0)
        case "second" => Some(1)
        case "no"     => None
        case any      => throw new Exception(s"Invalid option to copy registers")
      }
    }

    val inputBoxIndexOpt: Option[Int] = inputBoxIdOpt.flatMap { _ =>
      inputBoxIndex match {
        case "first"  => Some(0)
        case "second" => Some(1)
        case "last"   => None
        case _        => None
      }
    }

    val feeSatoshis =
      try { formatUtil.removeDecimalS(fee) }
      catch { case any: Throwable => throw new Exception("Invalid fee") }
    val key = coinUtil.getPrivateKey(wifKey)
    sendUtil
      .sendCoinsWithKey(
        key,
        toSends,
        feeSatoshis,
        inputBoxIdOpt,
        copyRegToOpt,
        allowTokenBurn,
        optimizeInputs,
        inputBoxIndexOpt
      )
      ._1
      .toString
  }

  def processSign(wifKey: String, message: String) =
    try { coinUtil.getPrivateKey(wifKey).sign(message) }
    catch {
      case any: Throwable =>
        throw new Exception("Error while signing")
    }

  def getRedirectUrl(urlPattern: String) = "/key" + urlPattern
  def getBigInt                          = new java.math.BigInteger(256, new java.security.SecureRandom)
  def getRandomPrivateKey                = coinUtil.getKeyFromInt(getBigInt).getPrivateKeyWIF
  def getAddress(wifKey: String)         = coinUtil.getPrivateKey(wifKey).getAddress
}
