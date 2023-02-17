import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.google.gson.*;

public class Patient extends HttpServlet {
	
	HashMap <String,Object> res = new HashMap<>();
	
	public void doGet(HttpServletRequest request ,HttpServletResponse response) throws IOException, ServletException {
		
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		try{
			Class.forName("org.postgresql.Driver");
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/hospital","postgres","123456789");
			Statement stm = conn.createStatement();
			stm.execute("select * from patient p,details d where p.id = "+id+" and d.id="+id+";");
			ResultSet rst = stm.getResultSet();
			if(rst.next()){
				res.put("id",id);
				res.put("name",rst.getString("name"));
				res.put("age",rst.getString("age"));
				res.put("disease",rst.getString("disease"));
				res.put("condition",rst.getString("condition"));
				res.put("doctor_id",rst.getString("doctor_id"));
			}
			stm.execute("select * from users where id = "+id+";");
			rst = stm.getResultSet();
			res.put("hasUsername",rst.next());
			stm.execute("select * from request where pat_id = "+id+";");
			rst = stm.getResultSet();
			res.put("hasRequest",rst.next());
			stm.execute("select * from doctor dr,details d where dr.id = "+(String)res.get("doctor_id")+" and d.id = "+(String)res.get("doctor_id")+";");
			rst = stm.getResultSet();
			if(rst.next()){
				res.put("docname",rst.getString("name"));
				res.put("docage",rst.getString("age"));
				res.put("number",rst.getString("number"));
				res.put("docspec",rst.getString("specialization"));
				res.put("docexp",rst.getString("exp"));
			}
			stm.execute("select * from doctor dr,details d where dr.id = d.id and d.id != "+(String)res.get("doctor_id")+";");
			rst = stm.getResultSet();
			HashSet <HashMap<String,Object>> hm = new HashSet<>();
			int i=1;
			while(rst.next()){
				HashMap<String,Object> hsm = new HashMap<>(); 
				hsm.put("name",rst.getString("name"));
				hsm.put("id",rst.getString("id"));
				hsm.put("spec",rst.getString("specialization"));
				hsm.put("exp",rst.getString("exp"));
				hm.add(hsm);
			}
			res.put("others",hm);
			stm.execute("select * from trusted_device where id = "+id+";");
			rst = stm.getResultSet();
			HashSet <HashMap<String,Object>> device = new HashSet<>();
			if(rst.next()){
				res.put("hasdevice",true);
				HashMap<String,Object> hsm = new HashMap<>(); 
				hsm.put("name",rst.getString("name"));
				hsm.put("prod_id",rst.getString("product_id"));
				hsm.put("s_no",rst.getString("s_no"));
				device.add(hsm);
			}else res.put("hasdevice",false);
			res.put("device",device);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.write(new Gson().toJson(res));
			conn.close();
		}catch(Exception e){
			System.out.println(e+" #Patient#");
		}
		
	}
}