package ru.geekbrains.com;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8189;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public static void main(String[] args) {
        EchoClient echoClient = new EchoClient();
        echoClient.start();

    }

    private void start() {
        try{
            openConnection();
            final Scanner scanner = new Scanner(System.in);
            while (true){
                final String msg = scanner.nextLine();
                out.writeUTF(msg);
                if("/end".equalsIgnoreCase(msg)){
                    //в этом месте было бы логично завершить сеанс....
                    break;
                }
            }
            scanner.close();

        }catch (Exception e){
            e.getStackTrace();
        }finally {
            closeConnection();
        }
    }

    private void closeConnection() {
        if (socket != null){
            try{
                socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        if (out != null){
            try {
                out.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    System.out.println("Сеанс связи завершен");

    private void openConnection() throws IOException {
        socket = new Socket(SERVER_ADDR, SERVER_PORT);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        if (socket != null) {
            System.out.println("Подключение к серверу по адресу: " + SERVER_ADDR + " порт : " + SERVER_PORT + " успешно");
            System.out.println("Введите Ваше сообщение");

        }
        new Thread(()->
        {
            try {
                while (true) {
                    if (socket != null && in != null){
                        final String msg = in.readUTF();
                        System.out.println(msg);
                        if ("/end".equalsIgnoreCase(msg))
                            break;
                    }
                    else{
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}


