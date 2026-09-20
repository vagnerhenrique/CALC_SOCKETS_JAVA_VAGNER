
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;

/**
 * Cliente TCP da calculadora utilizando Protocol Buffers.
 *
 * Envia N = 20 requisições, mede RTT e compara o tamanho
 * das mensagens protobuf com o protocolo textual da Parte 2.
 */
public class CalcClientProto {

    private static final String SERVER_ADDRESS =
            "localhost";

    private static final int SERVER_PORT =
            5000;

    private static final int N =
            20;

    private static final Random RANDOM =
            new Random();

    public static void main(String[] args) {

        Locale.setDefault(Locale.US);

        System.out.println("=====================================");
        System.out.println("    CALCULADORA PROTOBUF - CLIENTE");
        System.out.println("=====================================");
        System.out.println(
                "Servidor: "
                        + SERVER_ADDRESS
                        + ":"
                        + SERVER_PORT
        );

        System.out.println(
                "Quantidade de requisicoes: "
                        + N
        );

        System.out.println();

        int successfulRequests = 0;

        double totalRttMs = 0.0;
        double maxRttMs = 0.0;

        long totalProtoRequestBytes = 0;
        long totalProtoResponseBytes = 0;

        long totalTextRequestBytes = 0;
        long totalTextResponseBytes = 0;

        long experimentStart =
                System.nanoTime();

        try (
                Socket socket =
                        new Socket(
                                SERVER_ADDRESS,
                                SERVER_PORT
                        );

                InputStream input =
                        socket.getInputStream();

                OutputStream output =
                        socket.getOutputStream()
        ) {

            System.out.println(
                    "Conectado ao servidor.\n"
            );

            for (
                    int sequence = 0;
                    sequence < N;
                    sequence++
            ) {

                double operand1 =
                        randomOperand();

                double operand2 =
                        randomOperand();

                String operator =
                        randomOperator();

                /*
                 * Ocasionalmente gera divisão por zero
                 * para verificar o tratamento de ERROR.
                 */
                if (
                        operator.equals("/")
                        && RANDOM.nextInt(10) == 0
                ) {
                    operand2 = 0.0;
                }

                CalculatorProto.CalcRequest request =
                        CalculatorProto.CalcRequest
                                .newBuilder()
                                .setSequence(sequence)
                                .setOperand1(operand1)
                                .setOperator(operator)
                                .setOperand2(operand2)
                                .build();

                /*
                 * Representação textual equivalente à Parte 2.
                 * Ela NÃO é enviada: é usada somente
                 * para comparar o tamanho das mensagens.
                 */
                String textRequest =
                        String.format(
                                Locale.US,
                                "CALC:%d:%.2f:%s:%.2f",
                                sequence,
                                operand1,
                                operator,
                                operand2
                        );

                int protoRequestBytes =
                        request.getSerializedSize();

                int textRequestBytes =
                        textRequest
                                .getBytes(
                                        StandardCharsets.UTF_8
                                )
                                .length;

                /*
                 * Mede o RTT do envio da requisição
                 * até a chegada da resposta.
                 */
                long start =
                        System.nanoTime();

                request.writeDelimitedTo(output);
                output.flush();

                CalculatorProto.CalcResponse response =
                        CalculatorProto.CalcResponse
                                .parseDelimitedFrom(input);

                long end =
                        System.nanoTime();

                if (response == null) {

                    System.err.println(
                            "Servidor encerrou a conexao."
                    );

                    break;
                }

                if (
                        response.getSequence()
                        != sequence
                ) {

                    System.err.printf(
                            "Sequencia inesperada. "
                            + "Esperada=%d, recebida=%d%n",
                            sequence,
                            response.getSequence()
                    );

                    continue;
                }

                double rttMs =
                        (end - start)
                        / 1_000_000.0;

                successfulRequests++;

                totalRttMs +=
                        rttMs;

                maxRttMs =
                        Math.max(
                                maxRttMs,
                                rttMs
                        );

                int protoResponseBytes =
                        response.getSerializedSize();

                String textResponse;

                if (response.getSuccess()) {

                    textResponse =
                            "RESULT:"
                                    + response.getSequence()
                                    + ":"
                                    + response.getResult();

                } else {

                    textResponse =
                            "ERROR:"
                                    + response.getSequence()
                                    + ":"
                                    + response.getErrorMessage();
                }

                int textResponseBytes =
                        textResponse
                                .getBytes(
                                        StandardCharsets.UTF_8
                                )
                                .length;

                totalProtoRequestBytes +=
                        protoRequestBytes;

                totalProtoResponseBytes +=
                        protoResponseBytes;

                totalTextRequestBytes +=
                        textRequestBytes;

                totalTextResponseBytes +=
                        textResponseBytes;

                System.out.println(
                        "Requisicao "
                                + sequence
                );

                System.out.println(
                        "  Operacao: "
                                + textRequest
                );

                if (response.getSuccess()) {

                    System.out.println(
                            "  Resposta: RESULT:"
                                    + sequence
                                    + ":"
                                    + response.getResult()
                    );

                } else {

                    System.out.println(
                            "  Resposta: ERROR:"
                                    + sequence
                                    + ":"
                                    + response
                                            .getErrorMessage()
                    );
                }

                System.out.printf(
                        "  RTT: %.3f ms%n",
                        rttMs
                );

                System.out.println(
                        "  Tamanho requisicao:"
                );

                System.out.println(
                        "    Protobuf: "
                                + protoRequestBytes
                                + " bytes"
                );

                System.out.println(
                        "    Texto:    "
                                + textRequestBytes
                                + " bytes"
                );

                System.out.println(
                        "  Tamanho resposta:"
                );

                System.out.println(
                        "    Protobuf: "
                                + protoResponseBytes
                                + " bytes"
                );

                System.out.println(
                        "    Texto:    "
                                + textResponseBytes
                                + " bytes"
                );

                System.out.println(
                        "-------------------------------------"
                );
            }

        } catch (ConnectException e) {

            System.err.println(
                    "Nao foi possivel conectar ao servidor."
            );

            System.err.println(
                    "Execute primeiro o CalcServerProto."
            );

            return;

        } catch (IOException e) {

            System.err.println(
                    "Erro de comunicacao: "
                            + e.getMessage()
            );

            return;
        }

        long experimentEnd =
                System.nanoTime();

        double totalTimeMs =
                (experimentEnd - experimentStart)
                / 1_000_000.0;

        if (successfulRequests == 0) {

            System.out.println(
                    "Nenhuma requisicao concluida."
            );

            return;
        }

        double averageRtt =
                totalRttMs
                / successfulRequests;

        double avgProtoRequest =
                (double) totalProtoRequestBytes
                / successfulRequests;

        double avgProtoResponse =
                (double) totalProtoResponseBytes
                / successfulRequests;

        double avgTextRequest =
                (double) totalTextRequestBytes
                / successfulRequests;

        double avgTextResponse =
                (double) totalTextResponseBytes
                / successfulRequests;

        /*
         * Considera requisição + resposta como mensagens.
         * Portanto há 2 mensagens por operação concluída.
         */
        double avgProtoMessage =
                (
                    totalProtoRequestBytes
                    + totalProtoResponseBytes
                )
                / (2.0 * successfulRequests);

        double avgTextMessage =
                (
                    totalTextRequestBytes
                    + totalTextResponseBytes
                )
                / (2.0 * successfulRequests);

        double differencePercent =
                avgTextMessage == 0.0
                ? 0.0
                : (
                    (
                        avgTextMessage
                        - avgProtoMessage
                    )
                    / avgTextMessage
                )
                * 100.0;

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
                "Protocolo de transporte: TCP"
        );

