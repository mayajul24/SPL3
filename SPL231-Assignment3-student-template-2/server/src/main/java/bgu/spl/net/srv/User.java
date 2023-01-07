package bgu.spl.net.srv;

import java.util.LinkedList;

public class User {
    private String userName;
    private String password;
    private int connectionId;
    private int id;
    private LinkedList<Topic> topics;

    public User(String userName, String password, int connectionId, int id){
        userName = userName;
        password = password;
        connectionId = connectionId;
        id = id;
        topics = new LinkedList<Topic>();
    }

}
