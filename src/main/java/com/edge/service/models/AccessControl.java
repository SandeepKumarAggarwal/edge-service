package com.edge.service.models;

import javax.persistence.*;

@Entity
@Table(name = "access_control")
public class AccessControl {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "user")
	private String user;

	@Column(name = "client")
	private String client;

	@Column(name = "api")
	private String api;

    @Column(name = "rateLimit")
    private int rateLimit;

    @Column(name = "rateLimitMinutes")
    private int rateLimitMinutes;

	public AccessControl() {

	}

	public AccessControl(String user, String client, String api, int ratelimit, int rateLimitMinutes) {
		this.user = user;
		this.client = client;
		this.api = api;
		this.rateLimit = ratelimit;
		this.rateLimitMinutes = rateLimitMinutes;
	}

    public int getRateLimitMinutes() {
        return rateLimitMinutes;
    }

    public void setRateLimitMinutes(int rateLimitMinutes) {
        this.rateLimitMinutes = rateLimitMinutes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public int getRateLimit() {
        return rateLimit;
    }

    public void setRateLimit(int rateLimit) {
        this.rateLimit = rateLimit;
    }

    @Override
	public String toString() {
		return "Tutorial [id=" + id + ", user=" + user + ", client=" + client + ", api=" + api + ", rateLimit=" + rateLimit + ", rateLimitMinutes=" + rateLimitMinutes + "]";
	}

}
