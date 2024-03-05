package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class Logout extends HttpServlet {   
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
            HttpSession session = request.getSession();
            session.invalidate();
            response.sendRedirect("index.jsp"); 
    }

}
