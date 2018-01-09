/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.admin_servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlet.adminDb.CapacityDAO;
import servlet.registration.exceptions.AlreadyExistException;
import servlet.registration.exceptions.DBException;

/**
 *
 * @author User
 */
@WebServlet(name = "generateKeys", urlPatterns = {"/generateKeys"})
public class generateKeys extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer value = Integer.valueOf(request.getParameter("value"));
        Integer count = Integer.valueOf(request.getParameter("count"));
        System.out.println(request.getParameter("timestamp"));
        Date date = Date.valueOf(request.getParameter("timestamp"));
        CapacityDAO dao = new CapacityDAO();
        for (int i = 0; i < count; i++) {
            try {
                dao.addKeys(UUID.randomUUID().toString(), value, date);

            } catch (DBException ex) {
                Logger.getLogger(generateKeys.class.getName()).log(Level.SEVERE, null, ex);
            } catch (AlreadyExistException ex) {
                Logger.getLogger(generateKeys.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        request.getRequestDispatcher("Admin").forward(request, response);
    }

}
