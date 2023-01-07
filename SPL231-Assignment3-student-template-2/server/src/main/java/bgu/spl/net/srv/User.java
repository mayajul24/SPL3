package bgu.spl.net.srv;
import java.util.List;
import java.util.LinkedList;

public class User {
    private String userName;
    private String password;
    private int connectionId;
    private int id;
    private List<Topic> topics;

    public User(String userName, String password, int connectionId, int id){
        userName = userName;
        password = password;
        connectionId = connectionId;
        id = id;
        topics = new LinkedList<Topic>();
    }

    public String getUserName(){
        return userName;
    }

    public String getPassword(){
        return password;
    }

    public int getConnectionId(){
        return connectionId;
    }

    public int getId(){
        return id;

    
    }

    public List getTopics(){
        return topics;
    }

}
