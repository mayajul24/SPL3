package bgu.spl.net.srv;

import java.util.HashMap;

import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.StompMessagingProtocol;

public class StompProtocol implements StompMessagingProtocol<String> {
    private boolean shouldTerminate = false;

    @Override
    public String process(String msg) {
        String command = "";
        HashMap<String, String> headers = new HashMap<String, String>();
        int i = 0;
        while (msg.charAt(i) != '\n') {
            command = command + msg.charAt(i);
            i++;
        }
        i++;
        // checks if there are headers to read:
        if (msg.charAt(i) != '\n') { 
            String key = "";
            String value = "";
            while (msg.charAt(i) != '\n') {
                while (msg.charAt(i) != ':') {
                    key = key + msg.charAt(i);
                    i++;
                }
                i++;
                while (msg.charAt(i) != '\n') {
                    value = value + msg.charAt(i);
                    i++;
                }
                headers.put(key, value);
                i++;
            }
        }
        i++;
        //read body:
        String body = "";
        while (msg.charAt(i) != '\n')
        {
            body = body + msg.charAt(i);
        }
        Frame frame  = createFrame(command,headers,body);
        return frame.handleFrame();
    }

    @Override
    void start(int connectionId, Connections<T> connections){
        
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
    public Frame createFrame(String command,HashMap<String,String> headers,String body)
    {
        if(command.equals("CONNECT"))
        {
            return new ConnectFrame(command, headers, body);
        }
        if(command.equals("DISCONNECT"))
        {
            return new DisconnectFrame(command, headers, body);
        }
        if(command.equals("SUBSCRIBE"))
        {
            return new SubscribeFrame(command, headers, body);
        }
        if(command.equals("UNSUBSCRIBE"))
        {
            return new UnsubscribeFrame(command, headers, body);
        }
        else
        {
            return new SendFrame(command, headers, body);
        }
    }

}
