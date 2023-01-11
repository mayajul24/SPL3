package bgu.spl.net.srv;
import java.util.HashMap;

public class SendFrame extends Frame {
    private HashMap<String, String> headers;
    private String body;
    private String originalMessage;

    public SendFrame(String command,HashMap<String,String> headers,String body,String originalMessage)
    {
        this.headers = headers;
        this.body = body;
        this.originalMessage = originalMessage;
    }
    public boolean handleFrame(ConnectionsImpl<String> connections, ConnectionHandler<String>handler,int connectionId)
    {
        String error = lookForErrors(connections, connectionId);
        if(error.length()==0)
        {
            String topic = getTopicName(headers.get("destination"));
            connections.send(topic, createMessageFrame(connections.getMessageID()));
            if(headers.containsKey("receipt")){
                String receipt = "RECEIPT" + "\n" + "receipt-id:" + headers.get("receipt") + "\n" + "" +"\n"+ "\u0000";
                connections.send(connectionId, receipt);
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
    
    public String lookForErrors(ConnectionsImpl<String> connections, int connectionId)
    {
        String error = "";
        if(!headers.containsKey("destination"))
        {
            return createError("Frame doesn't contain destination");
        }
        String currentTopic = getTopicName(headers.get("destination"));
        Topic topic = connections.getNameToTopic().get(currentTopic);
        if(topic!=null && !topic.getConnectionIDsToSubsctiptionIDs().containsKey("connectionId"))
        {
            return createError("User not subsctibed to Topic");
        }
        if(body=="")
        {
            return createError("body shouldn't be empty");
        }
        return error;
    }

    public String getTopicName(String destination){
        String topic = "";
        int index = destination.length()-1; 
        char currentChar = destination.charAt(index);
        while(currentChar != '/' && index >= 0)
        {
            topic = currentChar + topic;
            index--;
            currentChar = destination.charAt(index);
        }
        return topic;
    }

    public String createMessageFrame(int messageID)
    {
        return "MESSAGE"+"\n"+"subscription"+headers.get("id")+"\n"+"message-id"+messageID+"/n"+
        "destination:/topic/"+getTopicName(headers.get("destination"))+"/n"+"/n"+body;
    }
}