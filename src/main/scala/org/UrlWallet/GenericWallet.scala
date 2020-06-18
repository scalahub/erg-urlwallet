
package org.UrlWallet

class GenericWallet(
  val coinUtil:CoinUtil,
  val coinReader: CoinReader,
  val coinWriter: CoinWriter
) {
  val formatUtil = new FormatUtil(coinUtil.decimals)
  val sendUtil = new SendUtil(coinReader, coinWriter, coinUtil)
  val urlWallet = new UrlWallet(sendUtil, coinUtil)
  val keyWallet = new KeyWallet(sendUtil, coinUtil)
}