package com.example.merisuraksha.Domain;

import java.util.List;

public class POJO {

    private Integer totalResults;
    private List<Articles> articles;
    private String status;

    public Integer getTotalResults() {

        return totalResults;

    }

    public void setTotalResults(Integer totalResults) {

        this.totalResults = totalResults;

    }

    public List<Articles> getArticles() {

        return articles;

    }

    public void setArticles(List<Articles> articles) {

        this.articles = articles;

    }

    public String getStatus() {

        return status;

    }

    public void setStatus(String status) {

        this.status = status;

    }

}


 class Source {

    private String name;
    private String id;

    public String getName() {

        return name;

    }

    public void setName(String name) {

        this.name = name;

    }

    public String getId() {

        return id;

    }

    public void setId(String id) {

        this.id = id;

    }

}





