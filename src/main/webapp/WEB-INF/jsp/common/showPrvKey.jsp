
                                        <tr class="d2">
					                        <script>
                                                function hideClicked() {
                                                    showText('nokeyshown','keyshown');
                                                    showText('showkey', 'hidekey');
                                                }
                                                function showClicked() {
                                                    showText('keyshown','nokeyshown');
                                                    showText('hidekey', 'showkey');
                                                }
                                            </script>
                                            <td style="width: 160px; text-align:center;"><span class="MaxBox"><span class="MaxBox">
                                            <small>
                                            <div id="showkey" class="show">
                                                <table style="width: 160px; text-align:center;">
                                                    <tr>
                                                        <td style="width: 105px;text-align:center; z-index:99">Secret key
                                                        </td>
                                                        <td style="text-align:left;"><a onclick="showClicked()" href="javascript:void(0);">show</a></td>
                                                    </tr>
                                                </table>
                                            </div>
                                            <div id="hidekey" class="hide">
                                                <table style="width: 160px; text-align:center;">
                                                    <tr>
                                                        <td style="width: 105px;text-align:center;">Secret key
                                                        </td>
                                                        <td style="text-align:left;"><a onclick="hideClicked()" href="javascript:void(0);">hide</a></td>
                                                    </tr>
                                                </table>
                                            </div>
                                            </small></span></td>
                                            <td style="width: 350px; vertical-align: middle; text-align:center;"><span class="MaxBox">
                                            <small>
                                            <div id="nokeyshown" class="show"> Click "show" to view private key</div>
                                            <div id="keyshown" class="hide"> <%out.print(prvKey);%></div>
                                            </small>
                                            </span></td>
                                        </tr>
