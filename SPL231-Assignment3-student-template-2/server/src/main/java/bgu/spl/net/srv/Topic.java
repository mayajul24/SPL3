package bgu.spl.net.srv;

import java.util.LinkedList;

public class Topic {
    private String name;
    private LinkedList<User> users;

    public Topic(String name, LinkedList<User> users)
    {
        this.name = name;
        this.users = users;
    }
    public String getName()
    {
        return name;
    }
    public LinkedList<User> getUsers()
    {
        return users;
    }
}
