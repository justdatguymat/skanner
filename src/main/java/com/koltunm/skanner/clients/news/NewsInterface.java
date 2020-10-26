package com.koltunm.skanner.clients.news;


import java.time.LocalDate;
import java.util.List;

public interface NewsInterface
{
    public List<Article> getArticles(String query, LocalDate from, LocalDate to, int pageSize);
}
