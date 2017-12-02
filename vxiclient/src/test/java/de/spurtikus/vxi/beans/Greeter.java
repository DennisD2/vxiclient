package de.spurtikus.vxi.beans;

import java.io.PrintStream;

import org.jvnet.hk2.annotations.Service;

/**
 * A component for creating personal greetings.
 */
public class Greeter {
    public void greet(PrintStream to, String name) {
        to.println(createGreeting(name));
    }

    public String createGreeting(String name) {
        return "Hello, " + name + "!";
    }
}

