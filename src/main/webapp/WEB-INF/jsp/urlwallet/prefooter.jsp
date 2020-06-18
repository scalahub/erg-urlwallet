
        <div class="footerstuff" id="footerstuff">
        <table width="81%" border="1" cellpadding="1" cellspacing="1" style="text-align: left; width: 100%;">
            <tbody>
            <tr>
                <td colspan="2" rowspan="1"  bgcolor="#FFFFFF" style="vertical-align: top; width: 376px; font-family: 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', 'DejaVu Sans', Verdana, sans-serif; color: #000000; text-align: left;">
                    <span style="text-align: center; color: #EA080C;">
                        <small>
                        Amount must be >= <%out.print(org.UrlWallet.Wallet.formatUtil().formatLongNumber(org.UrlWallet.Wallet.coinUtil().minFee()));%> <%out.print(org.UrlWallet.Wallet.coinUtil().symbol());%> and
                        any change less than this will be added to fee
                        </small>
                    </span>
              </td>              
            </tr>
            </tbody>
        </table>
        </div>
        