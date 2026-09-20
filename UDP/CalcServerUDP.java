package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class CalcServerUDP {

    private static final int DEFAULT_PORT = 5001;
    private static final int BUFFER_SIZE = 2048;

    public static void main(String[] args) {

        int port = DEFAULT_PORT;
        double lossRate = 0.0;

        try {

            for (int i = 0; i < args.length; i++) {

                switch (args[i]) {

                    case "--loss-rate":

                        if (i + 1 >= args.length) {
                            throw new IllegalArgumentException(
                                    "Falta valor para --loss-rate"
                            );
                        }

                        lossRate =
                                Double.parseDouble(args[++i]);

                        break;

                    case "--port":

                        if (i + 1 >= args.length) {
                            throw new IllegalArgumentException(
                                    "Falta valor para --port"
                            );
                        }

                        port =
                                Integer.parseInt(args[++i]);

                        break;

                    default:

                        throw new IllegalArgumentException(
                                "Argumento desconhecido: "
                                        + args[i]
                        );
                }
            }

            if (lossRate < 0.0 || lossRate > 1.0) {

                throw new IllegalArgumentException(
                        "--loss-rate deve estar entre 0.0 e 1.0"
                );
            }

            if (port < 1 || port > 65535) {

                throw new IllegalArgumentException(
                        "Porta inválida: " + port
                );
            }

        } catch (NumberFormatException e) {

            System.err.println(
                    "Valor numérico inválido: "
                            + e.getMessage()
            );

            printUsage();

            return;

        } catch (IllegalArgumentException e) {

            System.err.println(e.getMessage());

            printUsage();

            return;
        }

        final double configuredLossRate =
                lossRate;

        final int configuredPort =
                port;

        /*
         * Pool de threads.
         *
         * Diferentemente do TCP, o UDP não cria uma
         * conexão para cada cliente.
         *
         * O endereço IP e a porta de origem presentes
         * em cada DatagramPacket identificam o cliente.
         */
        ExecutorService pool =
                Executors.newCachedThreadPool();

        System.out.println(
                "====================================="
        );

        System.out.println(
                "      CALCULADORA UDP - SERVIDOR"
        );

        System.out.println(
                "====================================="
        );

        System.out.println(
                "Porta: " + configuredPort
        );

        System.out.printf(
                "Taxa de perda simulada: %.0f%%%n",
                configuredLossRate * 100.0
        );

        System.out.println(
                "Aguardando datagramas...\n"
        );

        try (
                DatagramSocket socket =
                        new DatagramSocket(
                                configuredPort
                        )
        ) {

            while (true) {

                byte[] receiveBuffer =
                        new byte[BUFFER_SIZE];

                DatagramPacket packet =
                        new DatagramPacket(
                                receiveBuffer,
                                receiveBuffer.length
                        );

                /*
                 * Bloqueia apenas aguardando o próximo
                 * datagrama.
                 */
                socket.receive(packet);

                /*
                 * Copiamos os dados porque o processamento
                 * será realizado por outra thread.
                 */
                byte[] requestData =
                        new byte[
                                packet.getLength()
                        ];

                System.arraycopy(
                        packet.getData(),
                        packet.getOffset(),
                        requestData,
                        0,
                        packet.getLength()
                );

                InetAddress clientAddress =
                        packet.getAddress();

                int clientPort =
                        packet.getPort();

                /*
                 * O processamento é enviado para uma
                 * thread do pool.
                 */
                pool.submit(
                        () -> handleRequest(
                                socket,
                                requestData,
                                clientAddress,
                                clientPort,
                                configuredLossRate
                        )
                );
            }

        } catch (IOException e) {

            System.err.println(
                    "Erro no servidor UDP: "
                            + e.getMessage()
            );

        } finally {

            pool.shutdownNow();
        }
    }

    private static void handleRequest(
            DatagramSocket socket,
            byte[] requestData,
            InetAddress clientAddress,
            int clientPort,
            double lossRate) {

        String request =
                new String(
                        requestData,
                        StandardCharsets.UTF_8
                ).trim();

        String client =
                clientAddress.getHostAddress()
                        + ":"
                        + clientPort;

        System.out.println(
                "["
                        + Thread.currentThread().getName()
                        + "] Recebido de "
                        + client
                        + ": "
                        + request
        );

        /*
         * SIMULAÇÃO DE PERDA
         *
         * Exemplo:
         *
         * lossRate = 0.1
         *
         * aproximadamente 10% dos datagramas
         * serão ignorados.
         *
         * O servidor recebeu a mensagem,
         * porém propositalmente não responde.
         */
        if (
                ThreadLocalRandom
                        .current()
                        .nextDouble()
                        < lossRate
        ) {

            System.out.println(
                    "[PERDA SIMULADA] Datagram descartado de "
                            + client
                            + ": "
                            + request
            );

            return;
        }

        String response =
                processRequest(request);

        byte[] responseData =
                response.getBytes(
                        StandardCharsets.UTF_8
                );

        DatagramPacket responsePacket =
                new DatagramPacket(
                        responseData,
                        responseData.length,
                        clientAddress,
                        clientPort
                );

        try {

            /*
             * O mesmo DatagramSocket é compartilhado
             * entre as threads.
             */
            synchronized (socket) {

                socket.send(responsePacket);
            }

            System.out.println(
                    "Enviado para "
                            + client
                            + ": "
                            + response
            );

        } catch (IOException e) {

            System.err.println(
                    "Erro ao responder para "
                            + client
                            + ": "
                            + e.getMessage()
            );
        }
    }

    private static String processRequest(
            String request) {

        String sequence = "-1";

        try {

            /*
             * Formato:
             *
             * CALC:0:10:+:5
             */
            String[] parts =
                    request.split(":");

            if (parts.length != 5) {

                return
                        "ERROR:-1:"
                        + "formato de mensagem invalido";
            }

            if (!"CALC".equals(parts[0])) {

                return
                        "ERROR:-1:"
                        + "comando invalido";
            }

            sequence =
                    parts[1];

            int sequenceNumber =
                    Integer.parseInt(sequence);

            if (sequenceNumber < 0) {

                return
                        "ERROR:"
                        + sequence
                        + ":numero de sequencia invalido";
            }

            double operand1 =
                    Double.parseDouble(
                            parts[2]
                    );

            String operator =
                    parts[3];

            double operand2 =
                    Double.parseDouble(
                            parts[4]
                    );

            double result;

            switch (operator) {

                case "+":

                    result =
                            operand1
                            + operand2;

                    break;

                case "-":

                    result =
                            operand1
                            - operand2;

                    break;

                case "*":

                    result =
                            operand1
                            * operand2;

                    break;

                case "/":

                    if (operand2 == 0.0) {

                        return
                                "ERROR:"
                                + sequence
                                + ":divisao por zero";
                    }

                    result =
                            operand1
                            / operand2;

                    break;

                default:

                    return
                            "ERROR:"
                            + sequence
                            + ":operacao invalida";
            }

            return
                    "RESULT:"
                    + sequence
                    + ":"
                    + result;

        } catch (NumberFormatException e) {

            return
                    "ERROR:"
                    + sequence
                    + ":numero invalido";

        } catch (Exception e) {

            return
                    "ERROR:"
                    + sequence
                    + ":erro ao processar requisicao";
        }
    }

    private static void printUsage() {

        System.out.println(
                "Uso:"
        );

        System.out.println(
                "  java CalcServerUDP "
                        + "[--loss-rate 0.0..1.0] "
                        + "[--port porta]"
        );

        System.out.println(
                "Exemplo:"
        );

        System.out.println(
                "  java CalcServerUDP "
                        + "--loss-rate 0.1"
        );
    }
}