package bgu.spl.net.srv;


public abstract class Frame {

    abstract void handleFrame(ConnectionsImpl<String> connections,ConnectionHandler<String>handler,int connectionID);

}
