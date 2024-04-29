package com.lab1.commandline;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.List;

public class Server extends Thread {

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static String command;
    private static String resp;


    public static void start (int port) throws IOException {


            serverSocket = new ServerSocket(port);
            System.out.println("Server is ready, waiting for connections");
            clientSocket = serverSocket.accept();
            System.out.println("Connection accepted");
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void run() {
       ProcessBuilder builder = new ProcessBuilder();


        while (true){
            try {
                if ((command=in.readLine())!=null) {

                    System.out.println("Command:" + command);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(command != null) {


                try {


                    builder.command("sh", "-c", command);
                    builder.directory(new File("/home/me/IdeaProjects/demo/src/main/java/com/lab1/demo/"));

                  Process process = builder.start();
                    BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    HelloController.Log.add(command);
                    for(resp=output.readLine(); resp!=null;resp=output.readLine()) {
                        out.println(resp);
                        System.out.println("Respons:" + resp);
                        HelloController.Log.add(resp);
                    }

                    output.close();
                    process.destroyForcibly();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }




    public void shotdown() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }



}
