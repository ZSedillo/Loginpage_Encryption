package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import controller.Security;
import controller.CaptchaServlet;

public class LoginServlet extends HttpServlet{
    private String jdbcUrl;
    private String dbUsername;
    private String dbPassword;
    
    private String key;
    private int increment = 0;
    
    UpdatePasswords Ep = new UpdatePasswords();
    @Override
    public void init(ServletConfig config) throws ServletException {
            super.init(config);
            jdbcUrl = config.getInitParameter("jdbcUrl");
            dbUsername = config.getInitParameter("dbUsername");
            dbPassword = config.getInitParameter("dbPassword");
            key = config.getInitParameter("cipherTransformation");
            
            
            getServletContext().setAttribute("jdbcUrl", jdbcUrl);
            getServletContext().setAttribute("dbUsername", dbUsername);
            getServletContext().setAttribute("dbPassword", dbPassword);
            getServletContext().setAttribute("cipherTransformation", key);
            Ep.encryptPasswords(jdbcUrl, dbUsername, dbPassword, key);       
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        //Prevent Double encryption
        if(increment == 1){
            Ep.encryptPasswords(jdbcUrl, dbUsername, dbPassword, key);
            increment--;
        }
        increment++;
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        request.getSession().setAttribute("usernamePrev", username);
        request.getSession().setAttribute("passwordPrev", password);
        
        String captchaUserInput = request.getParameter("captcha");
        String captchaGenerated = (String) request.getSession().getAttribute("captchaGenerated");

        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
            if (username.isEmpty() && password.isEmpty()) {
                System.out.println("I AM HERE 1");
                decryptPasswords();
                throw new NullPointerException("No login credentials provided");
            }
            else if (username.isEmpty() && !password.isEmpty()){
                request.getSession().setAttribute("password", password);
                System.out.println("I AM HERE 2");
                decryptPasswords();
                response.sendRedirect("error_5.jsp");
            }
            else if (!username.isEmpty() && password.isEmpty()) {
                String sql = "SELECT * FROM USER_INFO WHERE username = ?";

                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setString(1, username);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            // Username exists, but password is blank
                            request.getSession().setAttribute("username", username);
                            System.out.println("I AM HERE 3");
                            decryptPasswords();
                            response.sendRedirect("error_6.jsp");
                        } else {
                            // Username does not exist
                            request.getSession().setAttribute("username", username);
                            System.out.println("I AM HERE 4");
                            decryptPasswords();
                            response.sendRedirect("error_1.jsp");
                        }
                    }
                }
            } 
            else if (!username.isEmpty() && !password.isEmpty()) {                
                String sql = "SELECT * FROM USER_INFO WHERE username = ?";

                String userNameDB, userPasswordDB;
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setString(1, username);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        // Username provided
                        if (resultSet.next()) {
                            userNameDB = resultSet.getString("username");
                            userPasswordDB = Security.decrypt(resultSet.getString("password"),key); //Decrypt specific password and login
                            if (userPasswordDB.equals(password)) {
                                if(captchaUserInput.isEmpty()){
                                    decryptPasswords();
                                    response.sendRedirect("index.jsp");
                                }
                                else if(!CaptchaServlet.checkCaptcha(captchaGenerated, captchaUserInput)){
                                    decryptPasswords();  
                                    response.sendRedirect("index.jsp");
                                }
                                else if(captchaUserInput.equals(captchaGenerated)){
                                    request.getSession().setAttribute("username", username);
                                    request.getSession().setAttribute("role", resultSet.getString("role"));
                                    response.sendRedirect("success.jsp");                                
                                }
                                
                                
                                
                                    //ORIG CODE
                                    // Correct username and password
//                                    request.getSession().setAttribute("username", username);
//                                    request.getSession().setAttribute("role", resultSet.getString("role"));
//                                    response.sendRedirect("success.jsp");                                
                            } else {
                                // Incorrect password
                                request.getSession().setAttribute("username", username);
                                System.out.println("I AM HERE 5");
                                decryptPasswords();
                                response.sendRedirect("error_2.jsp");
                            }
                        } else {
                            // Username does not exist
                            request.getSession().setAttribute("username", username);
                            request.getSession().setAttribute("password", password);
                            System.out.println("I AM HERE 6");
                            decryptPasswords();
                            response.sendRedirect("error_3.jsp");
                        }
                    }
                }
            }
        } catch (SQLException e) {    
            System.out.println("I AM HERE 7");
            response.sendRedirect("error_4.jsp");
        } 
        catch (NullPointerException ex) {
            // No login credentials provided
            request.getSession().setAttribute("username","hi");
            request.getSession().setAttribute("password", "and no"); 
            System.out.println("I AM HERE 8");
            response.sendRedirect("noLoginCredentials.jsp");
        } 
    }
    
    //TO BE CHECKED
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Incorrect context path
            String requestURI = request.getRequestURI();
    if (requestURI.equals(request.getContextPath() + "/")) {
        // Request is for the root URL
        response.sendRedirect("error_4.jsp");
    } else {
        // Incorrect context path
        response.sendRedirect("error_4.jsp");
    }
    }
    
    public void decryptPasswords() {
        System.out.print("I AM HERE LETS GOO");
        Ep.decryptPasswords(jdbcUrl, dbUsername, dbPassword, key);
    }
    
    private void captchaChecker(String captchaGenerated, String captchaInputted, String username, HttpServletRequest request, HttpServletResponse response, ResultSet resultSet) throws ServletException, IOException, SQLException {
        System.out.println("Checking Captcha");
        if (captchaInputted.equals(captchaGenerated)) {
            request.getSession().setAttribute("username", username);
            if (resultSet.next()) {
                request.getSession().setAttribute("role", resultSet.getString("role"));
            }
            response.sendRedirect("success.jsp");
        }
    }
    
    
}