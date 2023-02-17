import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*; 
import java.util.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import com.google.gson.*;
import javax.net.ssl.HttpsURLConnection;

public class OTPServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)throws IOException,ServletException{
		HttpSession session  = request.getSession();
		String received_otp = request.getParameter("otp"); 
		try{
			String session_otp = (String)session.getAttribute("OTP");
			if(received_otp.equals(session_otp)){
				response.sendRedirect("patient");
			}else{
				session.invalidate();
				response.sendRedirect("login");
			}
		}catch(Exception e){
			System.out.println(e);
		}
	}	
}