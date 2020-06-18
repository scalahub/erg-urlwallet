<%! 
    public String fullUrl(HttpServletRequest req, String urlPattern) {
        String relativeUrl=org.UrlWallet.Wallet.keyWallet().getRedirectUrl(urlPattern);
        if (isLocalHost(req)) {
            return relativeUrl;
        } else {
            return "https://"+req.getServerName()+relativeUrl;
        }
    }
%>
