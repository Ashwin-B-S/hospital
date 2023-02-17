
import java.io.*;
import java.sql.*;
import java.util.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import io.jsonwebtoken.*;

public class Saml extends HttpServlet {
	private String App_Secret="OhyxY[QrdRVf@ejtBDEVXT^_PKSFA[DbCUIc]eSQkrsU^jAL]Y";
	
	public void doPost(HttpServletRequest request ,HttpServletResponse response) throws IOException, ServletException {
		System.out.println("Hello SAML");
		HttpSession session = request.getSession();
		String serialnumber = request.getParameter("s_no"),name = request.getParameter("name"),prod_id = request.getParameter("prod_id");
		try{
			LinkGen obj = new LinkGen();
			String Url = obj.generateAuthUrl(serialnumber,name,prod_id);
			String res = sendPost(Url);
			System.out.println("URL : "+Url);
			String decodedURL = URLDecoder.decode(res, "UTF-8");
			System.out.println("\nResponse : "+decodedURL);
			String tkn = ParseClaimsFromSAMLResponse.readSAMLResponse(decodedURL);
			System.out.println("\n\nObtained JWT : "+tkn);
			Claims claim = Jwts.parser().setSigningKey(App_Secret).parseClaimsJws(tkn).getBody();
			if(String.valueOf(claim.get("status")).equals("success")){
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
				else response.sendRedirect("idplogin");
			}else{
				response.sendRedirect("idplogin");
			}
		}catch(Exception e){
			System.out.println(e+" DeviceAuthorize");
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
	
	private String getHardwareAddressAsString(byte[] hardwareAddress) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hardwareAddress.length; i++) {
            sb.append(String.format("%02X%s", hardwareAddress[i], (i < hardwareAddress.length - 1) ? "-" : ""));        
        }
        return sb.toString();
    }
}