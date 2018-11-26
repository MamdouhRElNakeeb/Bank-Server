package com.bankserver;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {

    protected Socket socket;

    public ServerThread(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
        InputStream inp = null;
        BufferedReader brinp = null;
        DataOutputStream out = null;
        try {
            inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }
        System.out.println("connected");

        BankService bankService = new BankService(socket);

        Thread thread = new Thread(bankService);
        thread.start();

    }
}