import io.jsonwebtoken.*;
import java.util.*;

public class TokenGenerator {
	public static void main(String []args){
		System.out.println(generate("8","patient"));
	}
	static String generate(String id,String type){
		long millisec = System.currentTimeMillis();
		long expsec = millisec+(60*1000*60);
		Date dateIss = new Date(millisec);
		Date dateExp = new Date(expsec);
		String sec="HGSdbbwhsknw17234teg2sbITEU@TZf8duj3029zie2093874hds!@#$%^hbwgue897*J82YHSBN2IHFBD";
		Claims claims = Jwts.claims().setExpiration(dateExp);		
		claims.put("ID",id);
		claims.put("Type",type);
		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512,sec).compact();
	}
}