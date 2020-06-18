
                                        <tr class="d1">
                                            <td style="vertical-align: middle; text-align:center;"><span class="MaxBox">
                                                <table style="width: 165px; text-align:center;">
                                                    <tbody><tr>
                                                        <td style="text-align: center; z-index: 99;">
                                                            <b>
                                                                <a target="_blank" href="/go/to?addr=<%out.print(browseURL+address);%>">
                                                                    <%out.print(addressMessage);%>
                                                                </a>
                                                            </b>
                                                        </td>
                                                        <td style="text-align: left;">
                                                            <small>
                                                            <div class="tooltip">
                                                                <a onclick="copyAddr()" onmouseout="outFunc()" href="javascript:void(0);%>">
                                                                    <span class="tooltiptext" id="copyAddrToolTip">Copy to clipboard</span>
                                                                    copy
                                                                </a>
                                                            </div>
                                                            </small>
                                                        </td>
                                                    </tr>
                                                </tbody></table></span>
                                            </td>
                                            <td style="width: 440px; vertical-align: middle; text-align:center;">
                                            <span class="MaxBox">
                                                <b>
                                                    <div id="addr-to-copy">
                                                        <%out.print(address);%>
                                                    </div>
                                                </b>
                                            </span>
                                            </td>
                                        </tr>
                                        <script language="Javascript">
                                            function copyAddr(){
                                                var addrElement = document.getElementById('addr-to-copy');
                                                var element = addrElement;
                                                    var range;
                                                if (document.selection) {
                                                   // IE
                                                   range = document.body.createTextRange();
                                                   range.moveToElementText(element);
                                                   range.select();
                                                } else if (window.getSelection) {
                                                   range = document.createRange();
                                                   range.selectNode(element);
                                                   window.getSelection().removeAllRanges();
                                                   window.getSelection().addRange(range);
                                                }
                                                document.execCommand("copy");
                                                var tooltip = document.getElementById("copyAddrToolTip");
                                                tooltip.innerHTML = "Address copied";
                                            }
                                            function outFunc() {
                                              var tooltip = document.getElementById("copyAddrToolTip");
                                              tooltip.innerHTML = "Copy to clipboard";
                                            }
                                        </script>
