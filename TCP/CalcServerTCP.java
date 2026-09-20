package TCP;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Nome: Vagner Henrique - Pós Graduação
 * Matricula: 603486
 * Professor: Paulo Rego 
 * Disciplina: Sistemas Distribuídos / Capítulo 4 (Comunicação entre Processos)
 * 
 * CalcServerTCP
 */

public class CalcServerTCP {

    private static final int PORT = 5000;

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("      CALCULADORA TCP - SERVIDOR V 1.0 ");
        System.out.println("=====================================");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Servidor iniciado.");
            System.out.println("Porta: " + PORT);
            System.out.println("Aguardando clientes...\n");

            while (true) {

                Socket clientSocket = serverSocket.accept();

                System.out.println(
                        "Novo cliente conectado: "
                        + clientSocket.getInetAddress().getHostAddress()
                        + ":"
                        + clientSocket.getPort()
                );

                /*
                 * Cada cliente é executado em uma thread independente.
                 *
                 * Isso permite que vários clientes sejam atendidos
                 * simultaneamente sem bloquear o servidor.
                 */
                Thread clientThread =
                        new Thread(new ClientHandler(clientSocket));

                clientThread.start();
            }

        } catch (IOException e) {

            System.err.println(
                    "Erro ao iniciar servidor: "
                    + e.getMessage()
            );
        }
    }


    /**
     * Classe responsável por atender individualmente cada cliente.
     */
    private static class ClientHandler implements Runnable {

        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            try (
                    BufferedReader input =
                            new BufferedReader(
                                    new InputStreamReader(
                                            socket.getInputStream()
                                    )
                            );

                    PrintWriter output =
                            new PrintWriter(
                                    socket.getOutputStream(),
                                    true
                            )
            ) {

                String request;

                /*
                 * Enquanto o cliente estiver conectado,
                 * recebe requisições.
                 */
                while ((request = input.readLine()) != null) {

                    System.out.println(
                            "[" + Thread.currentThread().getName()
                            + "] Recebido: "
                            + request
                    );

                    String response = processRequest(request);

                    output.println(response);

                    System.out.println(
                            "[" + Thread.currentThread().getName()
                            + "] Enviado: "
                            + response
                    );
                }

            } catch (IOException e) {

                System.err.println(
                        "Erro na comunicação com cliente: "
                        + e.getMessage()
                );

            } finally {

                try {
                    socket.close();

                    System.out.println(
                            "Cliente desconectado: "
                            + socket.getInetAddress().getHostAddress()
                    );

                } catch (IOException e) {

                    System.err.println(
                            "Erro ao fechar socket: "
                            + e.getMessage()
                    );
                }
            }
        }
    }


    /**
     * Processa uma requisição recebida.
     */
    private static String processRequest(String request) {

        String sequence = "-1";

        try {
            String[] parts = request.split(":");

            if (parts.length != 5) {
                return "ERROR:-1:formato de mensagem invalido";
            }

            if (!parts[0].equals("CALC")) {
                return "ERROR:-1:comando invalido";
            }

            sequence = parts[1];

            double operand1 =
                    Double.parseDouble(parts[2]);

            String operator =
                    parts[3];

            double operand2 =
                    Double.parseDouble(parts[4]);

            double result;

            switch (operator) {

                case "+":
                    result = operand1 + operand2;
                    break;

                case "-":
                    result = operand1 - operand2;
                    break;

                case "*":
                    result = operand1 * operand2;
                    break;

                case "/":

                    if (operand2 == 0) {

                        return "ERROR:"
                                + sequence
                                + ":divisao por zero";
                    }

                    result = operand1 / operand2;
                    break;

                default:

                    return "ERROR:"
                            + sequence
                            + ":operacao invalida";
            }

            return "RESULT:"
                    + sequence
                    + ":"
                    + result;

        } catch (NumberFormatException e) {

            return "ERROR:"
                    + sequence
                    + ":numero invalido";

        } catch (Exception e) {

            return "ERROR:"
                    + sequence
                    + ":erro ao processar requisicao";
        }
    }
}