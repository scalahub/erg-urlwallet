package org

import org.ErgUrlWallet.utils.Client
import org.UrlWallet._
import org.apache.commons.codec.binary.Hex
import org.ergoplatform.ErgoAddressEncoder
import org.ergoplatform.appkit.{ErgoToken => AppKitToken, InputBox => _, _}
import sigmastate.Values.ConstantNode
import sigmastate.eval.{bigIntToBigInteger, _}
import sigmastate.interpreter.CryptoConstants
import sigmastate.serialization.ValueSerializer
import sigmastate.{SType, Values, _}
import special.collection.Coll
import special.sigma.{BigInt, GroupElement}

import scala.{BigInt => ScalaBigInt}

package object ErgUrlWallet {
  def deserialize(hex: String): ErgoValue[_] = {
    val bytes: Array[Byte]         = Hex.decodeHex(hex.toCharArray)
    val value: Values.Value[SType] = ValueSerializer.deserialize(bytes)
    value match {
      case ConstantNode(g, SGroupElement)     => ErgoValue.of(g.asInstanceOf[GroupElement])
      case ConstantNode(i, SBigInt)           => ErgoValue.of(i.asInstanceOf[BigInt])
      case ConstantNode(l, SLong)             => ErgoValue.of(l.asInstanceOf[Long])
      case ConstantNode(c, _: SCollection[_]) => ErgoValue.of(c.asInstanceOf[Coll[Byte]].toArray)
      case ConstantNode(i, SInt)              => ErgoValue.of(i.asInstanceOf[Int])
      case any                                => throw new Exception(s"Unsupported encoded value $any")
    }
  }

  implicit def bytesToString(bytes: Array[Byte]) = Hex.encodeHexString(bytes)

  private val ErgoGroupElementType: ErgoType[GroupElement] = ErgoType.groupElementType
  private val ErgoIntType                                  = ErgoType.integerType()
  private val ErgoBigIntType                               = ErgoType.bigIntType()
  private val ErgoLongType                                 = ErgoType.longType()
  private val ErgoCollByteType: ErgoType[Coll[Byte]]       = ErgoType.collType(ErgoType.byteType())

  def serialize(ergoValue: ErgoValue[_]): String = {
    ergoValue.getType match {
      case ErgoIntType          => ValueSerializer.serialize(ergoValue.asInstanceOf[ErgoValue[Int]].getValue)
      case ErgoGroupElementType => ValueSerializer.serialize(ergoValue.asInstanceOf[ErgoValue[GroupElement]].getValue)
      case ErgoBigIntType       => ValueSerializer.serialize(ergoValue.asInstanceOf[ErgoValue[BigInt]].getValue)
      case ErgoLongType         => ValueSerializer.serialize(ergoValue.asInstanceOf[ErgoValue[Long]].getValue)
      case ErgoCollByteType     => ValueSerializer.serialize(ergoValue.asInstanceOf[ErgoValue[Coll[Byte]]].getValue.toArray)
      case any                  => throw new Exception("Unsupported type " + any)
    }
  }

  case class ErgInputBox(
      id: String,
      amount: ScalaBigInt,
      registers: Map[String, String],
      tokens: Seq[ErgToken],
      creationHeight: Int,
      address: String,
      spendingTxId: Option[String]
  ) extends InputBox {
    val isSpent = spendingTxId.isDefined
    def getRegistersAsSeq: Seq[ErgoValue[_]] =
      registers.toSeq.sortBy(_._1).map(_._2).map { hex =>
        deserialize(hex)
      }
  }

  case class ErgToken(id: String, value: ScalaBigInt) extends CoinToken {
    def toErgoToken = new AppKitToken(id, value.toLong)
    def +(that: CoinToken) =
      that match {
        case ErgToken(`id`, thatValue) => ErgToken(id, value + thatValue)
        case ErgToken(_, _)            => throw new Exception("Cannot add different token ids")
        case any                       => throw new Exception(s"Unknown CoinToken ${any.getClass.getCanonicalName}")
      }
  }

  case class ErgOutputBox(address: String, regs: Seq[ErgoValue[_]], value: ScalaBigInt, tokens: Seq[ErgToken]) extends OutputBox

  case class ErgSignedTx(tx: SignedTransaction) extends CoinSignedTx {
    override def hash: String = tx.getId

    override def getEncoded: String = tx.toJson(false)
  }

  case class ErgPrivateKey(bigInt: ScalaBigInt) extends CoinPrivateKey {
    val g: GroupElement = CryptoConstants.dlogGroup.generator
    override def getAddress: String = {
      val gZ: GroupElement = g.exp(bigInt.bigInteger)

      Client.usingContext { implicit ctx =>
        val contract = ctx.compileContract(
          ConstantsBuilder
            .create()
            .item(
              "gZ",
              gZ
            )
            .build(),
          "{proveDlog(gZ)}"
        )
        val addressEncoder = new ErgoAddressEncoder(ctx.getNetworkType.networkPrefix)
        addressEncoder.fromProposition(contract.getErgoTree).get.toString
      }
    }

    override def getPrivateKeyWIF: String = Hex.encodeHexString(bigInt.toByteArray)

    override def sign(message: String): String = throw new Exception("Signing is currently not supported")
  }

}
