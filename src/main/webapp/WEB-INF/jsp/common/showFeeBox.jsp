
                                                            <span class="MaxBox">
                                                                <small><small>
                                                                    <table border="0" id="feebox" class="feebox">
                                                                    <tbody>
                                                                        <tr>
                                                                            <tbody>
                                                                                <tr>
                                                                                    <td>
                                                                                        Pay mining fee of:
                                                                                    </td>
                                                                                    <td>
                                                                                        <input size="10" width="10" id="customFeeAmt" name="customFeeAmt" value="<%out.print(org.UrlWallet.Wallet.formatUtil().formatLongNumber(fee));%>" type="text"> <%out.print(currencySymbol);%></label>
                                                                                    </td>
                                                                                    <td>
                                                                                        &nbsp; (min fee <%out.print(org.UrlWallet.Wallet.formatUtil().formatLongNumber(fee));%>)
                                                                                    </td>
                                                                                </tr>
                                                                            </tbody>
                                                                        </tr>
                                                                    </tbody>
                                                                    </table>
                                                                </small></small>
                                                            </span>
