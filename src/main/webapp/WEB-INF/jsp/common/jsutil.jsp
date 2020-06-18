
        <script>
            function showText(show,hide){
                document.getElementById(show).className = "show";
                document.getElementById(hide).className = "hide";
            }
            function show(show){
                document.getElementById(show).className = "show";
            }
            function hide(hide){
                document.getElementById(hide).className = "hide";
            } 
        </script>
        <div id='target'></div>
        <DIV id='PopUp' style='display: none; position: absolute; left: 100px; top: 50px; border: solid black 1px; padding: 5px; background-color: White; color:black; text-align: left; font-size: 10px; width: 135px; z-index: 10000' onmouseover="document.getElementById('PopUp').style.display = 'none' ">
        <SPAN id='PopUpText'>TEXT</SPAN>
        </DIV>
