import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        //System.out.println(engine.search("бизнес"));

        try (ServerSocket serverSocket = new ServerSocket(8989);) {
            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream());
                ) {
                    String wordToSearch = in.readLine();

                    Type listType = new TypeToken<List<PageEntry>>() {
                    }.getType();

                    Gson gson = new Gson();
                    String json = gson.toJson(engine.search(wordToSearch), listType);
                    out.println(json);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}