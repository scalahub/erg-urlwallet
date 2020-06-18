
                <table style="background:black; width: 150px; margin-left:auto; margin-right:auto; vertical-align: middle; text-align:center;" border="0">
                    <tbody>
                        <tr>
                            <td style="width: 150px; vertical-align: middle; text-align:center;"><span class="MaxBox">
                            <div id="captcha" class="captcha">
                                <img height="30" width="120" id="captcha" name="captcha" src="/captcha/img<%out.print(org.UrlWallet.Random.randString());%>"/>
                            </div>
                            </span>
                            </td>
                        </tr>                    
                        <tr><td style="width: 150px; vertical-align: middle; text-align:center;"> 
                        <a id="reloadcaptcha" href="javascript:void(null)" onclick="reloadpic()"><font face="verdana">new image</font></a>  
                        </td></tr>
                        <tr><td style="width: 150px; vertical-align: middle; text-align:center;"> <input size="10" name="answer" type="text">  </td></tr>
                    </tbody>
                </table>
