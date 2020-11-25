package com.edge.service.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "access_tracking")
public class AccessTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "client")
    private String client;

    @Column(name = "api")
    private String api;

    @Column(name = "initial_access_date")
    private Date initialAccessDate;

    @Column(name = "access_count")
    private int accessCount;

    public AccessTracking() {

    }

    public AccessTracking(String client, String api, Date initialAccessDate, int accessCount) {
        this.client = client;
        this.api = api;
        this.initialAccessDate = initialAccessDate;
        this.accessCount = accessCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public Date getInitialAccessDate() {
        return initialAccessDate;
    }

    public void setInitialAccessDate(Date initialAccessDate) {
        this.initialAccessDate = initialAccessDate;
    }

    public int getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(int accessCount) {
        this.accessCount = accessCount;
    }

    @Override
    public String toString() {
        return "Tutorial [id=" + id + ", client=" + client + ", api=" + api + ", api=" + api + ", initialAccessDate=" + initialAccessDate +  ", accessCount=" + accessCount + "]";
    }

}
