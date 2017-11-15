package servlet;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class FileLocationContextListener implements ServletContextListener {

    private String rootPath = "E:\\upload";

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        /*       User user = (User) servletContextEvent.getServletContext().getAttribute("user");
        this.rootPath = String.valueOf(user.getFolder());*/
        ServletContext ctx = servletContextEvent.getServletContext();

    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //do cleanup if needed
    }

    public String getPath() {
        return rootPath;
    }

}
