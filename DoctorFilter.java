package Filters;

import javax.servlet.*;
import java.io.*;
import io.jsonwebtoken.*;
import javax.servlet.http.*;
import javax.security.auth.Subject;

public class DoctorFilter implements Filter {
	public void init(FilterConfig filterconfig)throws ServletException {
		
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain)throws ServletException,IOException{
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		HttpSession session = req.getSession();
		String token = "";
		boolean flag = false;
		token = String.valueOf(session.getAttribute("token"));
		
		try{
			Claims claim = Jwts.parser().setSigningKey("HGSdbbwhsknw17234teg2sbITEU@TZf8duj3029zie2093874hds!@#$%^hbwgue897*J82YHSBN2IHFBD").parseClaimsJws(token).getBody();
			String exptm = String.valueOf(claim.get("exp"));
			String id = String.valueOf(claim.get("ID"));
			session.setAttribute("id",id);
			System.out.println(id);
			String type= String.valueOf(claim.get("Type"));
			flag=type.equals("doctor");
		}catch(Exception e){
			System.out.println(e);
			try{
				res.sendRedirect("http://localhost:8080/hospital");
			}catch(Exception ex){
				System.out.println(ex);
			}
		}
		if(flag)chain.doFilter(request,response);
		else res.sendRedirect("admin");
		
	}
	
	public void destroy(){
		
	}
}