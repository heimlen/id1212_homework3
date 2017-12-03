package se.kth.id1212.heimlen.homework3.net;

import se.kth.id1212.heimlen.homework3.controller.Controller;
import se.kth.id1212.heimlen.homework3.dto.AccountSocketIDDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.channels.SocketChannel;

/**
 * Handles client connections on the server
 */
public class ConnectHandler {
    ConnectHandler(Controller controller, SocketChannel socketChannel) throws IOException {
        ObjectInputStream inputStream = new ObjectInputStream(socketChannel.socket().getInputStream());

        attachToUser(controller, inputStream, socketChannel);
    }

    private void attachToUser(Controller controller, ObjectInputStream inputStream, SocketChannel socketChannel) {
        try {
            AccountSocketIDDTO socketId = (AccountSocketIDDTO) inputStream.readObject();
            controller.attachSocketToUser(socketId.getId(), socketChannel);

            System.out.println(String.format("The userId: %d was associated with a socket!", socketId.getId()));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
