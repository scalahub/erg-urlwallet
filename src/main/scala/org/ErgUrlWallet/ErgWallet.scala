package org.ErgUrlWallet

import org.ErgUrlWallet.utils.Client
import org.UrlWallet.GenericWallet

class ErgWallet extends GenericWallet(ErgUtil, ErgReader, ErgWriter) {
  Client.setClient("http://88.198.13.202:9053/")
}
