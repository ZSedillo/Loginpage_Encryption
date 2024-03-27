/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author Zandro
 */
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
