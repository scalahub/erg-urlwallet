        <script>
        function xmlhttpPostSend(strURL) {
            var validation = validateInputSend();
            if (validation != 'ok') {
                document.getElementById("sendresult").innerHTML = validation;
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
            document.getElementById("sendresult").innerHTML = '<img height="20" src="/static/common/CircularProcessing.gif" alt="processing"></img>'; //'processing...';
            self.xmlHttpReq.open('POST', strURL, true);
            self.xmlHttpReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            self.xmlHttpReq.onreadystatechange = function() {
                if (self.xmlHttpReq.readyState == 4) {
                    updateSend(self.xmlHttpReq.responseText);
                }
            }
            self.xmlHttpReq.send(getquerystringSend());
        }
        function validateInputSend(){
            var result = 'ok';
            var form = document.forms['frm1'];

            var toaddr = form.address.value.trim();
            var amt = form.amt.value.trim();
            var token = form.token.value.trim();

            var answer = form.answer.value;

            var customFeeAmt = <%out.print(org.UrlWallet.Wallet.formatUtil().formatLongNumber(minFee));%>;
            if (form.customFeeAmt === undefined) {
                // do nothing
            } else {
                customFeeAmt = form.customFeeAmt.value.trim();
            }

            var secondAddr = document.getElementById('sendToSecondAddressCheckBox').checked;
            var addInput = document.getElementById('addInputCheckBox').checked;

            var toaddr2 = '';
            var amt2 = '';
            var token2 = '';

            if (secondAddr) {
                 toaddr2 = form.address2.value.trim();
                 amt2 = form.amt2.value.trim();
                 token2 = form.token2.value.trim();
            }

            var inputBoxId = '';
            var copyTo = '';

            if (addInput) {
                 inputBoxId = form.inputBoxId.value.trim();
                 copyTo = form.copyTo.value.trim();
            }

            var minFee = <%out.print(org.UrlWallet.Wallet.formatUtil().formatLongNumber(minFee));%>;
            var minAmt = <%out.print(org.UrlWallet.Wallet.formatUtil().formatLongNumber(minAmt));%>;

            if (toaddr == '') {
                result = 'To address cannot be empty';
            } else if (customFeeAmt < minFee) {
                result = 'Min fee is '+minFee;
            } else if (amt == '') {
                result = 'Amount cannot be empty';
            } else if (isNaN(amt)) {
                result = 'Amount must be a number';
            } else if (parseFloat(amt) < minAmt) {
                result = 'Amount must be >= '+minAmt ;
            } else if (token == '') {
                result = 'Token cannot be empty (set to 0 for none)';
            } else if (isNaN(token)) {
                result = 'Token must be a number';
            } else if (answer == '') {
                result = 'Please enter captcha';
            } else if (secondAddr) {
                if (toaddr2 == '') {
                    result = '2nd to address cannot be empty';
                } else if (amt2 == '') {
                    result = '2nd amount cannot be empty';
                } else if (isNaN(amt2)) {
                    result = '2nd amount must be a number';
                } else if (parseFloat(amt2) < minAmt) {
                    result = '2nd amount must >= '+minAmt ;
                } else if (token2 == '') {
                    result = '2nd token cannot be empty (set to 0 for none)';
                } else if (isNaN(token2)) {
                    result = '2nd token must be a number';
                }
            } else if (addInput) {
                if (inputBoxId == '') {
                    result = 'Input box Id cannot be empty';
                } else if (copyTo == '') {
                    result = 'Copy registers parameter must be defined';
                }
                if (copyTo === 'second') {
                    if (!secondAddr) {
                        result = 'Second output must be defined for copying registers';
                    }
                }
            }
            return result;
        }

        function getquerystringSend() {
            var form = document.forms['frm1'];
            var csrfform = document.forms['csrf'];
            var toaddr = form.address.value.trim();
            var amt = form.amt.value.trim();
            var secretData = form.secretData.value;
            var csrf = csrfform.csrf.value;
            var answer = form.answer.value;
            var token = form.token.value.trim();
            var tokenid = form.tokenid.value.trim();

            var customFeeAmt = <%out.print(org.UrlWallet.Wallet.formatUtil().formatLongNumber(minFee));%>;
            if (form.customFeeAmt === undefined) {
                // do nothing
            } else {
                customFeeAmt = form.customFeeAmt.value.trim();
            }
            var secondAddrData = '';
            var secondAddr = document.getElementById('sendToSecondAddressCheckBox').checked;

            var addInputData = '';
            var addInput = document.getElementById('addInputCheckBox').checked;

            var allowTokenBurnData = '';
            var optimizeInputsData = '';
            var allowTokenBurn = document.getElementById('allowTokenBurnCheckBox').checked;
            var optimizeInputs = document.getElementById('optimizeInputsCheckBox').checked;

            if (secondAddr) {
                var toaddr2 = form.address2.value.trim();
                var amt2 = form.amt2.value.trim();
                var token2 = form.token2.value.trim();
                var tokenid2 = form.tokenid2.value.trim();
                secondAddrData = '&address2='+escape(toaddr2)+'&amt2='+escape(amt2)+'&token2='+escape(token2)+'&tokenid2='+escape(tokenid2);
            }
            if (addInput) {
                var inputBoxId = form.inputBoxId.value.trim();
                var copyTo = form.copyTo.value.trim();
                addInputData = '&inputBoxId='+escape(inputBoxId)+'&copyTo='+escape(copyTo);
            }
            if (allowTokenBurn) {
                allowTokenBurnData = '&allowTokenBurn='+escape("true")
            } else {
                allowTokenBurnData = '&allowTokenBurn='+escape("false")
            }
            if (optimizeInputs) {
                optimizeInputsData = '&optimizeInputs='+escape("true")
            } else {
                optimizeInputsData = '&optimizeInputs='+escape("false")
            }
            qstr = 'address='+escape(toaddr)+'&amt='+escape(amt)+'&token='+escape(token)+'&tokenid='+escape(tokenid)+'&secretData='+escape(secretData)+'&customFeeAmt='+escape(customFeeAmt)+'&csrf='+escape(csrf)+'&answer='+escape(answer)+secondAddrData+addInputData+allowTokenBurnData+optimizeInputsData;
            return qstr;
        }

        function updateSend(str){
            var st = str.trim();
            if (st === "send:reload") {
                location.reload();
            } else if (st.substring(0, 5) === "send:") {
                var output = st.substring(5);
                document.getElementById("sendresult").innerHTML = output;
                if (output.substring(0, 5) !== "Error") {
                    setTimeout(document.getElementById("refresh").click(), 1000);
                }
            }
        }
	    function toggleSecondSendCheckBox() {
	    	if (document.getElementById('sendToSecondAddressCheckBox').checked) {
		        showSecondSendClicked();
	    	} else {
	    	    hideSecondSendClicked();
	    	}
	    }
	    function hideSecondSendClicked() {
	    	hide('secondSendBoxAddressText');
	    	hide('secondSendBoxAddress');
	    	hide('secondSendBoxAmountText');
	    	hide('secondSendBoxAmount');
	    }
	    function showSecondSendClicked() {
	    	show('secondSendBoxAddressText');
	    	show('secondSendBoxAddress');
	    	show('secondSendBoxAmountText');
	    	show('secondSendBoxAmount');
	    }
			
	    function toggleAddInputCheckBox() {
	    	if (document.getElementById('addInputCheckBox').checked) {
		        showAddInputClicked();
	    	} else {
		        hideAddInputClicked();
	    	}
	    }
	    function hideAddInputClicked() {
	    	hide('inputBoxText');
	    	hide('inputBoxRegistersText');
	    	hide('inputBoxRetrievedResult');
	    	hide('inputBoxRegisters');
	    	hide('inputBox');
	    }
	    function showAddInputClicked() {
	    	show('inputBoxText');
	    	show('inputBoxRegistersText');
	    	show('inputBoxRetrievedResult');
	    	show('inputBoxRegisters');
	    	show('inputBox');
	    }

        </script>
