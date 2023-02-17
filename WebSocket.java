import java.io.*;
import java.sql.*;
import java.util.*;
import javax.json.*;
import javax.websocket.*;
import org.json.simple.*;
import java.util.Base64.*;
import javax.websocket.Session;
import javax.websocket.server.*;
import java.text.SimpleDateFormat;

 
@ServerEndpoint("/socket/{username}")
public class WebSocket{
	
	static Set<Session> chatroomUsers = Collections.synchronizedSet(new HashSet<Session>());
	
	@OnOpen
	public void handleOpen(Session userSession, @PathParam("username") String username) throws Exception{
		System.out.println(username);
		if(username!=null){
			userSession.getUserProperties().put("username",username);
			chatroomUsers.add(userSession);			
		}
		for(Session ses: chatroomUsers){
			if(!((String)ses.getUserProperties().get("username")).equals(username))ses.getBasicRemote().sendText(buildJsonData(username,"100","all"));
		}
	}
	
	@OnMessage
	public void handleMessage(String Message,Session userSession) throws Exception{
		boolean flag = true;		
		Object obj=JSONValue.parse(Message);  
		JSONObject msg = (JSONObject) obj;  
		String username=(String)userSession.getUserProperties().get("username");
		String sender = (String)msg.get("sender"),receiver = (String)msg.get("receiver"), message = (String)msg.get("message");
		for (Session ses: chatroomUsers){
			if(((String)ses.getUserProperties().get("username")).equals(receiver)){
				ses.getBasicRemote().sendText(buildJsonData(sender,message,receiver));
				flag = false;
			}				
		}
		if(flag){
			addText(sender,message,receiver,200);
			userSession.getBasicRemote().sendText(buildJsonData(sender,"200",receiver));
		}else{
			addText(sender,message,receiver,400);
			userSession.getBasicRemote().sendText(buildJsonData(sender,"400",receiver));
		}
	}
	
	@OnClose
	public void handleClose(Session userSession){
		chatroomUsers.remove(userSession);
	}

	private String buildJsonData(String sender,String message,String receiver){
		JsonObject obj=Json.createObjectBuilder().add("sender",sender).add("message",message).add("receiver",receiver).build();
		StringWriter writer=new StringWriter();
		try(JsonWriter Writer=Json.createWriter(writer)){
			Writer.write(obj);
		}
		return writer.toString();
	}
	
	private void addText(String sender, String message, String receiver, int ack){
		try{
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/hospital","postgres","123456789");
			Statement stm = conn.createStatement();
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyy");
			SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");
			java.util.Date date = new java.util.Date();
			String curdate = sdf1.format(date);
			String curtime = sdf2.format(date);  
			java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
			String encode = encoder.encodeToString(message.getBytes());
			String str = "insert into chat values('"+sender+"','"+encode+"','"+curtime+"','"+curdate+"','"+receiver+"',"+ack+");";
			stm.execute(str);
			conn.close();
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	@OnError
	public void onError(Session session, Throwable thr) {
		//System.out.println("Connection aborted!");
	}
}