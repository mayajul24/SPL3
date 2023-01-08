package bgu.spl.net.srv;

import java.util.HashMap;

public class SendFrame extends Frame {
    private String command;
    private HashMap<String, String> headers;
    private String body;

    public SendFrame(String command,HashMap<String,String> headers,String body)
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
        if(!headers.containsKey("destination")){
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
