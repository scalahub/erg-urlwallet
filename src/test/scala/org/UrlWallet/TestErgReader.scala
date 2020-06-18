package org.UrlWallet

import org.ErgUrlWallet.{ErgReader, ErgUtil}

object TestErgReader extends App {
  val address = "9gMUzFpsjZeHFMgzwjc3TNecZ3WJ2uz2Wfqh4SkxJqMEQrTNitB"
  val (utxos, _) = ErgReader.getUnspentBoxes(address)
  utxos foreach (println)

  println(utxos.map(_.amount).sum)

}

