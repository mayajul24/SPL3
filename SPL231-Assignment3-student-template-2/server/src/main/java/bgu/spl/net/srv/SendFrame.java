package bgu.spl.net.srv;

import java.util.HashMap;

public class SendFrame extends Frame {
    private String command;
    private HashMap<String, String> headers;
    private String body;
    private String originalMessage;

    public SendFrame(String command,HashMap<String,String> headers,String body,String originalMessage)
    {
        this.command = command;
        this.headers = headers;
        this.body = body;
        this.originalMessage = originalMessage;
    }
    public String handleFrame(ConnectionsImpl<String> connections)
    {
        if(!headers.containsKey("destination"))
        {
            return createError("Frame doesn't contain destination");
        }
        return createReplayFrame();
    }
    
    public String createError(String error)
    {  
        String receipt = "";
        if(headers.containsKey("receipt")){
            receipt = "receipt-id: message-" + headers.get("receipt") +"\n";
        }
            
        return "ERROR" + "\n" + receipt +
        "message: malformed frame received\n" + "\n The message:" + "\n" + "----" + 
        "\n" + originalMessage + "\n" + "----" + "\n" + error + "\n" + "\u0000";
    }
    public String createReplayFrame()
    {
        return "";
    }  
}