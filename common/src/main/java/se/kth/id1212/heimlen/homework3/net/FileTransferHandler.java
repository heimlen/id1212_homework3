package se.kth.id1212.heimlen.homework3.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

/**
 * File handling over a TCP socket using NIO
 */
public class FileTransferHandler {
    private static final int bufferSize = 4096;

    /**
     * Handles receiving of a file over a TCP socket.
     *
     * @param channel The channel to receive the file from.
     * @param filename The name of the file to be uploaded.
     * @throws IOException If something goes wrong with the file transfer.
     */
    public static void receiveFile(SocketChannel channel, String filename) throws IOException {
        Path path = getServerPath(filename);

        try (FileChannel fileChannel = FileChannel.open(path,
                EnumSet.of(StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE))) {

            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            while (channel.read(buffer) > 0) {
                buffer.flip();
                fileChannel.write(buffer);
                buffer.clear();
            }

            System.out.println("File received!");
            channel.shutdownInput();
        }
    }

    /**
     * Handles sending of a file over a TCP socket.
     *
     * @param channel The channel to receive the file from.
     * @param filename The file to send.
     * @throws IOException If something goes wrong with the file transfer.
     */
    public static void sendFile(SocketChannel channel, String filename) throws IOException {
        Path path = getClientPath(filename);

        try (FileChannel fileChannel = FileChannel.open(path)) {
            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            while (fileChannel.read(buffer) > 0) {
                buffer.flip();
                channel.write(buffer);
                buffer.clear();
            }

            channel.shutdownOutput();
        }
    }

    /**
     * Returns the path to the local file on the client.
     * @param localFilename the file to return path for
     * @return the path to the @param localFilename
     */
    public static Path getClientPath(String localFilename) {
        return Paths.get("client/clientDirectory", localFilename);
    }

    /**
     * Returns the path to the remote file on the server.
     * @param remoteFilename the file to return path for
     * @return the path to the @param remoteFilename
     */

    public static Path getServerPath(String remoteFilename) {
        return Paths.get("server/serverDirectory", remoteFilename);
    }
}
