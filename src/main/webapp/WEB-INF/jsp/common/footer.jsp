
		<div class="contactstuff" id="contactstuff">
        <table style="text-align: left; width: 100%;" cellpadding="1" cellspacing="1" border="0">
            <tbody>
            <tr>
            <td colspan="2" rowspan="1" style="vertical-align: top;">
                Amount must be >= <%out.print(org.UrlWallet.Wallet.formatUtil().formatLongNumber(org.UrlWallet.Wallet.coinUtil().minFee()));%> <%out.print(org.UrlWallet.Wallet.coinUtil().symbol());%> and
                any change less than this will be added to fee
		    </td>
	    </tr>
            </tbody>
        </table>
        </div>
		
		
