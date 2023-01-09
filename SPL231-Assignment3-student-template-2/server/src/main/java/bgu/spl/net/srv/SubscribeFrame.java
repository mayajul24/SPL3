package bgu.spl.net.srv;

import java.util.HashMap;

public class SubscribeFrame extends Frame {
    private String command;
    private HashMap<String, String> headers;
    private String body;
    private String originalMessage;

    public SubscribeFrame(String command,HashMap<String,String> headers,String body,String originalMessage)
    {
        this.command = command;
        this.headers = headers;
        this.body = body;
        this.originalMessage = originalMessage;
    }
    public boolean handleFrame(ConnectionsImpl<String> connections, ConnectionHandler<String>handler,int connectionId)
    {
        String error = lookForErrors(connections);
        if(error.length() == 0)
        {
          Topic toSubscribe = connections.getNameToTopic().get(headers.get("destination"));
          toSubscribe.getConnectionIDs().add(connectionId);
          if(headers.containsKey("receipt") && headers.get("receipt") == "")
          {
            String receipt = "RECEIPT" + "\n" + "receipt-id:" + headers.get("receipt") + "\n" + "" +"\n"+ "\u0000";;
            connections.send(connectionId,receipt);
          }
          return true;
        }
        else
        {
            connections.send(connectionId, error);
            connections.disconnect(connectionId);
            return false;
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
        String receipt = "";
         if(headers.containsKey("receipt")){
            receipt = "receipt-id:" + headers.get("receipt") + "\n";
         }
        return "RECEIPT" + "\n" + "receipt-id:" + receipt  + "" + "\n"+ "\u0000";
    }

    public String lookForErrors(ConnectionsImpl<String> connections){
        if(connections.getNameToTopic().containsKey(headers.get("destenation")))
        {

        }
        if(!headers.containsKey("destination"))
        {
            return createError("Frame doesn't contain destination");
        }
        if(!connections.getNameToTopic().containsKey(headers.get("destination")))
        {
            return createError("topic doesn't exist");
        }
        // if(!headers.containsKey("receipt"))
        // {
        //     return createError("Frame doesn't contain receipt");
        // }
        if(!headers.containsKey("id"))
        {
            return createError("Frame doesn't contain id");
        }
        if(headers.get("id").length() == 0)
        {
            return createError("Invalid id");
        }
        if(body.length()!=0)
        {
            return createError("body should be empty");
        }

        return "";
    }
    
}
