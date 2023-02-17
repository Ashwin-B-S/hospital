
import java.io.*;
import java.sql.*;
import java.util.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import io.jsonwebtoken.*;

public class Authorize extends HttpServlet {
	private String App_Secret="OhyxY[QrdRVf@ejtBDEVXT^_PKSFA[DbCUIc]eSQkrsU^jAL]Y";
	
	public void doPost(HttpServletRequest request ,HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		String username = request.getParameter("username"),password = request.getParameter("password");
		try{
			LinkGen obj = new LinkGen();
			String Url = obj.generateAuthUrl(username,String.valueOf(password.hashCode()));
			String res = sendPost(Url);
			System.out.println("URL : "+Url);
			String decodedURL = URLDecoder.decode(res, "UTF-8");
			System.out.println("\nResponse : "+decodedURL);
			String tkn = ParseClaimsFromSAMLResponse.readSAMLResponse(decodedURL);
			System.out.println("\n\nObtained JWT : "+tkn);
			Claims claim = Jwts.parser().setSigningKey(App_Secret).parseClaimsJws(tkn).getBody();
			String exptm = String.valueOf(claim.get("exp"));
			String email = String.valueOf(claim.get("email"));
			String type = String.valueOf(claim.get("role"));
			Class.forName("org.postgresql.Driver");
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/hospital","postgres","123456789");
			Statement stm = conn.createStatement();
			stm.execute("select * from details where email = '"+email+"';");
			ResultSet rst = stm.getResultSet();
			String id = "",tokens = ""; 
			if(rst.next()){
				id = rst.getString("id");
				tokens = TokenGenerator.generate(id,type);
			}
			session.setAttribute("token",tokens);
			conn.close();
			if(!tokens.equals(""))response.sendRedirect("patient");
			else response.sendRedirect("http://localhost:8080/hospital");
		}catch(Exception e){
			System.out.println(e+" Authorize");
		}
	}
	private String sendPost(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode);
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