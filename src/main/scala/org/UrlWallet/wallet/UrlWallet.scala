package org.UrlWallet

class UrlWallet(sendUtil: SendUtil, coinUtil: CoinUtil) {
  val formatUtil = new FormatUtil(coinUtil.decimals)

  def processSend(
      urlPattern: String,
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
      throw new Exception("Addresses and amounts size mismatch")
    if (addressesToSend.size != tokensToSend.size)
      throw new Exception("Addresses and tokens size mismatch")
    if (urlPattern == null) throw new Exception("url pattern not found")
    val toSends = {
      ((addressesToSend zip amtsToSend) zip (tokensToSend zip tokenIdsToSend)) map {
        case (("", _), (_, _)) =>
          throw new Exception("to address cannot be empty")
        case ((null, _), (_, _)) =>
          throw new Exception("to address cannot be empty")
        case ((_, null), (_, _)) =>
          throw new Exception("amount cannot be empty")
        case ((addr, amt), (token, tokenId)) =>
          if (!coinUtil.isAddressValid(addr))
            throw new Exception("Invalid address: " + addr)
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
    val key = getKeyFromUrl(urlPattern)
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

  def processSign(urlPattern: String, message: String) =
    try { getKeyFromUrl(urlPattern).sign(message) }
    catch { case _: Throwable => throw new Exception("error while signing") }

  private val seed =
    "5c96eeacece56a5800f0e41ea9f18e36171f6c96437bcc6820a9ac44ec41349f".getBytes
  private val sha256 = java.security.MessageDigest.getInstance("SHA-256");

  def getAddress(urlPattern: String) = getKeyFromUrl(urlPattern).getAddress

  def getRandomString: String = synchronized { Random.randString }

  private val maxLength = 50

  def getKeyFromUrl(urlPattern: String) =
    synchronized {
      val url =
        if (urlPattern.length > maxLength + 1)
          urlPattern.substring(0, maxLength + 1)
        else urlPattern
      val secret = sha256.digest(url.getBytes ++ seed)
      val bigint = new java.math.BigInteger(secret)
      coinUtil.getKeyFromInt(bigint.abs)
    }

  def getPrivateKey(urlPattern: String) =
    getKeyFromUrl(urlPattern).getPrivateKeyWIF
  def getRedirectUrl(urlPattern: String) = "/wallet" + urlPattern
}
