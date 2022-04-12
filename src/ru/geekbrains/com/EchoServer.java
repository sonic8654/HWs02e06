package ru.geekbrains.com;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {
    private static Throwable e;

    public static void main(String[] args) {
        Socket socket = null;
        try (ServerSocket serverSocket = new ServerSocket(8189)){
            System.out.println("Сервер был запущен, ожидание подключения...");
            socket = serverSocket.accept();
            System.out.println("Клиент подключился");
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream();

            new Thread(()->
            {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    final String msg = scanner.nextLine();
                    //if("/end".equalsIgnoreCase(msg))break;
                    try {
                        out.writeUTF(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            while (true) {
                String str = in.readUTF();
                System.out.println("Сообщение клиента: " +str);
                out.writeUTF("Эхо: " + str);
                if ("/end".equals(str)){
                    break;
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
            if (socket != null){
                try {
                    socket.close();
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
            System.out.println("Сервер был остановлен");
            System.exit(0);

        }
    }
}
