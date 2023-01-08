package bgu.spl.net.srv;
import bgu.spl.net.srv.ConnectionsImpl;

import java.util.HashMap;

public class ConnectFrame extends Frame {
    private String command;
    private HashMap<String, String> headers;
    private String body;
    private String originalFrame;

    public ConnectFrame(String command,HashMap<String,String> headers,String body, String originalFrame)
    {
        this.command = command;
        this.headers = headers;
        this.body = body;
        this.originalFrame = originalFrame;
    }
    @Override
    public String handleFrame(ConnectionsImpl<String> connections)
    {
        if(!headers.containsKey("host"))
        {
            return "";
        }
        if(connections.getConnectionHandlers().contains(connections))
        
        if(headers.get("version") != "1.2"){
            return "";
        }
        if(headers.get("body") != ""){
            return "";
        }
        if(headers.get("host") != "stomp.bgu.ac.il"){
            return "";
        }
        if(!checkPasscode(connections)){
            //TODO(NOA): create an incorrectPasswordError;
            return "";
        }
        return "CONNECTED" + "\n" + "version:1.2" + "\n" + "" + "\n" + "\u0000"; 
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
    
    
}
