/*
 * CopyRight (c) 2012-2015 Hikvision Co, Ltd. All rights reserved. Filename:
 * Test.java Creator: joe.zhao(zhaohaolin@hikvision.com.cn) Create-Date:
 * 下午12:37:23
 */
package org.dna.mqtt.moquette;

import java.io.IOException;

import org.dna.mqtt.moquette.server.Server;

/**
 * TODO
 * 
 * @author joe.zhao(zhaohaolin@hikvision.com.cn)
 * @version $Id: Test, v 0.1 2015年5月13日 下午12:37:23 Exp $
 */
public class Test {
	
	private Server					m_server;
	
	protected void startServer() throws IOException {
		m_server = new Server();
		m_server.startServer();
	}
	
	public void setUp() throws Exception {
		startServer();
	}
	
	public static void main(String[] args) throws Exception {
		Test test = new Test();
		test.setUp();
	}
	
}
