
import java.util.*;
import java.util.Base64.*;
import java.sql.*;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.*;
import javax.security.auth.spi.*;
import javax.servlet.*;
import javax.servlet.http.*;
import io.jsonwebtoken.*;

@SuppressWarnings("unchecked")
public class hospitalLoginModule implements LoginModule{
	
	private String login;
	private CallbackHandler callbackHandler = null;
	private static HashMap<String,ArrayList <String> > hm = new HashMap<>();
	private Subject subject;
	private List<String> userGroups;
	private UserPrincipal userPrincipal;
	private RolePrincipal rolePrincipal;
	
	@Override
	public void initialize(Subject subject,CallbackHandler callbackHandler,Map<String, ?> sharedState,Map<String, ?> options) {
		this.callbackHandler = callbackHandler;
		this.subject = subject;
	}
	
	@Override
	public boolean login()throws LoginException{
		boolean flag=false;
		Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("login");
        callbacks[1] = new PasswordCallback("password", true);
		try {
			callbackHandler.handle(callbacks);
			String name = ((NameCallback) callbacks[0]).getName();
			String password = String.valueOf(((PasswordCallback) callbacks[1]).getPassword());
			String type="";
			Class.forName("org.postgresql.Driver");
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hospital","postgres", "123456789");
			Statement smt = conn.createStatement();
			smt.execute("SELECT * FROM users;");
			ResultSet rst = smt.getResultSet();
			while(rst.next()){
				ArrayList <String> al = new ArrayList<>();
				Decoder decoder = Base64.getDecoder();
				byte arr[] = decoder.decode(rst.getString("password"));
				al.add(new String(arr));
				al.add(rst.getString("id"));
				hm.put(rst.getString("username"),al);
			}
			for(Map.Entry <String,ArrayList <String> > x : hm.entrySet()){
				if(name.equals(x.getKey()) && password.equals(x.getValue().get(0))){
					login = name;
					flag=true;
					userGroups = new ArrayList<String>();
					userGroups.add("User");
					break;
				}
			}
			hm.clear();
			conn.close();
		}catch(Exception e){
			System.out.println(e);
		}
		return flag;
	}
	
	@Override
	public boolean commit()throws LoginException{
		userPrincipal = new UserPrincipal(login);
		subject.getPrincipals().add(userPrincipal);
		if (userGroups != null && userGroups.size() > 0) {
			for (String groupName : userGroups) {
				rolePrincipal = new RolePrincipal(groupName);
				subject.getPrincipals().add(rolePrincipal);
			}
		}
		//Auth.setUrl(login);
		return true;
	}
	
	@Override
	public boolean abort()throws LoginException{
		return false;
	}
	
	@Override
	public boolean logout()throws LoginException{
		subject.getPrincipals().remove(userPrincipal);
		subject.getPrincipals().remove(rolePrincipal);
		return true;
	}
}