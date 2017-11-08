package servlet.registration.servlets;

import servlet.registration.db.UserDAO;
import servlet.registration.exceptions.DBException;
import servlet.registration.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "login", urlPatterns = {"/login"})
public class Login extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            req.getSession().setAttribute("goTo", "E:\\" + user.getFolder());
            req.getSession().setAttribute("message", "You logged as " + user.getEmail().toString());
            getServletContext().getRequestDispatcher("/message.jsp").forward(
                    req, resp);
        } else {
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
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
                        req.getSession().setAttribute("message", "You logged as " + user.getEmail());
                        getServletContext().getRequestDispatcher("/message.jsp").forward(
                                req, resp);
                    }
                } else {
                    req.setAttribute("wrong", "User not found");
                    doGet(req, resp);
                }
            } catch (DBException e) {
                req.setAttribute("wrong", "Database Error");
                doGet(req, resp);
            }
        }
    }

    private User getUser(String email, String password) throws DBException {
        UserDAO repository = new UserDAO();
        return repository.findByEmail(email, password);
    }
}
