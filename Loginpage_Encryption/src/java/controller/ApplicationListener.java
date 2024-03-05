package controller;

import java.util.Date;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ApplicationListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext listener = sce.getServletContext();
        Date currentTime = new Date();
        listener.setAttribute("time", currentTime);
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }
}
