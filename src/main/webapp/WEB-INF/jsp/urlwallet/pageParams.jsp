<%
        String urlPattern=request.getPathInfo();  
        boolean isNewPatternNeeded = (urlPattern == null || urlPattern.replace("/", "").equals(""))? true : false;
        boolean isHttpsNeeded = (request.getScheme().equals("http") && (!isLocalHost(request)))? true : false;
        if (isNewPatternNeeded) {
            /* 
                No url pattern given. User came on bare site url 
                We need to construct a random wallet (i.e., url)
            */
            urlPattern = "/"+randomPattern;
        }
            
        if (isNewPatternNeeded || isHttpsNeeded) { 
            response.sendRedirect(fullUrl(request, urlPattern)); 
            return;
        } 
        long minFee = org.UrlWallet.Wallet.coinUtil().minFee();
        long minAmt = org.UrlWallet.Wallet.coinUtil().minAmt();
        long defaultAmt = org.UrlWallet.Wallet.coinUtil().defaultAmt();

	
%>