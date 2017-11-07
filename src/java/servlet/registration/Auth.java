/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.registration;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

/**
 *
 * @author User
 */
@WebServlet(name = "Auth", urlPatterns = {"/Auth"})
public class Auth extends HttpServlet {

    private static final String CONTENT_TYPE = "text/html";
    private List<String[]> base = new ArrayList<>();
    private List<String> passwords = new ArrayList<>();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        try (PrintWriter rpw = response.getWriter()) {
            String login = request.getParameter("login");
            String password = request.getParameter("password");
            String remember = request.getParameter("remember");
            if (remember == null) {
                remember = "off";
            }
            Cookie lCookie = new Cookie("login", login);
            if (remember.equals("on")) {
                response.addCookie(lCookie);
            }
            ServletContext context = getServletContext();
            Scanner in = new Scanner(new File("C:\\upload"));
            while (in.hasNextLine()) {
                String str = in.nextLine();
                base.add(str.split(","));

            }
            String text = "Cannot login!";
            String titleText = "Cannot auth";
            HttpSession session = request.getSession();
            try {
                if (!session.getAttribute("login").equals(null)) {
                    login = (String) session.getAttribute("login");
                    password = (String) session.getAttribute("password");
                }
            } catch (NullPointerException ex) {
            }
            for (int i = 0; i < base.size(); i++) {
                if (login.equals(base.get(i)[0]) && password.equals(base.get(i)[1])) {
                    text = "<h1>You logged as " + base.get(i)[2] + " " + base.get(i)[3] + " throw login+password\n";
                    titleText = "Auth-ed by " + login;
                    session.setAttribute("login", login);
                    session.setAttribute("password", password);
                }
            }
            rpw.println("<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "    <head>\n"
                    + "        <title>" + titleText + "</title>\n"
                    + "        <meta charset=\"UTF-8\">\n"
                    + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                    + "    </head>\n"
                    + "    <body>\n");
            rpw.println(text + "<body>\n"
                    + "    </body>\n"
                    + "</html>\n");

        }
    }
}
