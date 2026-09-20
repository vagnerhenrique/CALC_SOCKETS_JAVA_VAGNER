package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;

public class CalcClientUDP {

    private static final String DEFAULT_SERVER =
            "localhost";

    private static final int DEFAULT_PORT =
            5001;

    /*
     * Exigido pela atividade.
     */
    private static final int N =
            20;

    /*
     * Valor sugerido pela atividade.
     */
    private static final int TIMEOUT_MS =
            500;

    /*
     * Máximo de tentativas:
     *
     * tentativa 1 = envio inicial
     * tentativas 2..5 = retransmissões
     */
    private static final int MAX_ATTEMPTS =
            5;

    private static final int BUFFER_SIZE =
            2048;

    private static final Random RANDOM =
            new Random();

    public static void main(String[] args) {

        Locale.setDefault(
                Locale.US
        );

        String serverHost =
                DEFAULT_SERVER;

        int serverPort =
                DEFAULT_PORT;

        try {

            for (int i = 0; i < args.length; i++) {

                switch (args[i]) {

                    case "--host":

                        if (i + 1 >= args.length) {

                            throw new IllegalArgumentException(
                                    "Falta valor para --host"
                            );
                        }

                        serverHost =
                                args[++i];

                        break;

                    case "--port":

                        if (i + 1 >= args.length) {

                            throw new IllegalArgumentException(
                                    "Falta valor para --port"
                            );
                        }

                        serverPort =
                                Integer.parseInt(
                                        args[++i]
                                );

                        break;

                    default:

                        throw new IllegalArgumentException(
                                "Argumento desconhecido: "
                                        + args[i]
                        );
                }
            }

            if (
                    serverPort < 1
                    || serverPort > 65535
            ) {

                throw new IllegalArgumentException(
                        "Porta inválida: "
                                + serverPort
                );
            }

        } catch (NumberFormatException e) {

            System.err.println(
                    "Porta inválida."
            );

            printUsage();

            return;

        } catch (IllegalArgumentException e) {

            System.err.println(
                    e.getMessage()
            );

            printUsage();

            return;
        }

        System.out.println(
                "====================================="
        );

        System.out.println(
                "       CALCULADORA UDP - CLIENTE"
        );

        System.out.println(
                "====================================="
        );

        System.out.println(
                "Servidor: "
                        + serverHost
                        + ":"
                        + serverPort
        );

        System.out.println(
                "Requisições: "
                        + N
        );

        System.out.println(
                "Timeout: "
                        + TIMEOUT_MS
                        + " ms"
        );

        System.out.println(
                "Máximo de tentativas: "
                        + MAX_ATTEMPTS
        );

        System.out.println();

        int successfulRequests =
                0;

        int lostRequests =
                0;

        int retransmissions =
                0;

        double sumRtt =
                0.0;

        double maxRtt =
                0.0;

        /*
         * Tempo total do experimento.
         *
         * Inclui:
         *
         * envio
         * espera
         * timeout
         * retransmissões
         */
        long totalStart =
                System.nanoTime();

        try (
                DatagramSocket socket =
                        new DatagramSocket()
        ) {

            InetAddress serverAddress =
                    InetAddress.getByName(
                            serverHost
                    );

            /*
             * Envia 20 requisições,
             * uma por vez.
             */
            for (
                    int sequence = 0;
                    sequence < N;
                    sequence++
            ) {

                String request =
                        generateRequest(
                                sequence
                        );

                byte[] requestData =
                        request.getBytes(
                                StandardCharsets.UTF_8
                        );

                boolean answered =
                        false;

                System.out.println(
                        "Requisição "
                                + sequence
                                + ": "
                                + request
                );

                /*
                 * Tenta até MAX_ATTEMPTS.
                 */
                for (
                        int attempt = 1;
                        attempt <= MAX_ATTEMPTS
                                && !answered;
                        attempt++
                ) {

                    if (attempt > 1) {

                        retransmissions++;

                        System.out.println(
                                "  Retransmissão "
                                        + (attempt - 1)
                                        + " (tentativa "
                                        + attempt
                                        + "/"
                                        + MAX_ATTEMPTS
                                        + ")"
                        );

                    } else {

                        System.out.println(
                                "  Envio inicial "
                                        + "(tentativa 1/"
                                        + MAX_ATTEMPTS
                                        + ")"
                        );
                    }

                    DatagramPacket requestPacket =
                            new DatagramPacket(
                                    requestData,
                                    requestData.length,
                                    serverAddress,
                                    serverPort
                            );

                    /*
                     * Começa a medir RTT da tentativa.
                     */
                    long attemptStart =
                            System.nanoTime();

                    socket.send(
                            requestPacket
                    );

                    /*
                     * Define o instante limite
                     * para esta tentativa.
                     */
                    long deadline =
                            attemptStart
                            + TIMEOUT_MS
                            * 1_000_000L;

                    while (!answered) {

                        long remainingNanos =
                                deadline
                                - System.nanoTime();

                        if (
                                remainingNanos
                                <= 0
                        ) {

                            System.out.println(
                                    "  TIMEOUT após "
                                            + TIMEOUT_MS
                                            + " ms."
                            );

                            break;
                        }

                        int remainingMs =
                                (int) Math.max(
                                        1L,
                                        (
                                            remainingNanos
                                            + 999_999L
                                        )
                                        / 1_000_000L
                                );

                        /*
                         * Timeout do DatagramSocket.
                         */
                        socket.setSoTimeout(
                                remainingMs
                        );

                        byte[] responseBuffer =
                                new byte[
                                        BUFFER_SIZE
                                ];

                        DatagramPacket responsePacket =
                                new DatagramPacket(
                                        responseBuffer,
                                        responseBuffer.length
                                );

                        try {

                            /*
                             * Aguarda a resposta.
                             */
                            socket.receive(
                                    responsePacket
                            );

                            /*
                             * Verifica se a resposta veio
                             * do servidor esperado.
                             */
                            if (
                                    !responsePacket
                                            .getAddress()
                                            .equals(
                                                    serverAddress
                                            )
                                    ||
                                    responsePacket
                                            .getPort()
                                            != serverPort
                            ) {

                                System.out.println(
                                        "  Datagram ignorado: "
                                        + "origem inesperada."
                                );

                                continue;
                            }

                            String response =
                                    new String(
                                            responsePacket.getData(),
                                            responsePacket.getOffset(),
                                            responsePacket.getLength(),
                                            StandardCharsets.UTF_8
                                    );

                            /*
                             * Confere o número da sequência.
                             */
                            int responseSequence =
                                    extractSequence(
                                            response
                                    );

                            if (
                                    responseSequence
                                    != sequence
                            ) {

                                System.out.println(
                                        "  Resposta ignorada "
                                        + "(sequência "
                                        + responseSequence
                                        + ", esperada "
                                        + sequence
                                        + ")."
                                );

                                continue;
                            }

                            long attemptEnd =
                                    System.nanoTime();

                            double rtt =
                                    (
                                        attemptEnd
                                        - attemptStart
                                    )
                                    / 1_000_000.0;

                            successfulRequests++;

                            sumRtt +=
                                    rtt;

                            maxRtt =
                                    Math.max(
                                            maxRtt,
                                            rtt
                                    );

                            answered =
                                    true;

                            System.out.println(
                                    "  Resposta: "
                                            + response
                            );

                            System.out.printf(
                                    "  RTT da tentativa "
                                    + "bem-sucedida: "
                                    + "%.3f ms%n",
                                    rtt
                            );

                        } catch (
                                SocketTimeoutException e
                        ) {

                            System.out.println(
                                    "  TIMEOUT após "
                                            + TIMEOUT_MS
                                            + " ms."
                            );

                            break;
                        }
                    }
                }

                /*
                 * Não houve resposta depois
                 * das cinco tentativas.
                 */
                if (!answered) {

                    lostRequests++;

                    System.out.println(
                            "  PERDA DEFINITIVA: "
                            + "máximo de tentativas "
                            + "esgotado."
                    );
                }

                System.out.println(
                        "-------------------------------------"
                );
            }

        } catch (IOException e) {

            System.err.println(
                    "Erro na comunicação UDP: "
                            + e.getMessage()
            );
        }

        long totalEnd =
                System.nanoTime();

        double totalTimeMs =
                (
                    totalEnd
                    - totalStart
                )
                / 1_000_000.0;

        double averageRtt =
                successfulRequests > 0
                ?
                sumRtt
                / successfulRequests
                :
                0.0;

        /*
         * Resultado solicitado pela atividade.
         */
        System.out.println();

        System.out.println(
                "====================================="
        );

        System.out.println(
                "        RESULTADO DO EXPERIMENTO"
        );

        System.out.println(
                "====================================="
        );

        System.out.println(
                "Protocolo: UDP"
        );

        System.out.println(
                "Requisições previstas: "
                        + N
        );

        System.out.println(
                "Requisições respondidas: "
                        + successfulRequests
        );

        System.out.println(
                "Requisições perdidas definitivamente: "
                        + lostRequests
        );

        System.out.println(
                "Retransmissões: "
                        + retransmissions
        );

        System.out.printf(
                "Tempo total: %.3f ms%n",
                totalTimeMs
        );

        System.out.printf(
                "RTT médio "
                + "(requisições respondidas): "
                + "%.3f ms%n",
                averageRtt
        );

        System.out.printf(
                "RTT máximo "
                + "(requisições respondidas): "
                + "%.3f ms%n",
                maxRtt
        );
    }