        System.out.println(
                "Serializacao: Protocol Buffers"
        );

        System.out.println(
                "Requisicoes concluidas: "
                        + successfulRequests
                        + "/"
                        + N
        );

        System.out.printf(
                "Tempo total: %.3f ms%n",
                totalTimeMs
        );

        System.out.printf(
                "RTT medio: %.3f ms%n",
                averageRtt
        );

        System.out.printf(
                "RTT maximo: %.3f ms%n",
                maxRttMs
        );

        System.out.println();

        System.out.println(
                "TAMANHO MEDIO DAS REQUISICOES"
        );

        System.out.printf(
                "  Protobuf: %.2f bytes%n",
                avgProtoRequest
        );

        System.out.printf(
                "  Texto:    %.2f bytes%n",
                avgTextRequest
        );

        System.out.println();

        System.out.println(
                "TAMANHO MEDIO DAS RESPOSTAS"
        );

        System.out.printf(
                "  Protobuf: %.2f bytes%n",
                avgProtoResponse
        );

        System.out.printf(
                "  Texto:    %.2f bytes%n",
                avgTextResponse
        );

        System.out.println();

        System.out.println(
                "TAMANHO MEDIO GERAL DAS MENSAGENS"
        );

        System.out.printf(
                "  Protobuf: %.2f bytes%n",
                avgProtoMessage
        );

        System.out.printf(
                "  Texto:    %.2f bytes%n",
                avgTextMessage
        );

        if (differencePercent > 0.0) {

            System.out.printf(
                    "  Protobuf foi %.2f%% menor "
                    + "nesta execucao.%n",
                    differencePercent
            );

        } else if (differencePercent < 0.0) {

            System.out.printf(
                    "  Protobuf foi %.2f%% maior "
                    + "nesta execucao.%n",
                    Math.abs(
                            differencePercent
                    )
            );

        } else {

            System.out.println(
                    "  Os tamanhos medios foram iguais."
            );
        }

        System.out.println();

        System.out.println(
                "Observacao: os tamanhos acima representam "
                + "o payload da mensagem."
        );

        System.out.println(
                "O prefixo de tamanho usado por "
                + "writeDelimitedTo/parseDelimitedFrom "
                + "nao foi incluido."
        );
    }

    private static double randomOperand() {

        return Math.round(
                RANDOM.nextDouble()
                * 10000.0
        ) / 100.0;
    }

    private static String randomOperator() {

        String[] operators = {
                "+",
                "-",
                "*",
                "/"
        };

        return operators[
                RANDOM.nextInt(
                        operators.length
                )
        ];
    }
}
