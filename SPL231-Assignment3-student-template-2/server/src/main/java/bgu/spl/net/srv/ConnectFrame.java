package bgu.spl.net.srv;
import bgu.spl.net.srv.ConnectionsImpl;

import java.util.HashMap;

public class ConnectFrame extends Frame {
    private String command;
    private HashMap<String, String> headers;
    private String body;
    private String originalMessage;

    public ConnectFrame(String command,HashMap<String,String> headers,String body, String originalFrame)
    {
        this.command = command;
        this.headers = headers;
        this.body = body;
        this.originalMessage = originalFrame;
        
    }
    @Override
    public String handleFrame(ConnectionsImpl<String> connections)
    {
        String error = lookForErrors(connections);
        if(error.length()==0)
        {
            connections.getConnectedUsers().add(headers.get("login"));
            return "CONNECTED" + "\n" + "version:1.2" + "\n" + "" + "\n" + "\u0000";
        }
        else{
            return error;
        }
         
    }   

    public String createError(String error){
        String receipt = "";

        if(headers.containsKey("receipt")){
            receipt = "receipt-id: message-" + headers.get("receipt") +"\n";
        }
            
        return "ERROR" + "\n" + receipt +
        "message: malformed frame received\n" + "\n The message:" + "\n" + "----" + 
        "\n" + originalMessage + "\n" + "----" + "\n" + error + "\n" + "\u0000";
        
    }

    public boolean checkPasscode(ConnectionsImpl<String> connections){
        if(connections.getUsersToPasscode().containsKey(headers.get("login"))){
            if(headers.get("passcode") == connections.getUsersToPasscode().get(headers.get("login")))
            {
                 return true;
            }
            else
            {
                return false;
            }
        }
        else 
        {
            //Add new user to connections
            connections.getUsersToPasscode().put(headers.get("login"),headers.get("passcode"));
            return true;
        }
        
    }
    public String lookForErrors(ConnectionsImpl<String> connections)
    {
        String error = "";
        if(!headers.containsKey("accept-version"))
        {
            return createError("Frame should contain accept-version header");
        }
        if(!headers.containsKey("host"))
        {
            return createError("Frame should contain host header");
        }
        if(!headers.containsKey("login"))
        {
            return createError("Frame should contain login header");
        }
        if(!headers.containsKey("passcoden"))
        {
            return createError("Frame should contain passcode header");
        }
        if(connections.getConnectedUsers().contains(headers.get("login"))){
            return createError("User already connected");
        }
    
        if(headers.get("accept-version") != "1.2"){
            return createError("Incorrect frame version");
        }
        if(headers.get("host") != "stomp.bgu.ac.il"){
            return createError("Host name incorrect");
        }
        if(headers.get("body") != ""){
            return createError("Frame body should be empty");
        }
        
        if(!checkPasscode(connections)){
            return createError("Incorrect passcode");
        }

        return error;
    } 
}
