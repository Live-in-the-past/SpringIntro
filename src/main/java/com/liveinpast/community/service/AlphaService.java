package com.liveinpast.community.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
@Scope("prototype")
public class AlphaService {
    // constructor
    public AlphaService() {
        System.out.println("instance AlphaService");
    }

    // init method, run after constructors
    @PostConstruct
    public void init() {
        System.out.println("init AlphaService");
    }

    // destory method, run before recycle object
    @PreDestroy
    public void destorysss() {
        System.out.println("destory AlphaService");
    }
}
