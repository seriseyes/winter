package winter;

import com.google.gson.Gson;
import winter.annotations.Controller;
import winter.enums.HTTP;
import winter.log.Log;
import winter.log.LogType;
import winter.managers.ControllerManager;
import winter.parsers.Request;
import winter.utils.ClassSearcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Test
 *
 * @author Баярхүү.Лув, 2022.08.02
 */
public class WinterApplication {
    public static Gson gson = new Gson();
    static int port = 9000;

    /**
     * extend only
     */
    protected WinterApplication() {

    }

    public static void launch() {
        ClassSearcher classSearcher = new ClassSearcher();

        Log.println("Started searching for classes...", LogType.INFO);
        Set<Class> classes = classSearcher.findAllClassesUsingClassLoader("").stream().filter(Objects::nonNull).collect(Collectors.toSet());

        Log.println("Started searching for controllers...", LogType.INFO);
        ControllerManager controllerManager = new ControllerManager(classes.stream().filter(e -> e.isAnnotationPresent(Controller.class)).collect(Collectors.toList()));

        Log.println("Application Started on port " + port, LogType.INFO);

        try (ServerSocket server = new ServerSocket(port)) {
            while (true) {
                try (Socket socket = server.accept()) {
                    try (InputStream input = socket.getInputStream();
                         OutputStream output = socket.getOutputStream()) {
                        byte[] buffer = new byte[10000];
                        int total = input.read(buffer);
                        Request request = Request.of(new String(Arrays.copyOfRange(buffer, 0, total)));
                        if (request == null) {
                            output.write(HTTP.NOT_FOUND.getBytes());
                        } else {
                            output.write(controllerManager.control(request).getBytes());
                        }
                    } catch (SocketTimeoutException ex) {
                        if (Thread.currentThread().isInterrupted()) {
                            break;
                        }
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}