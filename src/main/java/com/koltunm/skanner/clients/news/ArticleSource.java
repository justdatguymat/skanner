package com.koltunm.skanner.clients.news;

public class ArticleSource 
{
    private String id;
    private String name;

    public ArticleSource(String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    /**
     * @return the id
     */
    public String getId() 
    {
        return id;
    }
    /**
     * @return the name
     */
    public String getName() 
    {
        return name;
    }
}
