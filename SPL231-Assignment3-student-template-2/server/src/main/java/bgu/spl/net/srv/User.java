package bgu.spl.net.srv;

public class User {
    private String userName;
    private String password;
    private int connectionId;
    private int id;
    private List<Topics> topics;

    public User(String userName, String password, int connectionId, int id){
        userName = userName;
        password = password;
        connectionId = connectionId;
        id = id;
        topics = new LinkedList<topics>;
    }

}
