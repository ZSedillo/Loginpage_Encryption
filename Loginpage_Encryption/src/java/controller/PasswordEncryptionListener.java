//package controller;
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//import javax.servlet.ServletContext;
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//import javax.servlet.annotation.WebListener;
//
//@WebListener
//public class PasswordEncryptionListener implements ServletContextListener {
//
//    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//        ServletContext context = sce.getServletContext();
//        EncryptPasswords.encryptPasswords(context); 
//    }
//
//    @Override
//    public void contextDestroyed(ServletContextEvent sce) {
//
//    }
//}

package controller;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class PasswordEncryptionListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        EncryptPasswords encryptor = new EncryptPasswords();
//    encryptor.encryptPasswords(); 
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}