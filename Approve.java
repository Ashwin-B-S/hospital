package Doctor;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.*;
import java.sql.*;

public class Approve extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res)throws IOException,ServletException{
		
		PrintWriter out = res.getWriter();
		String pat_id = req.getParameter("pat"),doc_id = req.getParameter("doc");
		Connection conn = null;
		if(!doc_id.equals("rejected")){
			try {
            	conn = DriverManager.getConnection("jdbc:postgresql://localhost/hospital","postgres","123456789");
				Statement stm = conn.createStatement();
				stm.execute("update patient set doctor_id="+doc_id+" where id="+pat_id+";");
				int num=(int)(Math.random()*10);
				if(num<=6){
					stm.execute("update patient set condition= 'Stable' where id="+pat_id+";");
				}else{
					stm.execute("update patient set condition= 'Critical',status='Alive' where id="+pat_id+";");
				}
				stm.execute("delete from request where pat_id="+pat_id+";");
				conn.close();
        	}catch (Exception e){
				System.out.println(e);
        	}
		}else{
			try{
				conn = DriverManager.getConnection("jdbc:postgresql://localhost/hospital","postgres","123456789");
				Statement stm = conn.createStatement();
				stm.execute("delete from request where pat_id="+pat_id+";");
				conn.close();
			}catch(Exception ex){
				System.out.println(ex);
			}
		}
	}	
}