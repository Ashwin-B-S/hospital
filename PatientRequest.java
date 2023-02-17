import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import javax.mail.*;
import javax.servlet.*;
import io.jsonwebtoken.*;
import javax.servlet.http.*;
import javax.mail.internet.*;
import javax.security.auth.*;

public class PatientRequest extends HttpServlet {
	static String name, age ,disease ,condition ,doc_name ,id,pat_id;
	public void doPost(HttpServletRequest req, HttpServletResponse res)throws IOException,ServletException{
		PrintWriter out = res.getWriter();
		HttpSession session = req.getSession();
		String new_doc=req.getParameter("newdoc"),old_doc=req.getParameter("olddoc");
		pat_id = (String)session.getAttribute("id");
		Connection conn = null;
      		try {
         		Class.forName("org.postgresql.Driver");
         		conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hospital","postgres", "123456789");
				Statement stmt = null;
				stmt=conn.createStatement();
				stmt.execute("Insert INTO request VALUES("+pat_id+","+old_doc+","+new_doc+");");
				stmt.execute("SELECT * FROM doctor WHERE id = "+new_doc+";");
				ResultSet rst = stmt.getResultSet();
				if(rst.next()){
					id=new_doc;
					Statement smt = conn.createStatement();
					smt.execute("SELECT * FROM details WHERE id="+id+";");
					ResultSet rt = smt.getResultSet();
					if(rt.next()){
						doc_name=rt.getString("name");
					}
					smt.execute("SELECT * FROM details WHERE id="+pat_id+";");
					rt = smt.getResultSet();
					if(rt.next()){
						name=rt.getString("name");
						age=rt.getString("age");
					}
					smt.execute("SELECT * FROM patient WHERE id="+pat_id+";");
					rt = smt.getResultSet();
					if(rt.next()){
						disease=rt.getString("disease");
						condition=rt.getString("condition");
					}
					String doc_email=rst.getString("email");
					sendMail(doc_email);
				}
				conn.close();
				res.sendRedirect("patient");
      		} catch ( Exception e ) {
         		System.err.println(e.getClass().getName()+": "+ e.getMessage());
      		}
	}
	public static void sendMail(String recepient){

		Properties properties = new Properties();
		properties.put("mail.smtp.auth","true");
		properties.put("mail.smtp.starttls.enable","true");
		properties.put("mail.smtp.host","smtp.gmail.com");
		properties.put("mail.smtp.port","587");
		
		String username = "19ecashwinbs@drngpit.ac.in";
		String password = "Ashwin@006";
		
		Session session = Session.getInstance(properties,new javax.mail.Authenticator(){
			
			protected javax.mail.PasswordAuthentication getPasswordAuthentication(){
				return new javax.mail.PasswordAuthentication(username,password);
			}

		});
		
		Message message = prepareMessage(session,username,recepient);

		try{
			Transport.send(message);
		}catch(Exception e){
			e.printStackTrace();
		}

		System.out.println("Email sent");
	}
	
	private static Message prepareMessage(Session session,String username,String recepient){
		Message message = new MimeMessage(session);

		long millisec = System.currentTimeMillis();
		long expsec = millisec+(60*1000*30);
		
		java.util.Date dateIss = new java.util.Date(millisec);
		java.util.Date dateExp = new java.util.Date(expsec);
		String sec="HGSdbbwhsknw17234teg2sbITEU@TZf8duj3029zie2093874hds!@#$%^hbwgue897*J82YHSBN2IHFBD";
		Claims claims = Jwts.claims().setIssuer(id).setExpiration(dateExp);
		claims.put("pat_id",pat_id);
		String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512,sec).compact();
		try{
			message.setFrom(new InternetAddress(username));
			message.setRecipient(Message.RecipientType.TO,new InternetAddress(recepient));
			message.setSubject("Patient Request");
			String strline1 = "Hello, Dr."+doc_name+"<br>This is a Request mail requesting you to be his/her doctor, patient details is given below:";
			String strline2 = "<br>Name : "+name+"<br>Age : "+age+"<br>Disease : "+disease+"<br>Condition : "+condition;
			String strline3 = "<br>click <a href=\"http://localhost:8080/hospital/Request.jsp?tkn="+token+"\">here</a> to accept/deny request!";
			String strline4 = "<br>Thank You!<br>Have a great day!";
			message.setContent(strline1+strline2+strline3+strline4,"text/html");
		}catch(Exception e){
			e.printStackTrace();
		}
		return message;
	}	
}