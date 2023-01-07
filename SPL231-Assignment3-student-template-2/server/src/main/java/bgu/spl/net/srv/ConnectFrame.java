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


    }
    public String createError()
    {

    }
    public String createReplayFrame()
    {

    }
    
}
