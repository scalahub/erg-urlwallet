
                                <div id="sendbox" class="sendbox">
                                    <table style="height: 71px;">
                                        <tbody>
                                        <tr><br>
                                            <td style="width: 160px; font-weight: bold;"><span class="MaxBox">Send <%out.print(currencyName);%></span></td>
                                            <td style="width: 450px;">
                                                <table>
                                                    <tr>
                                                        <td> <%// style="width: 250px;"> %>
                                                        <%@ include file="/WEB-INF/jsp/common/showFeeBox.jsp" %>
                                                        </td>
                                                        <input name="answer" type="hidden" value="none">
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>

					                    <tr>
                                            <td style="width: 160px;"><span class="MaxBox">To (address)</span></td>
                                            <td><span class="MaxBox"><input size="50" id="address" name="address" width="100"></span></td>
                                        </tr>
                                        <tr>
                                            <td style="width: 160px;"><span class="MaxBox">How much?<br></span></td>
                                            <td>
                                                <span class="MaxBox"><input id="amt" size="10" name="amt" type="text" width="100" value="0.1"> <%out.print(currencySymbol);%> &nbsp;
                                                <input id="token" size="10" name="token" type="text" width="100" value="0">
                                                <select id="tokenid" name="tokenid">
                                                    <option value="new" selected>new</option>
                                                </select> token
                                                </span>
                                            </td>
                                        </tr>
										<tr>
                                            <td>
                                            <div class="feebox">
                                                <div class="custom-checkbox" style="background-color:white;">
                                                <small><small>
                                                    <div class="custom-checkbox"><input type="checkbox" id="optimizeInputsCheckBox" name="optimizeInputs" value="optimizeInputs" checked><label for="optimizeInputsCheckBox" class="">Optimize Inputs</label></div>
                                                </small></small>
                                                </div>
                                            </div>
                                            </td>

											<td>
											<div class="feebox">
												<div class="custom-checkbox" style="background-color:white;" >
												<small><small>
													<label for="sendToSecondAddressCheckBox">Add second output</label>
													<input type="checkbox" id="sendToSecondAddressCheckBox" name="sendToSecondAddress" value="secondSend" onchange='JavaScript:toggleSecondSendCheckBox()'>
												</small></small>
												</div>
											</div>
											</td>
										</tr>

										<tr>											
                                            <td style="width: 160px;">
												<div class="hide" id="secondSendBoxAddressText">
													<span class="MaxBox">To (2nd address)</span>
												<div>
											</td>
											<td>
												<div class="hide" id="secondSendBoxAddress">
													<span class="MaxBox"><input size="50" id="address2" name="address2" width="100"></span>
												<div>
											</td>
										</tr>
                                        <tr>
                                            <td style="width: 160px;">
												<div class="hide" id="secondSendBoxAmountText">
													<span class="MaxBox">How much?<br></span>
												</div>
											</td>
                                            <td>
												<div class="hide" id="secondSendBoxAmount">
													<span class="MaxBox"><input id="amt2" size="10" name="amt2" type="text" width="100" value="0.1"> <%out.print(currencySymbol);%> &nbsp;
													<input id="token2" size="10" name="token2" type="text" width="100" value="0">
                                                    <select id="tokenid2" name="tokenid2">
                                                        <option value="new" selected>new</option>
                                                    </select> token
                                                    </span>
												</div>
											</td>
                                        </tr>

										<tr>
											<td>
											<div class="feebox">
												<div class="custom-checkbox" style="background-color:white;">
												<small><small>
													<div class="custom-checkbox"><input type="checkbox" id="allowTokenBurnCheckBox" name="allowTokenBurn" value="allowTokenBurn"><label for="allowTokenBurnCheckBox" class="">Allow token burning</label></div>
												</small></small>
												</div>
											</div>
											</td>
											<td>
											<div class="feebox">
												<div class="custom-checkbox" style="background-color:white;">
												<small><small>
													<div class="custom-checkbox"><input type="checkbox" id="addInputCheckBox" name="addInput" value="addInput" onchange="JavaScript:toggleAddInputCheckBox()"><label for="addInputCheckBox" class="">Add custom input (usually for "emission-box" pattern)</label></div>
												</small></small>
												</div>
											</div>
											</td>
										</tr>

                                        <tr>
                                            <td style="width: 160px;">
												<div class="hide" id="inputBoxText">
													<span class="MaxBox">Input Box Id <small>(<a onclick='JavaScript:xmlhttpPostRetrieve("<%out.print(retrieveMethod);%>")' href="javascript:void(0);">retrieve</a>)</small></span>
												</div>
											</td>
										    <td>
                                                <div class="hide" id="inputBox">
                                                    <span class="MaxBox"><input size="50" id="inputBoxId" name="inputBoxId" width="100"></span>
                                                    <div></div>
                                                </div>
										    </td>
                                        </tr>
                                        <tr>
                                            <td style="width: 160px;">
                                                <div class="hide" id="inputBoxRegistersText">
                                                    <span class="MaxBox">Copy all registers to</span>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="hide" id="inputBoxRegisters">
                                                    <select id="copyTo" name="copyTo">
                                                      <option value="no">no</option>
                                                      <option value="first">first</option>
                                                      <option value="second" selected>second</option>
                                                    </select> output
                                                </div>
                                            </td>
                                        </tr>
                    					<tr>
                                            <td style="width: 160px;"></td>
                                            <td>
                                                <div class="hide" id="inputBoxRetrievedResult">
                                                    <span class="MaxBox"> <div id="retrieveResult"></div>
                                                <div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="width: 160px; text-align: right;"><span class="MaxBox">
                                            <div align="center">
                                            <input value="send" class="myButton" type="button" onclick='JavaScript:xmlhttpPostSend("<%out.print(sendMethod);%>")'>
                                            </div></span>
                                            </td>
                                            <td><span class="MaxBox"> <div id="sendresult"></div></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
