
        <script language="Javascript">
            function xmlhttpPostRetrieve(strURL) {

                var form = document.forms['frm1'];
                var boxId = form.inputBoxId.value.trim();
                if (boxId == '') {
                    updateBox("read:Error: Box Id cannot be empty");
                    return;
                }

                var xmlHttpReq = false;
                var self = this;
                // Mozilla/Safari
                if (window.XMLHttpRequest) {
                    self.xmlHttpReq = new XMLHttpRequest();
                }
                // IE
                else if (window.ActiveXObject) {
                    self.xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
                }
        		updateBox("read:waiting");
                self.xmlHttpReq.open('POST', strURL, true);
                self.xmlHttpReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                self.xmlHttpReq.onreadystatechange = function() {
                    if (self.xmlHttpReq.readyState == 4) {
                        updateBox(self.xmlHttpReq.responseText);
                    }
                }
		        self.xmlHttpReq.send(getQueryStringRetrieve());
            }

            function getQueryStringRetrieve() {
                var form = document.forms['frm1'];
                var csrfform = document.forms['csrf'];
                var csrf = csrfform.csrf.value;
                var boxId = form.inputBoxId.value.trim();
                qstr = 'boxId='+escape(boxId)+'&csrf='+escape(csrf); // NOTE: no '?' before querystring
                return qstr;
            }

            var tokensInInput = [];

            function updateBox(str){
                var st = str.trim();
                if (st === "read:reload") {
                    location.reload();
                } else if (st === "read:waiting") {
        		    document.getElementById("retrieveResult").innerHTML = "<img height='20' src='/static/common/CircularProcessing.gif'/>"
		        } else if (st.substring(0, 5) === "read:") {

		            var infoStr = st.substring(5);

		            var msgType = infoStr.substring(0, 5);

		            var inputHtml = '';
		            if (msgType == "Error") {
                		inputHtml = infoStr;
		            } else {
                        var arr = infoStr.split(";", 3);
                        var inputAddr = arr[0];
                        var inputBal = arr[1];

                        var tokenInfo = arr[2];

                        var tokens = tokenInfo.split(" ");

                        var tokenBalance = '';

                        window.tokensInInput = [];

                        var i;
                        for (i = 0; i < tokens.length; i++) {
                            var token = tokens[i];
                            if (token != '') {
                                var tokenArr = token.split(":", 2);
                                var tokenAmt = tokenArr[0];
                                var tokenId = tokenArr[1];
                                window.tokensInInput[window.tokensInInput.length] = tokenId;
                                tokenBalance += "<br>"+ tokenAmt + " " + tokenId.substring(0, 10);
                            }
                        }

                        inputHtml = inputAddr + "<br>" + inputBal + " <% out.print(org.UrlWallet.Wallet.coinUtil().symbol());%>"+tokenBalance;
		            }

                    document.getElementById("retrieveResult").innerHTML = inputHtml;
                    updateTokenIds();
                }
            }
            
        </script>