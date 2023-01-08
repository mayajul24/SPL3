package bgu.spl.net.srv;
import bgu.spl.net.srv.ConnectionsImpl;

import java.util.HashMap;

public class ConnectFrame extends Frame {
    private String command;
    private HashMap<String, String> headers;
    private String body;

    public ConnectFrame(String command,HashMap<String,String> headers,String body)
    {
        this.command = command;
        this.headers = headers;
        this.body = body;
    }
    @Override
    public String handleFrame(ConnectionsImpl<String> connections)
    {
        return "";
    }
    public boolean isValid()
    {
        if(headers.get("version") != "1.2"){
            return false;
        }
        if(headers.get("body") != ""){
            return false;
        }
        if(headers.get("host") != "stomp.bgu.ac.il"){
            return false;
        }
        if(!checkLogin()){
            //TODO(NOA): create an incorrectPasswordError;
            return false;
        }

        return true;


    }
    public String createError()
    {

    }
    public String createReplayFrame()
    {
        return "CONNECTED" + "\n" + "version:1.2" + "\n" + "" + "\u0000"; 
    }

    public boolean checkLogin(ConnectionsImpl<String> connections){
        if(connections.getUsers().containsKey(headers.get("login"))){
            if(headers.get("passcode") == connections.getUsers().get(headers.get("login"))){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            //TODO(NOA): add the user to connectionsimpl
            return true;
        }
        
    }
    
    
}
