
import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import org.json.*; 
import javax.net.ssl.*;

public class Token extends HttpServlet{
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)throws IOException,ServletException{
		System.out.println("code :"+req.getParameter("code"));
		String authCode = req.getParameter("code");
        String clientId = "1012893848736-ji3so884eh6foqsgh0kh35h4c9pj1tba.apps.googleusercontent.com";
        String clientSecret = "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";
        String redirectUri = "http://localhost:8080/Demo/token";

        String url = "https://www.googleapis.com/oauth2/v4/token";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        con.setRequestMethod("POST");

        String urlParameters = "code=" + authCode + "&client_id=" + clientId
                + "&client_secret=" + clientSecret + "&redirect_uri="
                + redirectUri + "&grant_type=authorization_code";

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
		String access="";
        System.out.println(response.toString());
		try{
			JSONObject jsobj = new JSONObject(response.toString());
			access = (String)jsobj.get("access_token");
		}catch(Exception e){
			System.out.println(e);
		}
		res.sendRedirect("https://admin.googleapis.com/admin/directory/v1/users/ashwin@illuminati.com?access_token="+access);
	}
}

/*
 myfunction(sender, receiver) {
    $.ajax({
      url:
        '"Url",
      success: function (result) {
        $('#id').html(result);
      },
    });
  }
*/