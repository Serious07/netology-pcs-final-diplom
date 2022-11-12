import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static int port = 8989;
    private static String ip = "localhost";

    public static void main(String[] args) {
        System.out.println("Введите искомое слово чтобы получить ответ: ");

        while (true) {
            try (Socket clientSocket = new Socket(ip, port);
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                String currentCommand = reader.readLine();

                if (currentCommand.equalsIgnoreCase("end") ||
                        currentCommand.equalsIgnoreCase("exit") ||
                        currentCommand.equalsIgnoreCase("quit")) {
                    System.out.println("Программа остановлена");
                    break;
                } else {
                    out.println(currentCommand);
                    System.out.println("Ответ от сервера:");
                    System.out.println(in.readLine());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
