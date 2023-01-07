package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.Server;
import bgu.spl.net.srv.StompEncDec;
import bgu.spl.net.srv.StompProtocol;

public class StompServer implements Server<T> {

    public static void main(String[] args) {
        // TODO: implement this
        Server.threadPerClient(
                7777, //port
                () -> new StompProtocol(), //protocol factory
                StompEncDec::new //message encoder decoder factory
        ).serve();

         Server.reactor(
                 Runtime.getRuntime().availableProcessors(),
                 7777, //port
                 () -> new StompProtocol(), //protocol factory
                 StompEncDec::new //message encoder decoder factory
         ).serve();
    }
}
