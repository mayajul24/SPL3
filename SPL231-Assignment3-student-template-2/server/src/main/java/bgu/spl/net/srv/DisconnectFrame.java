package bgu.spl.net.srv;

import java.util.HashMap;

public class DisconnectFrame extends Frame {
    private String command;
    private HashMap<String, String> headers;
    private String body;

    public DisconnectFrame(String command,HashMap<String,String> headers,String body)
    {
        this.command = command;
        this.headers = headers;
        this.body = body;
    }
    public String handleFrame(ConnectionsImpl<String> connections)
    {
        return "";
    }
    public boolean isValid()
    {
        if(headers.get("receipt") == "0"){
            //TODO(NOA): is there an apropriate receipt
        }
        return body.length()==0;
        //receipt should be unique
    }
    public String createError()
    {

    }
    public String createReplayFrame()
    {
        return "RECEIPT" + "\n" + "receipt-id:" + headers.get("receipt") + "\n" + "" + "^@";
    }
    
}