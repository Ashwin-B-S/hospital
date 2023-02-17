
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.google.gson.Gson;

public class DocData extends HttpServlet {
	
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
			if(rst.next()){
				res.put("name",rst.getString("name"));
				res.put("id",id);
			}
			stm.execute("select * from details d ,patient p where p.id=d.id and p.doctor_id = "+id+";");
			rst = stm.getResultSet();
			HashSet<HashMap<String,Object>> hs = new HashSet<>();
			while(rst.next()){
				HashMap <String,Object> hm = new HashMap<>();
				hm.put("name",rst.getString("name"));
				hm.put("age",rst.getString("age"));
				hm.put("id",rst.getString("id"));
				hm.put("disease",rst.getString("disease"));
				hm.put("condition",rst.getString("Condition"));
				if(rst.getString("disease").equals("Covid-19"))hm.put("hasCovid",true);
				else hm.put("hasCovid",false);
				hs.add(hm);
			}
			res.put("patients",hs);
			stm.execute("select * from details d ,patient p where p.id=d.id and p.id in (select pat_id from request where newdoc_id = "+id+");");
			rst = stm.getResultSet();
			HashSet<HashMap<String,Object>> hs1 = new HashSet<>();
			while(rst.next()){
				HashMap <String,Object> hm = new HashMap<>();
				hm.put("name",rst.getString("name"));
				hm.put("age",rst.getString("age"));
				hm.put("disease",rst.getString("disease"));
				hm.put("id",rst.getString("id"));
				hs1.add(hm);
			}
			res.put("requests",hs1);
			if(hs1.size()>0)res.put("hasRequests",true);
			else res.put("hasRequests",false);
			stm.execute("select * from users where id = "+id+";");
			rst = stm.getResultSet();
			res.put("hasUsername",rst.next());
			stm.execute("update chat set status = 400 where receiver = '"+id+"' and status != 400 ;");
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.write(new Gson().toJson(res));
			conn.close();
		}catch(Exception e){
			System.out.println(e+" #Docdata#");
		}
	}
}