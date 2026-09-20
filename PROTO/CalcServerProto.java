import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CalcServerProto {

    private static final int PORT = 5000;

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   CALCULADORA PROTOBUF - SERVIDOR");
        System.out.println("=====================================");
        System.out.println("Porta TCP: " + PORT);
        System.out.println("Aguardando clientes...\n");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {

                Socket clientSocket = serverSocket.accept();

                System.out.println(
                        "Cliente conectado: "
                                + clientSocket.getInetAddress().getHostAddress()
                                + ":"
                                + clientSocket.getPort()
                );

                Thread clientThread = new Thread(
                        () -> handleClient(clientSocket)
                );

                clientThread.start();
            }

        } catch (IOException e) {

            System.err.println(
                    "Erro no servidor: "
                            + e.getMessage()
            );
        }
    }

    private static void handleClient(Socket socket) {

        try (
                socket;
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream()
        ) {

            while (true) {
                CalculatorProto.CalcRequest request =
                        CalculatorProto.CalcRequest
                                .parseDelimitedFrom(input);
                if (request == null) {
                    break;
                }
                System.out.printf(
                        "[%s] Req %d: %.2f %s %.2f%n",
                        Thread.currentThread().getName(),
                        request.getSequence(),
                        request.getOperand1(),
                        request.getOperator(),
                        request.getOperand2()
                );

                CalculatorProto.CalcResponse response =
                        processRequest(request);

                response.writeDelimitedTo(output);
                output.flush();

                if (response.getSuccess()) {

                    System.out.printf(
                            "[%s] Resp %d: %.6f%n",
                            Thread.currentThread().getName(),
                            response.getSequence(),
                            response.getResult()
                    );

                } else {

                    System.out.printf(
                            "[%s] Erro %d: %s%n",
                            Thread.currentThread().getName(),
                            response.getSequence(),
                            response.getErrorMessage()
                    );
                }
            }

        } catch (IOException e) {

            System.err.println(
                    "Erro na comunicação com cliente: "
                            + e.getMessage()
            );

        } finally {

            System.out.println(
                    "Cliente desconectado."
            );
        }
    }

    private static CalculatorProto.CalcResponse processRequest(
            CalculatorProto.CalcRequest request) {

        int sequence = request.getSequence();
        double operand1 = request.getOperand1();
        double operand2 = request.getOperand2();
        String operator = request.getOperator();

        CalculatorProto.CalcResponse.Builder response =
                CalculatorProto.CalcResponse
                        .newBuilder()
                        .setSequence(sequence);

        try {

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

                    if (operand2 == 0.0) {
                        throw new ArithmeticException(
                                "divisao por zero"
                        );
                    }

                    result = operand1 / operand2;
                    break;

                default:
                    throw new IllegalArgumentException(
                            "operacao invalida"
                    );
            }

            return response
                    .setSuccess(true)
                    .setResult(result)
                    .build();

        } catch (ArithmeticException
                 | IllegalArgumentException e) {

            return response
                    .setSuccess(false)
                    .setErrorMessage(e.getMessage())
                    .build();
        }
    }
}
