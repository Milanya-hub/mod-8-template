package hse.java.practice.echochat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class EchoChatServer {
    private static final Set<ClientHandler> clients = ConcurrentHashMap.newKeySet(); // потокобезопасный
    static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java EchoChatServer <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Chat server started on port " + port);
            while (true) {
                Socket socket = serverSocket.accept(); // соед с конкретный клиентом
                ClientHandler clientHandler = new ClientHandler(socket); // обьект для обслуживания клиента
                Thread thread = new Thread(clientHandler); // тред для клиента, позволяет параллелить, принимает обьект runnable
                thread.start(); // запускает clienthandler.run
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.send(message);
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private BufferedReader reader;
        private BufferedWriter writer;
        private String nickname;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
                requestNickname();
                clients.add(this);

                broadcast("[Сервер] " + nickname + " вошёл в чат. Участников: " + clients.size(), this);
                send("[Сервер] " + nickname + " вошёл в чат");

                String message;
                while ((message = reader.readLine()) != null) { // null -- клиент ушел

                    message = message.trim(); // удаляет пробелы переносы табуляции
                    if (message.isEmpty()) {
                        continue;
                    }
                    broadcast("[" + nickname + "] " + message, this);
                }

            } catch (IOException ignored) {
            } finally {
                disconnect();
            }
        }

        private void requestNickname() throws IOException {
            while (true) {
                send("Введите никнейм:");
                String input = reader.readLine();
                if (input == null) {
                    throw new IOException("Client disconnected before nickname");
                }

                input = input.trim();

                if (!input.isEmpty()) {
                    nickname = input;
                    return;
                }
            }
        }

        private void send(String message) {
            try {
                writer.write(message);
                writer.newLine();
                writer.flush();
            } catch (IOException ignored) {
            }
        }

        private void disconnect() {
            clients.remove(this);

            if (nickname != null) {
                broadcast("[Сервер] " + nickname + " покинул чат. Участников: " + clients.size(), this);
            }

            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }
}