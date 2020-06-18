
            <form id="csrf" method="post">
                <input name="csrf" type="hidden" value="<%out.print(session.getAttribute("csrf"));%>">
            </form>
