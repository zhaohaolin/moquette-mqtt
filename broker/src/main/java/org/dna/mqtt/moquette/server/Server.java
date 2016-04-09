package org.dna.mqtt.moquette.server;

import java.io.File;
import java.io.IOException;
import org.dna.mqtt.moquette.messaging.spi.impl.SimpleMessaging;
import org.dna.mqtt.moquette.server.mina.MinaAcceptor;
import org.dna.mqtt.moquette.server.netty.NettyAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Launch a configured version of the server.
 * 
 * @author andrea
 */
public class Server {
	
	private static final Logger	LOG						= LoggerFactory
																.getLogger(Server.class);
	
	public static final String	STORAGE_FILE_PATH		= System.getProperty("user.home")
																+ File.separator
																+ "moquette_store.hawtdb";
	public static String		NETTY_NIO_FRAMEWORK		= "netty";
	public static String		MINA_NIO_FRAMEWORK		= "mina";
	public static String		DEFAULT_NIO_FRAMEWORK	= NETTY_NIO_FRAMEWORK;
	
	private ServerAcceptor		acceptor;
	private SimpleMessaging		messaging;
	
	public static void main(String[] args) throws IOException {
		String nioFrameworkType = DEFAULT_NIO_FRAMEWORK;
		if (args.length > 0) {
			// TODO check the typing and in case print usge message
			nioFrameworkType = args[1];
			if (!(nioFrameworkType.equals(DEFAULT_NIO_FRAMEWORK) || nioFrameworkType
					.equals(DEFAULT_NIO_FRAMEWORK))) {
				printUsage(nioFrameworkType);
				return;
			}
		}
		
		final Server server = new Server();
		server.startServer(nioFrameworkType);
		// Bind a shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				server.stopServer();
			}
		});
		
	}
	
	public void startServer() throws IOException {
		startServer(DEFAULT_NIO_FRAMEWORK);
	}
	
	public void startServer(String nioFrameworkType) throws IOException {
		System.out.println("Starting server with " + nioFrameworkType
				+ " connectors");
		messaging = SimpleMessaging.getInstance();
		messaging.init();
		
		if (nioFrameworkType.equals(MINA_NIO_FRAMEWORK)) {
			acceptor = new MinaAcceptor();
		} else if (nioFrameworkType.equals(NETTY_NIO_FRAMEWORK)) {
			acceptor = new NettyAcceptor();
		}
		acceptor.initialize(messaging);
	}
	
	public void stopServer() {
		System.out.println("Server stopping...");
		messaging.stop();
		acceptor.close();
		System.out.println("Server stopped");
	}
	
	private static void printUsage(String nioFrameworkType) {
		System.out
				.println("The broker should be invoked with the following command line: \n");
		System.out.println("> java -jar <broker.jar> [mina|netty]\n");
		System.out.println("while was invoked with type: " + nioFrameworkType);
	}
	
}
