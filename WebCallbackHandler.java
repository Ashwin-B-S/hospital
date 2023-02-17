
import java.io.*;
import javax.security.auth.callback.*;

public class WebCallbackHandler implements CallbackHandler {
	private String username,password;
	
	WebCallbackHandler(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public void handle(Callback arr[]) throws IOException,UnsupportedCallbackException{
		int count=0;
		while(count < arr.length){
			if(arr[count] instanceof NameCallback){
				NameCallback name = (NameCallback)arr[count++];
				name.setName(username);
			}else if(arr[count] instanceof PasswordCallback){
				PasswordCallback pass = (PasswordCallback) arr[count++];
				pass.setPassword(password.toCharArray());
			}
		}
	}
}