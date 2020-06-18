package org.ErgUrlWallet.db.config

import org.h2.tools.Server

trait DBConfig {
  val dbname:String // e.g. petstore
  val dbuser:String // e.g. user
  val dbpass:String // e.g. pass

  def url = s"jdbc:h2:mem:$dbname;DB_CLOSE_DELAY=-1"
    
  def init = {
    try {
      val server = Server.createTcpServer()
      if (! server.isRunning(true)) server.start
    } catch { case any:Throwable => println ("could not start h2 db server") }
    Class.forName("org.h2.Driver")
  }
  init
}
