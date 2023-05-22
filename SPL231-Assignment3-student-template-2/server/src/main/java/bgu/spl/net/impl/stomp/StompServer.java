package bgu.spl.net.impl.stomp;

import java.io.IOException;
import bgu.spl.net.srv.Server;
import bgu.spl.net.srv.StompEncDec;
import bgu.spl.net.srv.StompProtocol;

public class StompServer<T> implements Server<T> {

    public static void main(String[] args) {
        // TODO: implement this
        if(args[1].equals("tpc"))
        {
                Server.threadPerClient(
                Integer.parseInt(args[0]), //port
                () -> new StompProtocol(), //protocol factory
                () -> new StompEncDec() //message encoder decoder factory
        ).serve();
        }
        else {
                Server.reactor(
                 Runtime.getRuntime().availableProcessors(),
                 Integer.parseInt(args[0]), //port
                 () -> new StompProtocol(), //protocol factory
                 StompEncDec::new //message encoder decoder factory
         ).serve();
        }
         
    }

@Override
public void close() throws IOException {
        // TODO Auto-generated method stub
        
}

@Override
public void serve() {
        // TODO Auto-generated method stub
        
}
}
