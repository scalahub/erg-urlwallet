package org.ErgUrlWallet

import jde.parser.Parser
import jde.compiler.{Compiler => JCompiler}
import kiosk.ergo.KioskLong
import kiosk.explorer.Explorer
import org.UrlWallet.{CoinReader, Curl, FormatUtil, InputBox}

import scala.util.Try

object ErgReader extends CoinReader {

  import io.circe.Json

  // private val baseUrl    = "https://new-explorer.ergoplatform.com"
  // private val unspentUrl = s"$baseUrl/transactions/boxes/byAddress/unspent/"

  private val baseUrl    = "https://api.ergoplatform.com"
  private val unspentUrl = s"$baseUrl/api/v0/transactions/boxes/byAddress/unspent/"
  private val boxUrl     = s"$baseUrl/transactions/boxes/"

  override def getBoxById(boxId: String): ErgInputBox = {
    getUtxoBoxFromJson(Curl.get(boxUrl + boxId))
  }

  private def getUtxoBoxFromJson(j: Json) = {
    val id    = getId(j)
    val value = (j \\ "value").map(v => v.asNumber.get).apply(0)
    val creationHeight =
      (j \\ "creationHeight").map(v => v.asNumber.get).apply(0)
    val assets: Seq[Json] = (j \\ "assets").map(v => v.asArray.get).apply(0)
    val tokens: Seq[ErgToken] = assets.map { asset =>
      val tokenID = (asset \\ "tokenId").map(v => v.asString.get).apply(0)
      val value =
        (asset \\ "amount").map(v => v.asNumber.get).apply(0).toLong.get
      ErgToken(tokenID, value)
    }
    val registers = (j \\ "additionalRegisters").flatMap { r =>
      r.asObject
        .map {
          _.toList.map {
            case (key, value) => key -> value.asString.get
          }
        }
        .getOrElse(Nil)
    }.toMap

    val address      = (j \\ "address").map(v => v.asString.get).apply(0)
    val spendingTxId = (j \\ "spentTransactionId").map(v => v.asString).apply(0)
    ErgInputBox(
      id,
      value.toLong.get,
      registers,
      tokens,
      creationHeight.toInt.get,
      address,
      spendingTxId
    )
  }

  private def getId(j: Json) = (j \\ "id").map(v => v.asString.get).apply(0)

  private def getUtxoBoxesFromJson(json: Json): Seq[ErgInputBox] = {
    json.asArray.get.map(getUtxoBoxFromJson)
  }

  override def getUnspentBoxes(
      address: String
  ): (Array[InputBox], ErgReader.IsConfirmed) = {
    val spentBoxIdsFromMemory: Seq[String] = SentCache.getSpentBoxIds(address)
    val receivedBoxesFromMemory: Set[ErgInputBox] = SentCache
      .getReceivedBoxes(address)
      .toSet // don't count received boxes for now

    val receivedBoxesFromExplorer: Set[ErgInputBox] = getUtxoBoxesFromJson(
      Curl.get(unspentUrl + address)
    ).toSet

    val explorerIds: Set[String] = receivedBoxesFromExplorer.map(_.id)
    val memoryIds: Set[String]   = receivedBoxesFromMemory.map(_.id)

    val allReceivedIds: Set[String] = explorerIds ++ memoryIds

    val unspentIds: Set[String] =
      allReceivedIds.filterNot(spentBoxIdsFromMemory.contains)

    val allReceivedBoxes: Set[ErgInputBox] =
      receivedBoxesFromExplorer ++ receivedBoxesFromMemory

    val unspentBoxes: Set[ErgInputBox] = unspentIds.map { unspentId =>
      allReceivedBoxes.find(_.id == unspentId).get
    }

    val unspentInputBoxes: Array[InputBox] =
      unspentBoxes.map(_.asInstanceOf[InputBox]).toArray

    val isConfirmed = unspentIds.forall(explorerIds.contains)

    (unspentInputBoxes, isConfirmed)
  }

