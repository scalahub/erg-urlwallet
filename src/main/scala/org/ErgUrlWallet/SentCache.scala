package org.ErgUrlWallet

import org.sh.db.ScalaDB._
import org.sh.db.config.TraitDBConfig
import org.sh.db.core.DataStructures._
import org.ergoplatform.appkit.{ErgoValue, InputBox => AppkitInputBox}

import scala.util.Try

object SentCache {
  val STR               = VARCHAR(255)
  val BIGSTR            = VARCHAR(1000)
  val BIGINT            = UBIGDEC(100, 0)
  val addressCol        = Col("address", BIGSTR)
  val boxIdCol          = Col("box_id", STR)
  val valueCol          = Col("amount", BIGINT)
  val tokenIdCol        = Col("token_id", STR)
  val creationHeightCol = Col("creation_height", UINT)
  val registerIdCol     = Col("register_id", STR)
  val registerValueCol  = Col("register_value", BIGSTR)
  val tokenIndexCol     = Col("token_index", UINT)
  val timeCol           = Col("time", ULONG)
  val feeCol            = Col("fee", BIGINT)

  implicit val config = new TraitDBConfig {
    override val dbname: String       = "memdb"
    override val dbuser: String       = "user"
    override val dbpass: String       = "pass"
    override val dbhost: String       = ""    // will be ignored
    override val dbms: String         = "h2server"
    override val connTimeOut: Max     = 10000 // will be ignored
    override val usePool: Boolean     = true  // will be ignored
    override val configSource: String = "code"

    override def url = s"jdbc:h2:mem:$dbname;DB_CLOSE_DELAY=-1"
    println("Initializing memory db")
    init
  }

  val spentBoxTable            = Tab.withName("spent").withCols(addressCol, boxIdCol, timeCol).withPriKey(boxIdCol)
  val createdBoxTable          = Tab.withName("created").withCols(addressCol, boxIdCol, valueCol, creationHeightCol, timeCol).withPriKey(boxIdCol)
  val createdBoxTokensTable    = Tab.withName("tokens").withCols(boxIdCol, tokenIndexCol, tokenIdCol, valueCol).withPriKey(boxIdCol, tokenIdCol)
  val createdBoxRegistersTable = Tab.withName("registers").withCols(boxIdCol, registerIdCol, registerValueCol).withPriKey(boxIdCol, registerIdCol)

  val tenMins     = 1000 * 60 * 10 // millis
  def pastTenMins = System.currentTimeMillis() - tenMins

  def getSpentBoxIds(address: String) = {
    val past = System.currentTimeMillis() - tenMins

    spentBoxTable.select(boxIdCol).where(addressCol === address, timeCol >= pastTenMins).firstAsT[String]
  }

  def getReceivedBoxes(address: String) = {
    createdBoxTable.select(boxIdCol, valueCol, creationHeightCol).where(addressCol === address, timeCol >= pastTenMins).as { boxArr =>
      val i              = boxArr.toIterator
      val boxId          = i.next().as[String]
      val value          = i.next().as[BigDecimal].toBigInt()
      val creationHeight = i.next().as[Int]
      val registers: Map[String, String] = createdBoxRegistersTable
        .select(registerIdCol, registerValueCol)
        .where(boxIdCol === boxId)
        .as { regArr =>
          val regId = regArr(0).as[String]
          val value = regArr(1).as[String]
          regId -> value
        }
        .toMap

      val tokens: Seq[ErgToken] = createdBoxTokensTable
        .select(tokenIndexCol, tokenIdCol, valueCol)
        .where(boxIdCol === boxId)
        .as { tokArr =>
          val index      = tokArr(0).as[Int]
          val tokenId    = tokArr(1).as[String]
          val tokenValue = tokArr(2).as[BigDecimal].toBigInt()
          (index, ErgToken(tokenId, tokenValue))
        }
        .sortBy(_._1)
        .map(_._2)

      ErgInputBox(boxId, value, registers, tokens, creationHeight, address, None)
    }
  }

  def addTx(address: String, inputBoxIds: Array[String], height: Int, changeBox: Option[(ErgOutputBox, AppkitInputBox)]) = {
    val now = System.currentTimeMillis()
    inputBoxIds.foreach { inputBoxId =>
      Try(
        spentBoxTable.insert(address, inputBoxId, now)
      )
    }
    changeBox match {
      case Some((e: ErgOutputBox, a: AppkitInputBox)) =>
        val boxId = a.getId.toString
        Try(
          createdBoxTable.insert(address, boxId, a.getValue, height, now)
        )
        e.regs.zipWithIndex.foreach {
          case (ergoValue: ErgoValue[_], index: Int) =>
            val hex     = serialize(ergoValue)
            val regName = "R" + index + 4
            createdBoxRegistersTable.insert(boxId, regName, hex)
          case _ => ???
        }
        e.tokens.zipWithIndex.foreach {
          case (token, index) =>
            Try(createdBoxTokensTable.insert(boxId, index, token.id, token.value))
        }
      case any =>
    }
  }
}
