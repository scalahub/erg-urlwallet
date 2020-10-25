package org.ErgUrlWallet

import org.UrlWallet.{CoinReader, Curl, InputBox}

object ErgReader extends CoinReader {

  import io.circe.Json

  // private val baseUrl = "https://new-explorer.ergoplatform.com"
  private val baseUrl = "https://api.ergoplatform.com"
  //  private val unspentUrl = s"$baseUrl/transactions/boxes/byAddress/unspent/"
  private val unspentUrl = s"$baseUrl/api/v0/transactions/boxes/byAddress/unspent/"
  private val boxUrl = s"$baseUrl/transactions/boxes/"

  override def getBoxById(boxId: String) = {
    getUtxoBoxFromJson(Curl.get(boxUrl + boxId))
  }

  private def getUtxoBoxFromJson(j: Json) = {
    val id = getId(j)
    val value = (j \\ "value").map(v => v.asNumber.get).apply(0)
    val creationHeight = (j \\ "creationHeight").map(v => v.asNumber.get).apply(0)
    val assets: Seq[Json] = (j \\ "assets").map(v => v.asArray.get).apply(0)
    val tokens: Seq[ErgToken] = assets.map { asset =>
      val tokenID = (asset \\ "tokenId").map(v => v.asString.get).apply(0)
      val value = (asset \\ "amount").map(v => v.asNumber.get).apply(0).toLong.get
      ErgToken(tokenID, value)
    }
    val registers = (j \\ "additionalRegisters").flatMap { r =>
      r.asObject.get.toList.map {
        case (key, value) => (key, value.asString.get)
      }
    }.toMap

    val address = (j \\ "address").map(v => v.asString.get).apply(0)
    val spendingTxId = (j \\ "spentTransactionId").map(v => v.asString).apply(0)
    ErgInputBox(id, value.toLong.get, registers, tokens, creationHeight.toInt.get, address, spendingTxId)
  }

  private def getId(j: Json) = (j \\ "id").map(v => v.asString.get).apply(0)

  private def getUtxoBoxesFromJson(json: Json): Seq[ErgInputBox] = {
    json.asArray.get.map(getUtxoBoxFromJson)
  }

  override def getUnspentBoxes(address: String): (Array[InputBox], ErgReader.IsConfirmed) = {
    val spentBoxIdsFromMemory: Seq[String] = SentCache.getSpentBoxIds(address)
    val receivedBoxesFromMemory: Set[ErgInputBox] = SentCache.getReceivedBoxes(address).toSet // don't count received boxes for now

    val receivedBoxesFromExplorer: Set[ErgInputBox] = getUtxoBoxesFromJson(Curl.get(unspentUrl + address)).toSet

    val explorerIds: Set[String] = receivedBoxesFromExplorer.map(_.id)
    val memoryIds: Set[String] = receivedBoxesFromMemory.map(_.id)

    val allReceivedIds: Set[String] = explorerIds ++ memoryIds

    val unspentIds: Set[String] = allReceivedIds.filterNot(spentBoxIdsFromMemory.contains)

    val allReceivedBoxes: Set[ErgInputBox] = receivedBoxesFromExplorer ++ receivedBoxesFromMemory

    val unspentBoxes: Set[ErgInputBox] = unspentIds.map { unspentId =>
      allReceivedBoxes.find(_.id == unspentId).get
    }

    val unspentInputBoxes: Array[InputBox] = unspentBoxes.map(_.asInstanceOf[InputBox]).toArray

    val isConfirmed = unspentIds.forall(explorerIds.contains)

    (unspentInputBoxes, isConfirmed)
  }
}