    private static String generateRequest(
            int sequence) {

        double operand1 =
                Math.round(
                        RANDOM.nextDouble()
                        * 10000.0
                )
                / 100.0;

        double operand2 =
                Math.round(
                        RANDOM.nextDouble()
                        * 10000.0
                )
                / 100.0;

        String[] operators = {
                "+",
                "-",
                "*",
                "/"
        };

        String operator =
                operators[
                        RANDOM.nextInt(
                                operators.length
                        )
                ];

        /*
         * Gera ocasionalmente divisão por zero
         * para testar ERROR.
         */
        if (
                operator.equals("/")
                &&
                RANDOM.nextInt(10) == 0
        ) {

            operand2 =
                    0.0;
        }

        return String.format(
                Locale.US,
                "CALC:%d:%.2f:%s:%.2f",
                sequence,
                operand1,
                operator,
                operand2
        );
    }

    /**
     * Obtém o número de sequência
     * de:
     *
     * RESULT:n:resultado
     *
     * ou:
     *
     * ERROR:n:mensagem
     */
    private static int extractSequence(
            String response) {

        try {

            String[] parts =
                    response.split(
                            ":",
                            3
                    );

            if (parts.length < 3) {

                return -1;
            }

            if (
                    !"RESULT".equals(
                            parts[0]
                    )
                    &&
                    !"ERROR".equals(
                            parts[0]
                    )
            ) {

                return -1;
            }

            return Integer.parseInt(
                    parts[1]
            );

        } catch (
                NumberFormatException e
        ) {

            return -1;
        }
    }

    private static void printUsage() {

        System.out.println(
                "Uso:"
        );

        System.out.println(
                "  java CalcClientUDP "
                        + "[--host endereco] "
                        + "[--port porta]"
        );

        System.out.println(
                "Exemplo:"
        );

        System.out.println(
                "  java CalcClientUDP "
                        + "--host localhost "
                        + "--port 5001"
        );
    }
}