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

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.*;

public class LoginServlet extends HttpServlet{
    private String jdbcUrl;
    private String dbUsername;
    private String dbPassword;
    private String cipherTransformation;
    	private static byte[] key = {
	 	0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 
                0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
	};

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        jdbcUrl = config.getInitParameter("jdbcUrl");
        dbUsername = config.getInitParameter("dbUsername");
        dbPassword = config.getInitParameter("dbPassword");
        
        //New
        cipherTransformation = config.getServletContext().getInitParameter("cipherTransformation");
        String keyString = config.getServletContext().getInitParameter("secretKey");
        key = keyString.getBytes();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
            if (username.isEmpty() && password.isEmpty()) {               
                throw new NullPointerException("No login credentials provided");
            }
            else if (username.isEmpty() && !password.isEmpty()){
                request.getSession().setAttribute("password", password);
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
                            response.sendRedirect("error_6.jsp");
                        } else {
                            // Username does not exist
                            request.getSession().setAttribute("username", username);
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
                            userPasswordDB = resultSet.getString("password");
                            
                            //Set encryptedPassword
                            String encryptedPasswordDB = encrypt(userPasswordDB);
                            request.getSession().setAttribute("encryptedPassword", encryptedPasswordDB);
                            
                            // Decrypt password from database
                            //String decryptedPasswordDB = decrypt(encryptedPasswordDB);

                            if (userPasswordDB.equals(password)) {
                                // Correct username and password
                                request.getSession().setAttribute("username", username);
                                request.getSession().setAttribute("role", resultSet.getString("role"));
                                response.sendRedirect("success.jsp");
                            } else {
                                // Incorrect password
                                request.getSession().setAttribute("username", username);
                                response.sendRedirect("error_2.jsp");
                            }
                        } else {
                            // Username does not exist
                            request.getSession().setAttribute("username", username);
                            request.getSession().setAttribute("password", password);
                            response.sendRedirect("error_3.jsp");
                        }
                    }
                }
            }
        } catch (SQLException e) {    
        response.sendRedirect("error_4.jsp");
        } catch (NullPointerException ex) {
            // No login credentials provided
            request.getSession().setAttribute("username","hi");
            request.getSession().setAttribute("password", "and no"); 
            response.sendRedirect("noLoginCredentials.jsp");
        } 
    }
    
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
    
// Encrypt method
    public String encrypt(String strToEncrypt) {
        String encryptedString = null;
        try {
            Cipher cipher = Cipher.getInstance(cipherTransformation);
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encryptedString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return encryptedString;
    }

    // Decrypt method
    public String decrypt(String codeDecrypt) {
        String decryptedString = null;
        try {
            Cipher cipher = Cipher.getInstance(cipherTransformation);
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            decryptedString = new String(cipher.doFinal(Base64.decodeBase64(codeDecrypt)));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return decryptedString;
    }
}