# Ergo URL-Wallet
[![Build Status](https://travis-ci.com/scalahub/erg-urlwallet.svg?branch=master)](https://travis-ci.com/scalahub/erg-urlwallet)

URL-Wallet for Ergo cryptocurrency.

## Running locally

Allows to create tokens and spend boxes following the "emission-box" pattern. 

To run locally, type the following inside sbt console:
    
    jetty:start
    
Then open http://localhost:8080 to open the wallet.

## Running on a remote server

First compile war file type the following in bash console:
  
    sbt package
    
The war file can be hosted on any J2EE web server. 

## Important aspects

1. The URL is needed to access the funds stored in the wallet. Hence the URL must be stored safely. 
2. Access to the URL allows access to funds. Hence the URL needs to be protected like a secret. 
3. There are many ways URLs can leak (see below). Hence keep only small amount on it.  
   * Browser history contains all URLs. Hence extra care needs to be taken to protect history
   * Many browsers store history on cloud (e.g., Chrome when signed in)
   * Browser extensions and plugins may have access to all URLs visited in the browser.
   * HTTP referer header may be sent by browser when visiting links in the wallet. This wallet prevents this leakage by redirecting all external links to a local link first (`/go/to?addr=some_other_url`)

When modifying the wallet, make sure you pay special attention to HTTP referer header and link to external URLs using the above technique. That is, to link to `http://foo.com`, 
use the link `/go/to?addr=http://foo.com`.

You may want to consider changing the [seed](/blob/master/src/main/scala/org/UrlWallet/wallet/UrlWallet.scala#L42), so a URL in your instance won't be valid on another instance.

## Software stack

1. Ergo block explorer for getting the data about an address (unspent boxes)
2. Ergo-Appkit for signing transactions
3. JSP for front-end
4. Scala for back-end


