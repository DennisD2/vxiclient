package de.spurtikus.vxi.service;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.spurtikus.vxi.beans.Greeter;

@RunWith(Arquillian.class)
public class InjectionTest {
	@Inject
	Greeter greeter;

	@Test
	public void should_create_greeting() {
	    Assert.assertEquals("Hello, Earthling!",
	        greeter.createGreeting("Earthling"));
	    greeter.greet(System.out, "Earthling");
	}
	
}
