import java.io.*;
import java.util.*;
import java.util.Base64.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class LoadChat extends HttpServlet {
	
	public void doGet(HttpServletRequest request ,HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		String rec = request.getParameter("pat"),send = request.getParameter("doc");
		try{
			response.setContentType("text/html");
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/hospital","postgres","123456789");
			Statement stm = conn.createStatement();
			stm.execute("update chat set status = 400 where receiver = '"+send+"' and status != 400 ;");
			String str = "select * from chat order by sno;";
			stm.execute(str);
			ResultSet rst = stm.getResultSet();
			PrintWriter out = response.getWriter();
			String date ="date";
			Decoder decoder = Base64.getDecoder();
			while (rst.next()){
				if(send.equals(rst.getString("sender")) && rec.equals(rst.getString("receiver"))){
					if(!date.equals(rst.getString("date"))){
						date = rst.getString("date");
						out.println("<h4 style=\"text-align:center;\">"+date+"</h4>");
					}
					String time = rst.getString("time"),arr[]=time.split(":");
					time="";
					if(Integer.parseInt(arr[0])-12>0)time+=Integer.parseInt(arr[0])-12+":"+arr[1]+" pm";
					else time+=arr[0]+":"+arr[1]+" am";
					byte ar[] = decoder.decode(rst.getString("message"));
					String msg = new String(ar);
					out.print("<div class=\"message\">");
					out.print("<div class=\"sender\">");
						out.print("<h4>"+msg+"</h4>");
						if(rst.getInt("status")==400)out.print("<p>"+time+"<i class=\"fa-regular fa-check\"></i></p>");
						else out.print("<p>"+time+"<i class=\"fa-regular fa-check\"></i></p>");
					out.print("</div>");
					out.print("</div>");
				}
				if(send.equals(rst.getString("receiver")) && rec.equals(rst.getString("sender"))){
					if(!date.equals(rst.getString("date"))){
						date = rst.getString("date");
						out.println("<h4 style=\"text-align:center;\">"+date+"</h4>");
					}
					byte ar1[] = decoder.decode(rst.getString("message"));
					String msg1 = new String(ar1);
					out.print("<div class=\"message\">");
					out.print("<div class=\"receiver\">");
						out.print("<h4>"+msg1+"</h4>");
					out.print("</div>");
					out.print("</div>");
				}
			}
			conn.close();
		}catch(Exception e){
			System.out.println(e+"docload");
		}
	}
}