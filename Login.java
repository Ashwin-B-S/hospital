import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*; 
import java.util.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import com.google.gson.*;
import javax.net.ssl.HttpsURLConnection;

public class Login extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)throws IOException,ServletException{
		HttpSession session  = request.getSession();
		String number = request.getParameter("mobile"); 
		try{
			Class.forName("org.postgresql.Driver");
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/hospital","postgres","123456789");
			Statement stm = conn.createStatement();
			stm.execute("select * from details where number = '"+number+"';");
			ResultSet rst = stm.getResultSet();
			Random rand = new Random();
			int num = rand.nextInt(9999);
			while(num<1000){
				num=rand.nextInt(9999);
			}
			if(rst.next()){
				session.setAttribute("OTP","1234");
				Statement stmt = conn.createStatement();
				String id = rst.getString("id"),type="";
				stmt.execute("select * from patient where id = '"+id+"';");
				ResultSet rest = stmt.getResultSet();
				if(rest.next()){
					type = "patient";
				}
				stmt.execute("select * from doctor where id = '"+id+"';");
				rest = stmt.getResultSet();
				if(rest.next()){
					type = "doctor";
				}
				stmt.execute("select * from admin where id = '"+id+"';");
				rest = stmt.getResultSet();
				if(rest.next()){
					type = "admin";
				}
				String token = TokenGenerator.generate(id,type);
				System.out.println(id+"  "+type);
				session.setAttribute("token",token);
				//sendSms(number,String.valueOf(num));
				response.sendRedirect("OTP");
			}else{
				response.sendRedirect("login");
			}
			conn.close();
		}catch(Exception e){
			System.out.println(e);
		}
	}
	public static void sendSms(String number,String num){
		number="9789539218";
		String message=num+" is your One Time Password to login";
		try{
			String apiKey="tLdkhpmxWGb38B6foZD0PzySAnVJOHjegRUIu451swMKalTEi7ibY5wZsSfp3k8QUJhNOXVnTFjGEMlg";
			String sendId="FSTSMS";
			message=URLEncoder.encode(message, "UTF-8");
			String language="english";
			String route="p";
			String myUrl="https://www.fast2sms.com/dev/bulk?authorization="+apiKey+"&sender_id="+sendId+"&message="+message+"&language="+language+"&route="+route+"&numbers="+number;
			URL url=new URL(myUrl);
			HttpsURLConnection con=(HttpsURLConnection)url.openConnection();		
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("cache-control", "no-cache");
			int code=con.getResponseCode();	
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
}