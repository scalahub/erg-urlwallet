<%
    String currencySymbol = org.UrlWallet.Wallet.coinUtil().symbol();
    String currencyName = org.UrlWallet.Wallet.coinUtil().name();
    String currencyNameLower = currencyName.toLowerCase();
    String browseURL = org.UrlWallet.Wallet.coinUtil().browseURL();

    session.setAttribute("currencySymbol",   currencySymbol);
    session.setAttribute("currencyName",     currencyName);
    session.setAttribute("currencyNameLower",currencyNameLower);
    session.setAttribute("browseURL",        browseURL);
%>