
                                <script>
                                    function hideSignClicked() {
                                        showText('showsign', 'hidesign');
                                        hide('signbox');
                                    }
                                    function showSignClicked() {
                                        showText('hidesign', 'showsign');
                                        show('signbox');
                                    }
                                </script> 
								<br>
                                <small><small>
                                    <div id="showsign" class="show">
                                        <a onclick="showSignClicked()" href="javascript:void(0);">show signing options</a>
                                    </div>
                                    <div id="hidesign" class="hide">
                                        <a onclick="hideSignClicked()" href="javascript:void(0);">hide signing options</a>
                                    </div>
                                </small></small>
                                <br>
                                <div id="signbox" class="hide">
                                    <div>
                                        <table style="height: 71px;">
                                            <tbody>
                                            <tr>
                                                <td style="width: 160px; font-weight: bold;">
                                                    <span class="MaxBox">Sign message</span>
                                                    </td>
                                                <td>
                                                    <span class="MaxBox">
                                                        <small><small>
                                                        </small></small>
                                                    </span>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="width: 160px;">
                                                    <span class="MaxBox">
                                                        Message
                                                    </span>
                                                </td>
                                                <td>
                                                    <span class="MaxBox">
                                                        <input accept-charset="UTF-8" size="50" id="message" name="message" width="100">
                                                    </span>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="width: 160px; text-align: right;">
                                                    <span class="MaxBox">
                                                        <div align="center">
                                                            <input value="sign" class="myButton" type="button" onclick='xmlhttpPostSign("<%out.print(signMethod);%>")'>
                                                        </div>
                                                    </span>
                                                </td>
                                                <td>
                                                    <span class="MaxBox">
                                                        <div id="signresult" style="width: 340px;word-wrap: break-word;">
                                                        </div>
                                                    </span>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                