package servlet.registration.servlets;

import Encription.Encript;
import servlet.registration.db.UserDAO;
import servlet.registration.exceptions.DBException;
import servlet.registration.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

@WebServlet(name = "login", urlPatterns = {"/login"})
public class Login extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user;

        user = (User) req.getSession().getAttribute("user");
        
        if (user != null) {
            req.getSession().setAttribute("goTo", "E:\\" + user.getFolder());    
            session.setAttribute("user", user);
            getServletContext().getRequestDispatcher("/Folder").forward(
                    req, resp);
        } else {
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email != null && password != null) {
            try {
                User user = getUser(email, password);
                if (user != null) {
                    req.getSession().setAttribute("user", user);
                    if (user.getEmail().equals("admin") && user.getPassword().equals("admin")) {
                        resp.sendRedirect("/admin");
                    } else {
                        req.getSession().setAttribute("goTo", "E:\\" + user.getFolder());
                        getServletContext().getRequestDispatcher("/Folder").forward(
                                req, resp);
                    }   
                } else {
                    req.getSession().setAttribute("email", email);
                    req.getSession().setAttribute("wrong", "User not found");
                    doGet(req, resp);
                }
            } catch (DBException e) {
                req.setAttribute("wrong", "Database Error");
                doGet(req, resp);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private User getUser(String email, String password) throws DBException, NoSuchAlgorithmException {
        UserDAO repository = new UserDAO();
        Encript encript = new Encript();
        return repository.findByEmail(email, encript.encript(password));
    }
}
