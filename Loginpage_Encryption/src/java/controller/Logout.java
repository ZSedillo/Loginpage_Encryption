/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.*;

/**
 *
 * @author Zandro
 */
public class Logout extends HttpServlet {   
    private String jdbcUrl;
    private String dbUsername;
    private String dbPassword;
    
    private String key;
    UpdatePasswords Ep = new UpdatePasswords();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
            HttpSession session = request.getSession();
            
            jdbcUrl = (String) getServletContext().getAttribute("jdbcUrl");
            dbUsername = (String) getServletContext().getAttribute("dbUsername");
            dbPassword = (String) getServletContext().getAttribute("dbPassword");
            key = (String) getServletContext().getAttribute("cipherTransformation");
            
            System.out.print(jdbcUrl); //For debugging
            System.out.print(key);
            Ep.decryptPasswords(jdbcUrl,dbUsername,dbPassword,key);
            
            session.invalidate();
            response.sendRedirect("index.jsp"); 
            
            
    }

}
