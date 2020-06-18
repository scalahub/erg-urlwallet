package org.UrlWallet

import java.awt.{Color, Font, Graphics2D}
import java.awt.image.BufferedImage
import java.util.{Random => JRandom}

import javax.imageio.ImageIO;
import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse};

class CaptchaServlet extends HttpServlet {
    def processRequest(request:HttpServletRequest, response:HttpServletResponse)={
        response.setContentType("image/jpg");
        /*
         * Define number characters contains the captcha image, declare global
         */
        val iTotalChars = 6;

        /*
         * Size image iHeight and iWidth, declare globl
         */
        val iHeight = 40;
        val iWidth = 150;

        /*
         * font style
         */
        val fntStyle1 = new Font("Arial", Font.BOLD, 30);
        val fntStyle2 = new Font("Verdana", Font.BOLD, 20);

        /*
         * Possible random characters in the image
         */
        val randChars = new JRandom();
        val sImageCode = randChars.nextLong().abs.toString.substring(0, iTotalChars);

        /*
         * BufferedImage is used to create a create new image
         */
        /*
         * TYPE_INT_RGB - does not support transpatency, TYPE_INT_ARGB - support transpatency
         */
        val biImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_INT_RGB);
        val g2dImage = biImage.getGraphics().asInstanceOf[Graphics2D];

        // Draw background rectangle and noisey filled round rectangles
        val iCircle = 15;
        for (i <- 0 to iCircle-1) {
            g2dImage.setColor(new Color(randChars.nextInt(255), randChars.nextInt(255), randChars.nextInt(255)));
            val iRadius = (Math.random() * iHeight / 2.0);
            val iX =  (Math.random() * iWidth - iRadius);
            val iY =  (Math.random() * iHeight - iRadius);
        }
        g2dImage.setFont(fntStyle1);
        for (i <- 0 to iTotalChars-1) {
            g2dImage.setColor(new Color(randChars.nextInt(255), randChars.nextInt(255), randChars.nextInt(255)));
            if (i % 2 == 0) {
                g2dImage.drawString(sImageCode.substring(i, i + 1), 25 * i, 24);
            } else {
                g2dImage.drawString(sImageCode.substring(i, i + 1), 25 * i, 35);
            }
        }

        /*
         * create jpeg image and display on the screen
         */
        val osImage = response.getOutputStream();
        ImageIO.write(biImage, "jpeg", osImage);
        // osImage.close();

        /*
         * Dispose function is used destroy an image object
         */
        g2dImage.dispose();
        val session = request.getSession();
        session.setAttribute("sessionCaptcha", sImageCode);
    }
    override def doGet(req:HttpServletRequest, resp:HttpServletResponse) {
        processRequest(req, resp);
    }
}
