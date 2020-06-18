package org.UrlWallet

import org.ErgUrlWallet
import org.apache.commons.codec.binary.Hex
import org.ergoplatform.appkit.{ErgoValue, JavaHelpers}
import special.sigma.GroupElement

object TestGroupElementSerializer extends App {
  val groupElementPrefix = "07"
  def groupElementToHex(groupElement: ErgoValue[GroupElement]) = {
    groupElementPrefix + Hex.encodeHexString(groupElement.getValue.getEncoded.toArray)
  }
  def hexToGroupElement(hex:String) = {
    if (hex.startsWith(groupElementPrefix)) {
      ErgoValue.of(JavaHelpers.decodeStringToGE(hex.drop(2)))
    } else throw new Exception("Prefix mismatch for group element")
  }

  val hex = "0702f1a4e08382396a43470eca307e89ab3c01fa27d42f33a47124c4f733b47217e1"
  val kioskGroupElement: ErgoValue[GroupElement] = hexToGroupElement(hex)
  val kisokHex: String = groupElementToHex(kioskGroupElement)
  val appKitHex: String = ErgUrlWallet.serialize(kioskGroupElement)
  assert(hex == kisokHex)
  assert(hex == appKitHex)

  val appKitErgoValue: ErgoValue[_] = ErgUrlWallet.deserialize(hex)
  assert(hex == ErgUrlWallet.serialize(appKitErgoValue))
  assert(appKitErgoValue.isInstanceOf[ErgoValue[GroupElement]])

  val appKitGroupElement: ErgoValue[GroupElement] = ErgUrlWallet.deserialize(hex).asInstanceOf[ErgoValue[GroupElement]]
  val appKitSerialized: String = ErgUrlWallet.serialize(appKitGroupElement)
  val kioskSerialized:String = groupElementToHex(appKitGroupElement)
  assert(hex == appKitSerialized)
  assert(hex == kioskSerialized)

  print("Test passed")
}
