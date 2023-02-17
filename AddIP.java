
import java.io.*;
import java.sql.*;
import java.util.*;
import java.sql.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import io.jsonwebtoken.*;

public class AddIP extends HttpServlet {
	private String App_Secret="OhyxY[QrdRVf@ejtBDEVXT^_PKSFA[DbCUIc]eSQkrsU^jAL]Y";
	
	public void doPost(HttpServletRequest request ,HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		String s_no = request.getParameter("s_no"),name = request.getParameter("name"),prod_id = request.getParameter("prod_id");
		String id = (String)session.getAttribute("id");
		try{
			Class.forName("org.postgresql.Driver");
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/hospital","postgres","123456789");
			Statement stm = conn.createStatement();
			stm.execute("select email from details  where id = "+id+" ;");
			ResultSet rst = stm.getResultSet();
			if(rst.next()){
				String email = rst.getString("email");
				LinkGen obj = new LinkGen();
				String Url = obj.generateInsertDeviceUrl(s_no,email,name,prod_id);
				String res = sendPost(Url);
				System.out.println(res);
			}
			stm.execute("insert into trusted_device values('"+name+"','"+s_no+"','"+prod_id+"','"+id+"')");
		}catch(Exception e){
			System.out.println(e+" AddIP");
		}
	}
	private String sendPost(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
		return response.toString();
    }
}