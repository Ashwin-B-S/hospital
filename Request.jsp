<%@ page import="java.sql.*,java.util.*,java.io.*,java.net.*,io.jsonwebtoken.*,javax.net.ssl.HttpsURLConnection,javax.servlet.http.*" contentType="text/html" language="java" %>
<html>

<head>
<title>Hospital Management</title>
<script type="text/javascript" src="JavaScript/Request.js"></script>
<link rel="stylesheet" href="CSS/Doctor.css">
</style>
</head>
<body onload="Request(<%=doc_id%>,<%=pat_id%>)">	

<%!String doc_id,pat_id;%>	

		<%
		if(request.getParameter("tkn")!=null){
			String token = request.getParameter("tkn");
			//System.out.println(token);
			try{
				Claims claim = Jwts.parser().setSigningKey("HGSdbbwhsknw17234teg2sbITEU@TZf8duj3029zie2093874hds!@#$%^hbwgue897*J82YHSBN2IHFBD").parseClaimsJws(token).getBody();
				String exptm = String.valueOf(claim.get("exp"));
				doc_id = String.valueOf(claim.get("iss"));
				pat_id = String.valueOf(claim.get("pat_id"));
			}catch(Exception e){
				System.out.println(e);
				response.sendRedirect("Home.jsp");
			}
		}		
		if(request.getParameter("tkn")==null){
			response.sendRedirect("http://localhost:8080/hospital");
		}
	%>
	<div class="logout">
		<a href="http://localhost:8080/hospital">Log In</a>
	</div>
	<div class="req" id = "req">
			<Span id = "reqtitle"></span>
			<Span id = "availreq"></span>
	</div>
</body>
</html>
	