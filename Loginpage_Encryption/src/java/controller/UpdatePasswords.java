package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServlet;

public class UpdatePasswords extends HttpServlet{
    
    public void encryptPasswords(String jdbcUrl, String dbUsername, String dbPassword,String cipherTransformation) throws NullPointerException{
            try {
            Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);

            String query = "SELECT password FROM USER_INFO";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            

            while (rs.next()) {
                Security security = new Security();
                String password = rs.getString("password");
                String setPassword = security.encrypt(password,cipherTransformation); //Culprit
                
                String updateQuery = "UPDATE USER_INFO SET password = ? WHERE password = ?";
                PreparedStatement updatePs = con.prepareStatement(updateQuery);
                updatePs.setString(1, setPassword);
                updatePs.setString(2, password);
                updatePs.executeUpdate();
                updatePs.close();
                
            }
            rs.close();
            ps.close();
            con.close();
            System.out.println("Encryption Successful");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
        public void decryptPasswords(String jdbcUrl, String dbUsername, String dbPassword, String cipherTransformation) throws NullPointerException {
        try {
            Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);

            String query = "SELECT password FROM USER_INFO";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Security security = new Security();
                String encryptedPassword = rs.getString("password");
                String decryptedPassword = security.decrypt(encryptedPassword, cipherTransformation);

                // Update the password with decrypted value
                String updateQuery = "UPDATE USER_INFO SET password = ? WHERE password = ?";
                PreparedStatement updatePs = con.prepareStatement(updateQuery);
                updatePs.setString(1, decryptedPassword);
                updatePs.setString(2, encryptedPassword);
                updatePs.executeUpdate();
                updatePs.close();
                System.out.println("Encrypted Password: " + encryptedPassword + " Decrypted Version: " + decryptedPassword); //For Checking
            }
            rs.close();
            ps.close();
            con.close();
            System.out.println("Decryption Successful");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
