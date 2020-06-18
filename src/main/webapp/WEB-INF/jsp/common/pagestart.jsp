<%        
    String pageName = "Your "+currencyName+" Wallet "+address;
%>
        <title><%out.print(pageName); %></title>
        
        <!-- fancy radio button and checkboxes stuff -->
        	<script type="text/javascript" src="/static/common/jquery.min.js"></script>
            <script type="text/javascript" src="/static/common/customInput.jquery.js"></script>
            <script type="text/javascript">
            // Run the script on DOM ready:
            $(function(){
                $('#sendToSecondAddressCheckBox').attr('checked', false);
                $('input').customInput();
                $('#refresh').click();
                $('#qr').attr('src', '/qr/qr?addr=<%out.print(address);%>');
            });
            </script>
        
<%@ include file="/WEB-INF/jsp/common/balanceJS.jsp" %>
<%@ include file="/WEB-INF/jsp/common/getBoxByIdJS.jsp" %>
<%@ include file="/WEB-INF/jsp/common/sendJS.jsp" %>
<%@ include file="/WEB-INF/jsp/common/signJS.jsp" %>
