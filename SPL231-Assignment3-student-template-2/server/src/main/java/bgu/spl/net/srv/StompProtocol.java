package bgu.spl.net.srv;

import java.util.HashMap;

import bgu.spl.net.api.MessagingProtocol;

public class StompProtocol implements MessagingProtocol<String> {
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
        frame.handleFrame();
    }


    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
    public Frame createFrame(String command,HashMap<String,String> headers,String body)
    {
        
    }

}
