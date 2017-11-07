package servlet.registration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Register", urlPatterns = {"/Re"
    + "gister"})
public class RegistrationForm extends HttpServlet {

    private static final String CONTENT_TYPE = "text/html";
    private List<String> logins = new ArrayList<>();
    private List<String> emails = new ArrayList<>();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        try (PrintWriter rpw = response.getWriter()) {
            String login = request.getParameter("login");
            String password = request.getParameter("password");
            String rpassword = request.getParameter("rpassword");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String gender = request.getParameter("gender");
            String country = request.getParameter("country");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String distribution = request.getParameter("advertising");
            Cookie lCookie = new Cookie("login", login);
            Cookie eCookie = new Cookie("email", email);
            Cookie fnCookie = new Cookie("firstName", firstName);
            Cookie lnCookie = new Cookie("lastName", lastName);
            Cookie gCookie = new Cookie("gender", gender);
            Cookie cCookie = new Cookie("country", country);
            Cookie pCookie = new Cookie("phone", phone);
            Cookie dCookie = new Cookie("distribution", distribution);

            if (distribution == null) {
                distribution = "off";
            }
            String text = "error 500";
            ServletContext context = getServletContext();

            Scanner in = new Scanner(new File("C:\\upload"));
            logins.clear();
            emails.clear();
            while (in.hasNextLine()) {
                String str = in.nextLine();
                logins.add(str.substring(0, str.indexOf(',')));
                emails.add(str.substring(str.lastIndexOf(',') + 1, str.length()));
            }

            if (password.equals(rpassword) && phone.matches("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$") && login.matches("^[a-zA-Z][a-zA-Z0-9-_\\.]{1,20}$") && password.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*$")) {
                if (!logins.contains(login) && !emails.contains(email)) {
                    try (PrintWriter fpw = new PrintWriter(new BufferedWriter(new FileWriter(context.getResource("/WEB-INF/reg.csv").getFile(), true)))) {
                        response.addCookie(lCookie);
                        response.addCookie(eCookie);
                        response.addCookie(fnCookie);
                        response.addCookie(lnCookie);
                        response.addCookie(gCookie);
                        response.addCookie(cCookie);
                        response.addCookie(pCookie);
                        response.addCookie(dCookie);

                        fpw.println(login + "," + password + "," + firstName + ',' + lastName + "," + gender + "," + country + ',' + phone + ',' + distribution + ",," + email);
                        text = "REGISTERED!";
                    } catch (IOException ex) {
                        System.err.println(ex);
                    }
                } else if (logins.contains(login)) {
                    text = "User with this login is aready exist";
                } else if (emails.contains(email)) {
                    text = "User with this email is aready exist";
                }
            } else if (!password.equals(rpassword) || !password.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*$")) {
                text = "Wrong password!";
            } else if (!phone.matches("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")) {
                text = "Not a phone number!";
            } else if (!login.matches("^[a-zA-Z][a-zA-Z0-9-_\\.]{1,20}$")) {
                text = "Login must contain only a-Z, 0-9 and first have to be only letter";
            }

            rpw.println("<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "    <head>\n"
                    + "        <title>Registration Form</title>\n"
                    + "        <meta charset=\"UTF-8\">\n"
                    + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                    + "<script type=\"text/javascript\" src=\"jquery-latest.js\">\n"
                    + "</script>\n"
                    + "<script type=\"text/javascript\" src=\"jquery.tablesorter.js\">\n"
                    + "</script>\n"
                    + "    </head>\n"
                    + "    <body>\n"
                    + "        <div>\n"
                    + "            <form method=\"post\"\n"
                    + "                  action=\"/registration-servlet/register\"\n"
                    + "                  autocomplete=\"off\">\n"
                    + "                <p>\n<"
                    + "fieldset style=\"width: auto;\">\n"
                    + "                    <legend>Registration</legend>"
                    + "Login: <br> <input type=\"text\" name=\"login\" required value=\"" + login + "\"> <br>\n"
                    + "                    Password: <br> <input type=\"password\" name=\"password\" required value=\"" + password + "\"> <br>\n"
                    + "                    Repeat password: <br> <input type=\"password\" name=\"rpassword\" required value=\"" + rpassword + "\"> <br>\n"
                    + "                    First Name: <br> <input type=\"text\" name=\"firstName\" required value=\"" + firstName + "\"> <br>\n"
                    + "                    Last Name: <br> <input type=\"text\" name=\"lastName\" required value=\"" + lastName + "\"> <br>\n");
            if (gender.equals("Male")) {
                rpw.println("                    Gender: <br> <input name=\"gender\" type=\"radio\" value=\"Male\" required checked> Male\n"
                        + "                         <input name=\"gender\" type=\"radio\" value=\"Female\" required> Female <br>\n");
            } else {
                rpw.println("                    Gender: <br> <input name=\"gender\" type=\"radio\" value=\"Male\" required> Male\n"
                        + "                         <input name=\"gender\" type=\"radio\" value=\"Female\" required checked> Female <br>\n");
            }
            rpw.println("                    Country: <br> <select size=\"1\" name=\"country\" required>\n");
            if (country.equals("Russia")) {
                rpw.println("                                <option value=\"Russia\" selected>Russia</option>\n"
                        + "<option value=\"Japan\">Japan</option>\n");

            } else {
                rpw.println("                                <option value=\"Russia\">Russia</option>\n"
                        + "<option value=\"Japan\" selected>Japan</option>\n");
            }
            rpw.println("                            </select> <br>\n"
                    + "                    Telephone: <br> <input type=\"tel\" name=\"phone\" required value=\"" + phone + "\"> <br>\n"
                    + "                    Email: <br> <input type=\"email\" name=\"email\" required value=\"" + email + "\"> <br>\n");
            if (distribution.equals("on")) {
                rpw.println("Distribution of advertising: <input type=\"checkbox\" name=\"advertising\" value=\"on\" checked> <br> <br>\n");
            } else {
                rpw.println("Distribution of advertising: <input type=\"checkbox\" name=\"advertising\" value=\"on\"> <br> <br>\n");
            }
            rpw.println("                    <input type=\"submit\" value=\"Submit\">\n"
                    + "                    <input type=\"reset\" value=\"Reset\">\n"
                    + "<h1>" + text + "</h1>\n"
                    + "</fieldset>"
                    + "            </form>\n"
                    + "<fieldset style=\"width: auto;\">\n"
                    + "<script>\n"
                    + "$(document).ready(function(){\n"
                    + "  $(\"#regT\").tablesorter();\n"
                    + "});\n"
                    + "</script>\n"
                    + "<legend>Emails</legend>\n"
                    + "<table id=\"regT\" class=\"tablesorter\">\n"
                    + "<thead>\n"
                    + "<tr>\n"
                    + "<th>login</th>\n"
                    + "<th>email</th>\n"
                    + "</tr>\n"
                    + "</thead>\n"
                    + "<tbody>\n");
            Cookie[] cookies = request.getCookies();
            String loginCookies = "";
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("login")) {
                        loginCookies = cookie.getValue();
                    }
                }
            }
            for (int i = 0; i <= logins.size(); i++) {
                if (logins.get(i).equals(loginCookies)) {
                    rpw.println("<tr><td>Your login + email:</td></tr>");
                }

                rpw.println("<tr>\n<td>" + logins.get(i) + "</td>\n");
                rpw.println("<td>" + emails.get(i) + "</td>\n</tr>\n");
            }

            rpw.println("</tbody>\n</table>\n"
                    + "</fieldset>\n"
                    + "        </div>\n"
                    + "    </body>\n"
                    + "</html>\n");
        }
    }
}
