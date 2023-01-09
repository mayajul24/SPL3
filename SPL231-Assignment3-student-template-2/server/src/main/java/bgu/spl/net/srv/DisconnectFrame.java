package bgu.spl.net.srv;

import java.util.HashMap;

public class DisconnectFrame extends Frame {
    private String command;
    private HashMap<String, String> headers;
    private String body;
    private String originalFrame;

    public DisconnectFrame(String command,HashMap<String,String> headers,String body, String originalFrame)
    {
        this.command = command;
        this.headers = headers;
        this.body = body;
        this.originalFrame = originalFrame;
    }
    public String handleFrame(ConnectionsImpl<String> connections)
    {
        String error = lookForErrors();
        if(error.length()==0)
        {
            return createReplayFrame();
        }
        else
        {
            return error;
        }
    }
    public String createError(String error)
    {
        return "ERROR" + "\n"  +
        "message: malformed frame received\n" + "\n The message:" + "\n" + "----" + 
        "\n" + originalFrame + "\n" + "----" + "\n" + error + "\n" + "\u0000";
        
    }
    public String createReplayFrame()
    {
        return "RECEIPT" + "\n" + "receipt-id:" + headers.get("receipt") + "\n" + "" + "\u0000";
    }
    public String lookForErrors()
    {
        String error="";
        if(!headers.containsKey("receipt")){
            return createError("frame doesn't contain receipt");
        }
        
        if(body.length()!=0)
        {
            return createError("body should be empty");
        }
        return error;
    }
}