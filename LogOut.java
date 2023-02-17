import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class LogOut extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)throws IOException,ServletException{
		HttpSession session  = request.getSession();
		session.invalidate();
		response.sendRedirect("http://localhost:8080/hospital");
	}
}