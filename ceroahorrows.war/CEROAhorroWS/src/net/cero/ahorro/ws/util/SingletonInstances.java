package net.cero.ahorro.ws.util;

import com.google.gson.Gson;

import java.util.Objects;

public class SingletonInstances {
    public Gson gson;
    public static SingletonInstances singletonInstances;
    public SingletonInstances() {
        this.gson = new Gson();
    }

    public static SingletonInstances getInstance() {
        if(Objects.isNull(singletonInstances)) {
            singletonInstances = new SingletonInstances();
        }

        return singletonInstances;
    }
}
