package org.UrlWallet

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class QRServlet extends HttpServlet {
	val useLocalQR = false
	def processRequest(request:HttpServletRequest, response:HttpServletResponse) = {
		val address=request.getParameter("addr")
		if (address != null) {
			response.setContentType("image/png")
			val out = response.getOutputStream
			import net.glxn.qrgen.QRCode;
			import net.glxn.qrgen.image.ImageType;
			val img = QRCode.from(address).to(ImageType.PNG).withSize(200, 200).stream
			out.write(img.toByteArray)
		}
	}
	override def doGet(req:HttpServletRequest, resp:HttpServletResponse) {
			processRequest(req, resp);
	}
}
