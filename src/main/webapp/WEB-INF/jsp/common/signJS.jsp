
        <script>
            function xmlhttpPostSign(strURL) {
                var validation = validateInputSign();
                if (validation != 'ok') {
                    document.getElementById("signresult").innerHTML = validation;
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
                document.getElementById("signresult").innerHTML = '<img height="28" src="/static/common/CircularProcessing.gif" alt="processing"></img>'; //'processing...';
                self.xmlHttpReq.open('POST', strURL, true);
                self.xmlHttpReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                self.xmlHttpReq.onreadystatechange = function() {
                    if (self.xmlHttpReq.readyState == 4) {
                        updateSign(self.xmlHttpReq.responseText); 
                    }
                }
                self.xmlHttpReq.send(getquerystringSign());
            }
            function validateInputSign(){
                var result = 'ok';
                var form = document.forms['frm1'];
                var message = form.message.value;
                if (message == '') {
                    result = 'message cannot be empty';
                } 
                return result;
            }
            
            function getquerystringSign() {
                var form = document.forms['frm1'];
                var csrfform = document.forms['csrf'];
                var message = form.message.value;
                var secretData = form.secretData.value;
                var csrf = csrfform.csrf.value;
                qstr = 'message='+encodeURIComponent(message)+'&secretData='+escape(secretData)+'&csrf='+escape(csrf);
                return qstr;
            }
            function updateSign(str){
                var st = str.trim();
                if (st === "sign:reload") {
                    location.reload();
                } else if (st === "sign:encoding") {
                    document.getElementById("signresult").innerHTML = "unsupported encoding";                                        
                } else if (st.substring(0, 5) === "sign:"){                              
                    document.getElementById("signresult").innerHTML = st.substring(5);                    
                }
            }            
        </script>
