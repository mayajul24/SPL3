package bgu.spl.net.srv;

import java.util.HashMap;

public class UnsubscribeFrame extends Frame {
    private String command;
    private HashMap<String, String> headers;
    private String body;

    public UnsubscribeFrame(String command,HashMap<String,String> headers,String body)
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

    }
    public String createError()
    {

    }
    public String createReplayFrame()
    {

    }
    
}