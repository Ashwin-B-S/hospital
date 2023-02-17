import Duo.LoginController;
import java.util.Base64.*;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import io.jsonwebtoken.*;
import javax.security.auth.login.LoginContext;
import java.security.*;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Auth extends HttpServlet{
	private static HashMap<String,ArrayList <String> > hm = new HashMap<>();
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)throws IOException,ServletException{
		String username = (String)req.getRemoteUser(),tokens="",type="";
		LoginController  lcobj = new LoginController();
		if(username!=null){
			HttpSession session = req.getSession();
			Data();
			try{
				for(Map.Entry <String,ArrayList <String> > x : hm.entrySet()){
					if(username.equals(x.getKey())){
						Class.forName("org.postgresql.Driver");
						Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hospital","postgres", "123456789");
						String url = lcobj.login(username),id = x.getValue().get(1);
						Statement smt = conn.createStatement();
						smt.execute("SELECT * FROM patient where id="+id+";");
						ResultSet rt = smt.getResultSet();
						if(rt.next()){
							type = "patient";
						}
						smt.execute("SELECT * FROM doctor where id="+id+";");
						rt = smt.getResultSet();
						if(rt.next()){
							type = "doctor";
						}
						smt.execute("SELECT * FROM admin where id="+id+";");
						rt = smt.getResultSet();
						if(rt.next()){
							type = "admin";
						}			
						tokens = TokenGenerator.generate(id,type);
						session.setAttribute("token",tokens);
						conn.close();
						res.sendRedirect(url);
					}
				}
				hm.clear();
			}catch(Exception e){
				System.out.println(e +" URLauth");
			}
		}else{
			System.out.println("nope");
		}
	}
	
	public static void Data(){
		Connection conn=null;
		try{
			Class.forName("org.postgresql.Driver");
        	conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hospital","postgres", "123456789");
			Statement stmt = conn.createStatement();
			stmt.execute("SELECT * FROM users;");
			ResultSet rst = stmt.getResultSet();
			while(rst.next()){
				ArrayList <String> al = new ArrayList<>();
				Decoder decoder = Base64.getDecoder();
				byte arr[] = decoder.decode(rst.getString("password"));
				al.add(new String(arr));
				al.add(rst.getString("id"));
				hm.put(rst.getString("username"),al);
			}
			conn.close();
		}catch(Exception e){
			System.out.println(e);
		}
	}
}