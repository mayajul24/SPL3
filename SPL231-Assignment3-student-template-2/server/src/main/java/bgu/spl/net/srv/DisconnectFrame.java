package bgu.spl.net.srv;

import java.util.HashMap;
import java.util.Map;

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
    public void handleFrame(ConnectionsImpl<String> connections, ConnectionHandler<String>handler,int connectionId)
    {
        String error = lookForErrors();
        if(error.length()==0)
        {
            connections.send(connectionId, createReplayFrame());
            String username = connections.getConnectionIdToUsername().get(connectionId);
            connections.getConnectedUsers().remove(username);
            connections.getConnectionIdToConnectionHandler().remove(connectionId);
            for (Map.Entry<String,Topic> entry : connections.getNameToTopic().entrySet()) 
            {
                Topic currentTopic = entry.getValue();
                if(currentTopic.getConnectionIDs().contains(connectionId)){
                    currentTopic.getConnectionIDs().remove(connectionId);
                }
            }
            connections.getConnectionIdToUsername().remove(connectionId);
    }

        
        else
        {
            connections.send(connectionId,error);
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
        return "RECEIPT" + "\n" + "receipt-id:" + headers.get("receipt") + "\n" + "" +"\n"+ "\u0000";
    }
    public String lookForErrors()
    {
        String error="";
        if(!headers.containsKey("receipt") || headers.get("receipt")!="")
        {
            return createError("frame doesn't contain receipt");
        }
        
        if(body.length()!=0)
        {
            return createError("body should be empty");
        }
        return error;
    }

    
}