
import java.io.*;
import java.security.*;
public class jaasPrincipal implements Principal,Serializable{
	private String name;
	
	public jaasPrincipal(String name){
		this.name = name;	
	}
	public String getName(){
		return name;
	}
}