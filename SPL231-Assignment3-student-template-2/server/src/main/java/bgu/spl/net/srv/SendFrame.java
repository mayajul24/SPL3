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
    public void handleFrame(ConnectionsImpl<String> connections, ConnectionHandler<String>handler,int connectionId)
    {
        String error = lookForErrors(connections);
        if(error.length()==0)
        {
            connections.send(connectionId, "");
        }
        else
        {
            connections.send(connectionId, error);
            connections.disconnect(connectionId);
        }
        
        
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
    
    public String lookForErrors(ConnectionsImpl<String> connections)
    {
        String error = "";
        if(!headers.containsKey("destination"))
        {
            return createError("Frame doesn't contain destination");
        }
        return error;
    }
}