/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.registration.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlet.registration.db.FeedbackDAO;
import servlet.registration.exceptions.AlreadyExistException;
import servlet.registration.exceptions.DBException;

@WebServlet(name = "contactUs", urlPatterns = {"/contactUs"})
public class ContactUs extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String message = request.getParameter("message");
        FeedbackDAO dao = new FeedbackDAO();
        try {
            dao.addFeedback(firstName, lastName, email, message);
        } catch (DBException ex) {
            Logger.getLogger(ContactUs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AlreadyExistException ex) {
            Logger.getLogger(ContactUs.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.getRequestDispatcher("index.html").forward(request, response);
    }

}
