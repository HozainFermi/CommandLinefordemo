package com.lab1.commandline;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Calendar;
import java.util.List;

public class Server extends Thread {

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    public static PrintWriter out;
    private static BufferedReader in;
    private static String command;
    private static String resp;

    public static String fullpath = HelloController.class.getProtectionDomain().getCodeSource().getLocation().getPath();

   static Calendar now = Calendar.getInstance();


    public static void start (int port) throws IOException {

        fullpath=fullpath.replace("main/CommandLine-1.0-SNAPSHOT-shaded.jar","");
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
            now = Calendar.getInstance();
            try {

                if ((command=in.readLine())!=null) {
                    if (command=="shotdownCL"){
                        shotdown();
                        long pid = ProcessHandle.current().pid();

                        System.exit(0);
                    }
                    else {
                        System.out.println("Command:" + command);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(command != null) {
                try {
                    builder.command("sh", "-c", command);
                    builder.directory(new File(fullpath));

                  Process process = builder.start();
                    BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    HelloController.Log.add("["+now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+"]"+command);
                    for(resp=output.readLine(); resp!=null;resp=output.readLine()) {
                        out.println(resp);
                        System.out.println("Respons:" + resp);
                        HelloController.Log.add("[Response:]"+resp);
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

        clientSocket.close();
        serverSocket.close();
        in.close();
        out.close();
    }



}
