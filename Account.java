import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.google.gson.*;

public class Account extends HttpServlet {
	
	HashMap <String,Object> res = new HashMap<>();
	
	public void doGet(HttpServletRequest request ,HttpServletResponse response) throws IOException, ServletException {
		
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		try{
			Class.forName("org.postgresql.Driver");
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/hospital","postgres","123456789");
			Statement stm = conn.createStatement();
			res.put("id",id);
			stm.execute("select * from users where id = "+id+";");
			ResultSet rst = stm.getResultSet();
			if(rst.next()){
				res.put("Username",rst.getString("username"));
				res.put("hasUsername",true);
			}else{
				res.put("hasUsername",false);
				stm.execute("select * from users;");
				ResultSet rest = stm.getResultSet();
				HashSet <String> hm = new HashSet<>();
				while(rest.next()){
					hm.add(rest.getString("username"));
				}
				res.put("Username",hm);
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.write(new Gson().toJson(res));
			conn.close();
		}catch(Exception e){
			System.out.println(e+" #Account#");
		}
		
	}
}