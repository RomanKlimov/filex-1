/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.admin_servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlet.adminDb.CapacityDAO;
import servlet.registration.db.UserDAO;
import servlet.registration.exceptions.DBException;
import servlet.registration.models.Key;
import servlet.registration.models.User;

/**
 *
 * @author User
 */
@WebServlet(name = "Admin", urlPatterns = {"/Admin"})
public class Admin extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DBException, SQLException {
        User user = (User) request.getSession().getAttribute("user");
        UserDAO dao = new UserDAO();
        CapacityDAO cap = new CapacityDAO();
        if (!dao.checkRole(user)) {
            response.sendRedirect("login");
        }
        ArrayList<User> users = dao.findAllUsers();
        String usersToReturn = "<table id=\"users\">\n";
        usersToReturn += "<tr>\n";
        usersToReturn += "<th>Name</th>\n";
        usersToReturn += "<th>Email</th>\n";
        usersToReturn += "<th>User`s capacity</th>\n";
        usersToReturn += "<th>Phone number</th>\n";
        usersToReturn += "<th>Admin rights</th>\n";
        usersToReturn += "<th>Delete user</th>\n";
        usersToReturn += "</tr>\n";
        for (int i = 0; i < users.size(); i++) {
            usersToReturn += "<tr>\n";
            usersToReturn += "<td>" + users.get(i).getName() + "</td>\n";
            usersToReturn += "<td>" + users.get(i).getEmail() + "</td>\n";
            usersToReturn += "<td>Current capacity: " + cap.getUsersCapacity(users.get(i))
                    + "\n<br>\nSet net capacity for " + users.get(i).getEmail() + "<form action=\"setUsersCapacity\" method=\"post\">\n"
                    + "<input name=\"email\" type=\"hidden\" value=\"" + users.get(i).getEmail() + "\">\n"
                    + "<input name=\"capacity\" type=\"number\" min=\"1\" max=\"10000\">\n"
                    + "<input type=\"submit\" class=\"file-path validate\" value=\"Set Capacity\">\n"
                    + "</form></td>\n";
            usersToReturn += "<td>" + users.get(i).getPhoneNumber() + "</td>\n";
            usersToReturn += "<td>" + users.get(i).getAdminRole() + "</td>\n";
            usersToReturn += "<td><form action=\"DeleteUser\" method=\"post\">\n"
                    + "                    <input name=\"toDelete\" type=\"hidden\" value=\"" + users.get(i).getEmail() + "\">\n"
                    + "                    <input type=\"submit\" class=\"file-path validate\" value=\"Delete this user\">\n"
                    + "            </form></td>\n";
            usersToReturn += "</tr>\n";
        }
        usersToReturn += "</table>\n";
        ArrayList<Key> keys = cap.getAllKeys();
        System.out.println(keys);
        String keysToReturn = "<table id=\"keys\">\n";
        keysToReturn += "<tr>\n";
        keysToReturn += "<th>Key</th>\n";
        keysToReturn += "<th>Value</th>\n";
        keysToReturn += "<th>Valid until</th>\n";
        keysToReturn += "<th>Set key to user</th>\n";
        keysToReturn += "</tr>\n";
        for (int i = 0; i < keys.size(); i++) {
            keysToReturn += "<tr>\n";
            keysToReturn += "<td>" + keys.get(i).getKey() + "</td>\n";
            keysToReturn += "<td>" + keys.get(i).getValue() + "</td>\n";
            keysToReturn += "<td>" + keys.get(i).getValidUntil() + "</td>\n";
            keysToReturn += "<td><form action=\"setKey\" method=\"post\">\n"
                    + "                <h4>Set key to user</h4>\n"
                    + "                <p>\n"
                    + "                    <input name=\"key\" type=\"hidden\" value=\"" + keys.get(i).getKey() + "\" required>\n"
                    + "                </p>\n"
                    + "                <p>\n"
                    + "                    <input name=\"user\" type=\"email\" maxlength=\"40\" placeholder=\"Enter email to set\" required>\n"
                    + "                </p>\n"
                    + "                <p>\n"
                    + "                    <input type=\"submit\" class=\"file-path validate\" value=\"Set\">\n"
                    + "                </p>\n"
                    + "            </form>\n"
                    + "</td>";
            keysToReturn += "</tr>\n";
        }
        keysToReturn += "</table>\n";
        request.setAttribute("keys", keysToReturn);
        request.setAttribute("users", usersToReturn);
        request.getRequestDispatcher("admin.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (DBException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (DBException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
