package bgu.spl.net.srv;
import bgu.spl.net.api.StompMessagingProtocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Supplier;

public abstract class BaseServer<T> implements Server<T> {

    private final int port;
    private final Supplier<StompProtocol> protocolFactory;
    private final Supplier<StompEncDec> encdecFactory;
    private ServerSocket sock;

    public BaseServer(
            int port,
            Supplier<StompProtocol> protocolFactory,
            Supplier<StompEncDec> encdecFactory) {

        this.port = port;
        this.protocolFactory = protocolFactory;
        this.encdecFactory = encdecFactory;
		this.sock = null;
    }

    @Override
    public void serve() {
        int connectionID = 0;
        ConnectionsImpl<String> connections = new ConnectionsImpl<String>();

        try (ServerSocket serverSock = new ServerSocket(port)) {
			System.out.println("Server started");

            this.sock = serverSock; //just to be able to close

            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSock = serverSock.accept();  
                System.out.println(Thread.currentThread().isInterrupted());             
                BlockingConnectionHandler<T> handler = new BlockingConnectionHandler<>(
                        clientSock,
                        encdecFactory.get(),
                        protocolFactory.get());

                if(handler.getProtocol() instanceof StompMessagingProtocol)
                {
                    StompProtocol stompProtocol = (StompProtocol) handler.getProtocol();
                    stompProtocol.start(connectionID, connections,(ConnectionHandler<String>)handler);
                }
                connectionID++;
                execute(handler);
            }
        } catch (IOException ex) {
        }

        System.out.println("server closed!!!");
    }

    @Override
    public void close() throws IOException {
		if (sock != null)
			sock.close();
    }

    protected abstract void execute(BlockingConnectionHandler<T>  handler);

}