/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.admin_servlets;

import servlet.admin_servlets.ChangeUserRole;
import java.io.IOException;
import java.io.PrintWriter;
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

/**
 *
 * @author User
 */
@WebServlet(name = "setUsersCapacity", urlPatterns = {"/setUsersCapacity"})
public class setUsersCapacity extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        Integer capacity =Integer.valueOf(request.getParameter("capacity"));
        CapacityDAO dao = new CapacityDAO();

        try {
            dao.setCapacity(email, capacity);
        } catch (DBException ex) {
            Logger.getLogger(ChangeUserRole.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.sendRedirect("Admin");
    }
}
