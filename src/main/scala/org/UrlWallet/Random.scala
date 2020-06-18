package org.UrlWallet

import java.math.BigInteger

object Random {
  val secureRandom = new java.security.SecureRandom
  def randBigInt:BigInt = new BigInteger(256, secureRandom)

  val alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

  val len = alphabet.length

  def randString: String = {
    (1 to 50).map {i =>
      alphabet(secureRandom.nextInt(len))
    }.mkString
  }
}

