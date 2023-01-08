package bgu.spl.net.srv;

import java.util.LinkedList;

public class Topic {
    private String name;
    private LinkedList<Integer> connectionIDs;

    public Topic(String name, LinkedList<Integer> connectionIDs)
    {
        this.name = name;
        this.connectionIDs = connectionIDs;
    }
    public String getName()
    {
        return name;
    }
    public LinkedList<Integer> getConnectionIDs()
    {
        return connectionIDs;
    }
}
