import javax.servlet.*;
import javax.servlet.http.*;
import javax.security.auth.Subject;
import io.jsonwebtoken.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.Base64.*;

public class ChangePassword extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res)throws IOException,ServletException{
		String password1=req.getParameter("newpass"),password2=req.getParameter("confirmpass"),id="";
		HttpSession session  = req.getSession();
		PrintWriter out = res.getWriter();
		Connection conn =null;
		if(password2.equals(password1)){
			try{
				Class.forName("org.postgresql.Driver");
				conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hospital","postgres", "123456789");
				Statement smt = conn.createStatement();
				String token="";
				if(session.getAttribute("subject")!=null){
					Subject subject = (Subject)session.getAttribute("subject");
					token = subject.getPrincipals().iterator().next().getName();
				}else{
					token = String.valueOf(session.getAttribute("token"));
				}
				Claims claim = Jwts.parser().setSigningKey("HGSdbbwhsknw17234teg2sbITEU@TZf8duj3029zie2093874hds!@#$%^hbwgue897*J82YHSBN2IHFBD").parseClaimsJws(token).getBody();
				String exptm = String.valueOf(claim.get("exp"));
				id = String.valueOf(claim.get("ID"));
				Encoder encoder = Base64.getEncoder();
				String str = password1;
				String encode = encoder.encodeToString(str.getBytes());
				smt.execute("UPDATE users SET password = '"+encode+"' WHERE id = "+id+";");
				smt.execute("select * from users where id = '"+id+"';");
				ResultSet rst = smt.getResultSet();
				if(rst.next()){
					String Url = generateAlterPassUrl(rst.getString("username").toLowerCase()+"@abs.com",String.valueOf(password1.hashCode()));
					String response = sendPost(Url);
					System.out.println(res);
				}
				res.sendRedirect("patient");
			}catch(Exception e){
				res.sendRedirect("userSignin");
				System.out.println(e);
			}
		}else{
			res.sendRedirect("manageAccount");
		}
	}

		
	public String sendPost(String url) throws Exception {

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
	
	public String generateAlterPassUrl(String email,String password){
		long expsec = System.currentTimeMillis()+(1000*60*60);
		java.util.Date dateExp = new java.util.Date(expsec);
		String App_Secret="OhyxY[QrdRVf@ejtBDEVXT^_PKSFA[DbCUIc]eSQkrsU^jAL]Y";
		String link = "http://192.168.10.236:8080/IdentityProvider/editpass?App_ID=SDHTQBJXSFMJJOWMYPGQ&token=";
		Claims claims = Jwts.claims().setExpiration(dateExp);		
		claims.put("App_ID","SDHTQBJXSFMJJOWMYPGQ");
		claims.put("email",email);
		claims.put("password",password);
		claims.put("Request_type","change password");
		claims.put("Response_type","JWT");
		String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512,App_Secret).compact();
		return link+token;
	}
}