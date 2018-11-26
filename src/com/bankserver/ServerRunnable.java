
package com.bankserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerRunnable implements Runnable {

    private int BANK_PORT;

	public ServerRunnable(int port) {
		BANK_PORT = port;
	}

	public void run() {

		ServerSocket bank1SC = null;
		Socket bank1S = null;

		try {
			bank1SC = new ServerSocket(BANK_PORT);
		} catch (IOException e) {
			e.printStackTrace();

		}

		while (true) {
			try {
				bank1S = bank1SC.accept();
			} catch (IOException e) {
				System.out.println("I/O error: " + e);
			}
			// new thread for a client
			new ServerThread(bank1S).start();
		}
	}
}
