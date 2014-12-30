package com.springapp.mvc;

import akka.actor.ActorSystem;

/**
 * Created by zjh on 14-12-30.
 */
public class ActorSysContainer {
    private ActorSystem sys;
    private ActorSysContainer() {
        sys = ActorSystem.create("MySystem1");
    }

    public ActorSystem getSystem() {
        return sys;
    }

    private static ActorSysContainer instance = null;

    public static synchronized ActorSysContainer getInstance() {
        if (instance == null) {
            instance = new ActorSysContainer();
        }
        return instance;
    }
}
