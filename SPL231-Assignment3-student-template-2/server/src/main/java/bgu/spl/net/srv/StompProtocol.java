package bgu.spl.net.srv;

import java.util.HashMap;
import bgu.spl.net.api.StompMessagingProtocol;

public class StompProtocol implements StompMessagingProtocol<String> {
    private boolean shouldTerminate = false;
    private int connectionID;
    private ConnectionsImpl<String> connections;
    private ConnectionHandler<String> handler;

    @Override
    public void process(String msg) {
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
                key = "";
                value = "";
                i++;
            }
        }
        i++;
        // read body:
        String body = "";
        if (i < msg.length()) {
            while (msg.charAt(i) != '\n') {
                body = body + msg.charAt(i);
                i++;
            }
        }
        headers.put("message", msg);
        
        Frame frame = createFrame(command, headers, body, msg);
        
        if (frame != null) {
            boolean success = frame.handleFrame(connections, handler, connectionID);
            if (!success) {
                shouldTerminate = true;
            }
        }

        else {
            String error = "ERROR" + "\n" +
                    "message: malformed frame received\n" + "\n The message:" + "\n" + "----" +
                    "\n" + msg + "\n" + "----" + "\n" + "Command is not defined" + "\n" + "\u0000";
            connections.send(connectionID, error);
            connections.disconnect(connectionID);
            shouldTerminate = true;
        }
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    public Frame createFrame(String command, HashMap<String, String> headers, String body, String msg) {
        if (command.equals("CONNECT")) {
            return new ConnectFrame(command, headers, body, msg);
        }
        if (command.equals("DISCONNECT")) {
            return new DisconnectFrame(command, headers, body, msg);
        }
        if (command.equals("SUBSCRIBE")) {
            
            return new SubscribeFrame(command, headers, body, msg);
        }
        if (command.equals("UNSUBSCRIBE")) {
            return new UnsubscribeFrame(command, headers, body, msg);
        }
        if (command.equals("SEND")) {
            return new SendFrame(command, headers, body, msg);
        } else {
            return null;
        }
    }

    @Override
    public void start(int connectionId, Connections<String> connections, ConnectionHandler<String> handler) {
        this.connectionID = connectionId;
        this.connections = (ConnectionsImpl<String>) connections;
        this.handler = handler;
        this.connections.getConnectionIdToConnectionHandler().put(connectionId, handler);

    }

}
