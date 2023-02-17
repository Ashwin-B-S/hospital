<%@ page contentType="text/html" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Hospital Management</title>
</script>
<style>
	*{
		margin:0;
		padding:0;	
		box-sizing:border-box;
	}
	body{
		min-height:100vh;
		background-image: url('https://amritahospitals.org/wp-content/uploads/2022/09/amritahospital-fbd-scaled.jpg');
		background-size:100% 100%;
		display:flex;
	}
	.container{
		margin:auto;
		width:500px;
		max-width:90%;
		background:linear-gradient(to right,#1F5EF9,#01dbdf);
		justify-content: space-between;
		border-radius:15px;
		letter-spacing;10px;
		box-shadow:0 5px 30px 0 rgba(80,90,100,1);
	}
	.container h1{
		margin-top:20px;
		color:#000080;
		text-align:center;
	}
	.line{
		padding:10px;
		text-align:center;
		margin-bottom:5px;
		background:transparent;
	}
	.line .box{
		padding:5px;
		width:330px;
		border-width: thick;
		border-color:#E56B36;
		font-size:20px;
		font-family:courier;
		text-align:center;
	}
	.line h3{
		margin-bottom:10px;
		color:#E6E6F6;	
		font-size:25px;
	}
	.btn{		
		width:200px;
		height:30px;
		background:linear-gradient(to left,#1F5EF9,#01dbdf);
		cursor:pointer;
		outline:none;
		font-size:20px;
		color:white;	
		border-radius:5px;
		margin-bottom:10px;
		margin-top:10px;
	}
	.btn:hover{
		opacity:.7;
	}
	.create{
		margin-left:10%;
		margin-bottom:20px;
		color:#E6E6F6;		
		font-size:20px;
		cursor:pointer;
	}
	.create a{
		color:#E6E6F6;		
		font-size:20px;
	}
	.create a:hover{
		cursor:pointer;
		color:#ADFF2F;
		opacity:.7;
	}
</style>
</head>
<body>
	<div class="container">
	<form method="post" action="j_security_check" >
		<h1>Login Here</h1>
		<div class="line">
			<h3>Username</h3>
			<input type="text" class="box" required name="j_username" id="user" placeholder="Enter your username here"/>
		</div>
		<div class="line">
			<h3>Password</h3>
			<input type="password" class="box" required name="j_password" id="password" placeholder="Enter your password here"/>
		</div>
		<div class="line">
			<input type="submit" class="btn" style=""value="Sign in"> 
		</div>
	</form>
</body>
</html>
