package bgu.spl.net.srv;

import java.util.HashMap;

public class SubscribeFrame extends Frame {
    private String command;
    private HashMap<String, String> headers;
    private String body;

    public SubscribeFrame(String command,HashMap<String,String> headers,String body)
    {
        this.command = command;
        this.headers = headers;
        this.body = body;
    }
    public String handleFrame(ConnectionsImpl<String> connections)
    {
        return "";
    }
    public boolean isValid()
    {
        if(!ConnectionsImpl.getTopics.contains(headers.get("destination"))){
            return false;
        }
        if(headers.get("body") != ""){
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
