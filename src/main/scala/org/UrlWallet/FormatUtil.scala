package org.UrlWallet

class FormatUtil(d:Int) { // d is number of decimal digits
  def insertDecimalL(bigInt:BigInt) = {
    if (bigInt == null) "0" else {
      val sign = bigInt.signum
      val s = bigInt.abs.toString
      val i = s.length
      val str = if (i< (d+1)) ("0."+"0"*(d-i)+s) else s.substring(0, i-d)+"."+s.substring(i-d)
      if (sign < 0) "-"+str else str
    }
  }

  def formatStringNumber(s:String):String = noTrailing(insertDecimalS(s))
  def formatLongNumber(l:Long):String = noTrailing(insertDecimalL(l))
  def noTrailing(s:String) = if (s.indexOf(".")<0) s else s.replaceAll("0*$", "").replaceAll("\\.$", "")
  def insertDecimalS(s:String):String = if (s.equals(null)) "0" else insertDecimalL(BigInt(s))
  def insertDecimalI(s:Int):String = if (s.equals(null)) "0" else insertDecimalL(BigInt(s))

  def removeDecimalS(s:String) = {
    val bd = new BigDecimal(new java.math.BigDecimal(s))
    val m = BigDecimal("1"+"0"*d)
    val amt=bd*m
    amt.toBigInt.toString
  }

}
