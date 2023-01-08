package bgu.spl.net.srv;

import java.util.HashMap;

public class UnsubscribeFrame extends Frame {
    private String command;
    private HashMap<String, String> headers;
    private String body;
    private String originalMessage;

    public UnsubscribeFrame(String command,HashMap<String,String> headers,String body,String originalMessage)
    {
        this.command = command;
        this.headers = headers;
        this.body = body;
        this.originalMessage = originalMessage;
    }
    public String handleFrame(ConnectionsImpl<String> connections)
    {
        return "";
    }
    
    public String createError()
    {
        
    }
    public String createReplayFrame()
    {

    }
    
}