
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.google.gson.Gson;

public class AdminData extends HttpServlet {
	
	HashMap <String,Object> res = new HashMap<>();
	
	public void doGet(HttpServletRequest request ,HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		try{
			Class.forName("org.postgresql.Driver");
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/hospital","postgres","123456789");
			Statement stm = conn.createStatement();
			stm.execute("select name from details where id = "+id+";");
			ResultSet rst = stm.getResultSet();
			if(rst.next())res.put("name",rst.getString("name"));
			stm.execute("select * from details d ,patient p where p.id=d.id;");
			rst = stm.getResultSet();
			HashSet<HashMap<String,Object>> hs = new HashSet<>();
			while(rst.next()){
				HashMap <String,Object> hm = new HashMap<>();
				hm.put("name",rst.getString("name"));
				hm.put("age",rst.getString("age"));
				if(rst.getString("disease").equals("Covid-19"))hm.put("hasCovid",true);
				else hm.put("hasCovid",false);
				hm.put("condition",rst.getString("condition"));
				hs.add(hm);
			}
			res.put("Patients",hs);
			stm.execute("select * from doctor dr,details d where dr.id = d.id;");
			rst = stm.getResultSet();
			HashSet <HashMap<String,Object>> hm = new HashSet<>();
			int i=1;
			while(rst.next()){
				HashMap<String,Object> hsm = new HashMap<>(); 
				hsm.put("name",rst.getString("name"));
				hsm.put("spec",rst.getString("specialization"));
				hsm.put("exp",rst.getString("exp"));
				hm.add(hsm);
			}
			res.put("Doctors",hm);
			stm.execute("select * from users where id = "+id+";");
			rst = stm.getResultSet();
			res.put("hasUsername",rst.next());
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.write(new Gson().toJson(res));
			conn.close();
		}catch(Exception e){
			System.out.println(e+" #Admindata#");
		}
	}
}