  private val oracleRateScript =
    """{
      |  "constants": [
      |    {
      |      "name": "oraclePoolNFT",
      |      "type": "CollByte",
      |      "value": "011d3364de07e5a26f0c4eef0852cddb387039a921b7154ef3cab22c6eda887f"
      |    },
      |    {
      |      "name": "poolAddresses",
      |      "type": "Address",
      |      "values": [
      |        "NTkuk55NdwCXkF1e2nCABxq7bHjtinX3wH13zYPZ6qYT71dCoZBe1gZkh9FAr7GeHo2EpFoibzpNQmoi89atUjKRrhZEYrTapdtXrWU4kq319oY7BEWmtmRU9cMohX69XMuxJjJP5hRM8WQLfFnffbjshhEP3ck9CKVEkFRw1JDYkqVke2JVqoMED5yxLVkScbBUiJJLWq9BSbE1JJmmreNVskmWNxWE6V7ksKPxFMoqh1SVePh3UWAaBgGQRZ7TWf4dTBF5KMVHmRXzmQqEu2Fz2yeSLy23sM3pfqa78VuvoFHnTFXYFFxn3DNttxwq3EU3Zv25SmgrWjLKiZjFcEcqGgH6DJ9FZ1DfucVtTXwyDJutY3ksUBaEStRxoUQyRu4EhDobixL3PUWRcxaRJ8JKA9b64ALErGepRHkAoVmS8DaE6VbroskyMuhkTo7LbrzhTyJbqKurEzoEfhYxus7bMpLTePgKcktgRRyB7MjVxjSpxWzZedvzbjzZaHLZLkWZESk1WtdM25My33wtVLNXiTvficEUbjA23sNd24pv1YQ72nY1aqUHa2",
      |        "EfS5abyDe4vKFrJ48K5HnwTqa1ksn238bWFPe84bzVvCGvK1h2B7sgWLETtQuWwzVdBaoRZ1HcyzddrxLcsoM5YEy4UnqcLqMU1MDca1kLw9xbazAM6Awo9y6UVWTkQcS97mYkhkmx2Tewg3JntMgzfLWz5mACiEJEv7potayvk6awmLWS36sJMfXWgnEfNiqTyXNiPzt466cgot3GLcEsYXxKzLXyJ9EfvXpjzC2abTMzVSf1e17BHre4zZvDoAeTqr4igV3ubv2PtJjntvF2ibrDLmwwAyANEhw1yt8C8fCidkf3MAoPE6T53hX3Eb2mp3Xofmtrn4qVgmhNonnV8ekWZWvBTxYiNP8Vu5nc6RMDBv7P1c5rRc3tnDMRh2dUcDD7USyoB9YcvioMfAZGMNfLjWqgYu9Ygw2FokGBPThyWrKQ5nkLJvief1eQJg4wZXKdXWAR7VxwNftdZjPCHcmwn6ByRHZo9kb4Emv3rjfZE"
      |      ]
      |    }
      |  ],
      |  "auxInputs": [
      |    {
      |      "address": {
      |        "value": "poolAddresses"
      |      },
      |      "registers": [
      |        {
      |          "num": "R4",
      |          "name": "rateUsd",
      |          "type": "Long"
      |        }
      |      ]
      |    }
      |  ],
      |  "returns": [
      |    "rateUsd"
      |  ]
      |}
      |""".stripMargin

  private val program = Parser.parse(oracleRateScript)

  private val explorer = new Explorer
  private val compiler = new JCompiler(explorer)

  private val formatUSDUtil = new FormatUtil(2)

  private var rateUSD: (Long, Option[Long]) = (0, None)      // epochTime at which obtained, and value
  private val TenMins                       = 10 * 60 * 1000 // millis

  private def getRate: Option[Long] = {
    if (rateUSD._1 > (System.currentTimeMillis() - TenMins)) rateUSD._2
    else {
      val optNanoErgsPerUsd: Option[Long] =
        compiler.compile(program).returned.find(_.name == "rateUsd").map(_.values.head.asInstanceOf[KioskLong].value)
      rateUSD = System.currentTimeMillis() -> optNanoErgsPerUsd
      optNanoErgsPerUsd
    }
  }

  override def getUsd(nanoErgs: BigInt): Option[String] =
    Try {
      getRate.map(long => formatUSDUtil.formatLongNumber(nanoErgs.toLong * 100 / long))
    }.getOrElse(None)
}
