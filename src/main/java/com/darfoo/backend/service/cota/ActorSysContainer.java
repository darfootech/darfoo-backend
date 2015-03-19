package com.darfoo.backend.service.cota;

import akka.actor.ActorSystem;

/**
 * Created by zjh on 15-3-19.
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
