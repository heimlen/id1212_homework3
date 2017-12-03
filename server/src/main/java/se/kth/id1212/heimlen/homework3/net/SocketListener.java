package se.kth.id1212.heimlen.homework3.net;

import se.kth.id1212.heimlen.homework3.constants.Constants;
import se.kth.id1212.heimlen.homework3.controller.Controller;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CompletableFuture;

/**
 * Handles client connections via sockets.
 */
public class SocketListener implements Runnable {
        private Controller controller;

        public SocketListener(Controller controller) {
            this.controller = controller;

            Thread listener = new Thread(this);
            listener.setPriority(Thread.MAX_PRIORITY);
            listener.start();
        }

        @Override
        public void run() {
            try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
                serverSocket.socket().bind(new InetSocketAddress(Constants.SOCKET_PORT));

                while (true) {
                    SocketChannel client = serverSocket.accept();
                    System.out.println("Connection established: " + client.getRemoteAddress());

                    CompletableFuture.runAsync(() -> {
                        try {
                            new ConnectHandler(controller, client);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (BindException e) {
                System.err.println(String.format("Port %d is already in use!", Constants.SOCKET_PORT));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}

