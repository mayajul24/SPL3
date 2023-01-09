package bgu.spl.net.srv;


public abstract class Frame {

    abstract boolean handleFrame(ConnectionsImpl<String> connections,ConnectionHandler<String>handler,int connectionID);
}