        <script language="Javascript">

            function xmlhttpPostRefresh(strURL) {
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
		        updatebalance("read:waiting");
                self.xmlHttpReq.open('POST', strURL, true);
                self.xmlHttpReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                self.xmlHttpReq.onreadystatechange = function() {
                    if (self.xmlHttpReq.readyState == 4) {
                        updatebalance(self.xmlHttpReq.responseText);            
                    }
                }
		        self.xmlHttpReq.send(getquerystringRefresh());
            }

            function getquerystringRefresh() {
                var form = document.forms['frm1'];
                var csrfform = document.forms['csrf'];
                var csrf = csrfform.csrf.value;
                var addr = form.addr.value;
                qstr = 'addr='+ escape(addr)+'&csrf='+escape(csrf); // NOTE: no '?' before querystring
                return qstr;
            }

            var tokensInAddress = [];

            function updatebalance(str){
                var st = str.trim();
                if (st === "read:reload") {
                    location.reload();
                } else if (st === "read:error") {
                    document.getElementById("balanceresult").innerHTML = "<a href='javascript:void(null)' onclick='window.alert(\"Error reading balance. Please refresh.\")'><img HEIGHT='13' WIDTH='13' src='/static/common/warning.png'/></a>";
                } else if (st === "read:waiting") {
        		    document.getElementById("balanceresult").innerHTML = "<img height='20' src='/static/common/CircularProcessing.gif'/>"
		        } else if (st.substring(0, 5) === "read:") {
		            var balanceStr = st.substring(5);
		            var arr = balanceStr.split(";", 2);
		            var primaryBalance = arr[0];

                    var tokenInfo = arr[1];

                    var tokens = tokenInfo.split(" ");

                    var tokenBalance = '';

                    window.tokensInAddress = [];

                    var i;
                    for (i = 0; i < tokens.length; i++) {
                        var token = tokens[i];
                        if (token != '') {
                            var tokenArr = token.split(":", 2);
                            var tokenId = tokenArr[0];
                            var tokenAmt = tokenArr[1];
                            window.tokensInAddress[window.tokensInAddress.length] = tokenId;
                            tokenBalance += "<div><span><small><small>"+ tokenAmt+"</small></small> </span> <span> <small><small><small>" +
                            tokenId.substring(0, 10) + "</small></small></small></span></div>";
                        }
                    }

                    var balance = "<div><span>"+primaryBalance+"</span> <span> <small><small><%out.print(currencySymbol);%></small></small></span></div>"+tokenBalance

                    document.getElementById("balanceresult").innerHTML = balance;
                    updateTokenIds();

                }
            }

            function updateTokenIds() {
                var newToken = ["new"];
                var tokens = newToken.concat(Array.from(new Set(window.tokensInAddress.concat(window.tokensInInput))));

                var tokenSelect = document.getElementById('tokenid');
                var token2Select = document.getElementById('tokenid2');

                var tokenIdCurrent = tokenSelect.options[tokenSelect.selectedIndex].value;
                var tokenId2Current = token2Select.options[token2Select.selectedIndex].value;

                tokenSelect.length = 0;
                token2Select.length = 0;

                tokenSelect.selectedIndex = 0;
                token2Select.selectedIndex = 0;

                var i;
                for (i = 0; i < tokens.length; i++) {
                    var tokenId = tokens[i];

                    var objOption = document.createElement("option");
                    objOption.text = tokenId.substring(0, 10);
                    objOption.value = tokenId;
                    tokenSelect.options.add(objOption);
                    if (tokenIdCurrent == tokenId) { // select this
                         tokenSelect.selectedIndex = i;
                    }

                    var objOption2 = document.createElement("option");
                    objOption2.text = tokenId.substring(0, 10);
                    objOption2.value = tokenId;
                    token2Select.options.add(objOption2);
                    if (tokenId2Current == tokenId) { // select this
                        token2Select.selectedIndex = i;
                    }
                }
            }
        </script>