package bgu.spl.net.srv;

import java.util.HashMap;

public class Frame {
    private String command;
    private HashMap<String, String> headers;
    private String body;

    public Frame(String command, HashMap<String, String> headers, String body) {

        this.command = command;
        this.headers = headers;
        this.body = body;
    }
    public String handleFrame()
    {
        // if(command.equals("CONNECT"))
        // {
        //     if(connectFrameIsValid())
        //     {
        //         return createConnectedFrame()
        //     }
        //     else
        //     {
        //         return createConnectionError()
        //     }
        // }
        // if(command.equals("SEND"))
        // {
            
        //     if(sendFrameIsValid())
        //     {
        //         return createSendFrame()
        //     }
        //     else
        //     {
        //         return createSendError()
        //     }
        // }
        // if(command.equals("SUBSCRIBE"))
        // {
            
        //     if(connectFrameIsValid())
        //     {
        //         return createSubscribeFrame()
        //     }
        //     else
        //     {
        //         return createSubscriptionError()
        //     }
        // }
        // if(command.equals("UNSUBSCRIBE"))
        // {
            
        //     if(UnsubscribeFrameIsValid())
        //     {
        //         return createUnsubscribeFrame()
        //     }
        //     else
        //     {
        //         return createUnsubscriptionError()
        //     }
        // }
        
        // if(command.equals("DISCONNECT"))
        // {
            
        //     if(disconnectFrameIsValid())
        //     {
        //         return createDisconnectedFrame()
        //     }
        //     else
        //     {
        //         return createDisconnectionError()
        //     }
        // }

    }
    public String getCommand()
    {
        return command;
    }
    public HashMap<String, String> getHeaders()
    {
        return headers;
    }
    public String getBody()
    {
        return body;
    }
}
