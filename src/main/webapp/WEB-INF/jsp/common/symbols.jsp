<%
    String currencySymbol = org.UrlWallet.Wallet.coinUtil().symbol();
    String currencyName = org.UrlWallet.Wallet.coinUtil().name();
    String currencyNameLower = currencyName.toLowerCase();
    String browseURL = org.UrlWallet.Wallet.coinUtil().browseURL();
    String tokenBrowseURL = org.UrlWallet.Wallet.coinUtil().tokenBrowseURL();

    session.setAttribute("currencySymbol",   currencySymbol);
    session.setAttribute("currencyName",     currencyName);
    session.setAttribute("currencyNameLower",currencyNameLower);
    session.setAttribute("browseURL",        browseURL);
    session.setAttribute("tokenBrowseURL",   tokenBrowseURL);
%>