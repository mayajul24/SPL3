package bgu.spl.net.srv;


public abstract class Frame {

    abstract String handleFrame(ConnectionsImpl<String> connections);

}
