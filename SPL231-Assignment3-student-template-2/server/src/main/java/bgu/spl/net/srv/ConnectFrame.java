package bgu.spl.net.srv;

import java.util.HashMap;

public class ConnectFrame extends Frame {
    private String command;
    private HashMap<String, String> headers;
    private String body;

    public ConnectFrame(String command,HashMap<String,String> headers,String body)
    {
        this.command = command;
        this.headers = headers;
        this.body = body;
    }
    
}
