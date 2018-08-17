package edu.usu.cs.mas.managedisaster.service.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConfigTest {
	
	private Config config = new ConfigImpl();

	@Test
	public void testGetString() {
		String coalitionStarter = config.getString("edu.usu.cs.mas.managedisaster.coalitionstarter");
		assertEquals("firestations", coalitionStarter);
	}

}
