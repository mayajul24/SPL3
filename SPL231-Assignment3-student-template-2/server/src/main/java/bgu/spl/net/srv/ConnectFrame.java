package bgu.spl.net.srv;

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
    public String handleFrame()
    {
        
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
            //TODO(NOA): create a incorrectPasswordError;
            return false;
        }

        return true;


    }
    public String createError()
    {

    }
    public String createReplayFrame()
    {

    }

    public boolean checkLogin(){
        //TODO(NOA): find a way to get connectionsimpl
        if(ConnectionsImpl.getUsers.contains(headers.get("username"))){
            if(headers.get("password" == ConnectionsImpl.getUsers.get("username"))){
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
