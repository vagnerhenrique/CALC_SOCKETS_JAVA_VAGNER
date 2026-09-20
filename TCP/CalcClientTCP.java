package TCP;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Locale;
import java.util.Random;


/**
 * Nome: Vagner Henrique - Pós Graduação
 * Matricula: 603486
 * Professor: Paulo Rego 
 * Disciplina: Sistemas Distribuídos / Capítulo 4 (Comunicação entre Processos)
 * 
 * CalcClientTCP
 */

public class CalcClientTCP {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;

    /*
     * Quantidade especificada pela atividade.
     */
    private static final int N = 20;

    private static final Random random = new Random();

    public static void main(String[] args) {

        Locale.setDefault(Locale.US);

        System.out.println("=====================================");
        System.out.println("       CALCULADORA TCP - CLIENTE - V. 1.0");
        System.out.println("=====================================");

        System.out.println(
                "Servidor: "
                + SERVER_ADDRESS
                + ":"
                + SERVER_PORT
        );

        System.out.println(
                "Quantidade de requisições: "
                + N
        );

        System.out.println();

        try (
                Socket socket =
                        new Socket(
                                SERVER_ADDRESS,
                                SERVER_PORT
                        );

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

            System.out.println("Conectado ao servidor.\n");

            double totalRTT = 0;

            double maxRTT = 0;

            long totalStart =
                    System.nanoTime();

            for (int sequence = 0;
                 sequence < N;
                 sequence++) {

                String request =
                        generateRequest(sequence);

                System.out.println(
                        "Requisição " + sequence
                );

                System.out.println(
                        "Enviando: " + request
                );

                /*
                 * Momento imediatamente anterior
                 * ao envio da requisição.
                 */
                long start =
                        System.nanoTime();

                output.println(request);

                /*
                 * Aguarda a resposta do servidor.
                 *
                 * Não existe retransmissão aqui.
                 */
                String response =
                        input.readLine();

                /*
                 * Momento de chegada da resposta.
                 */
                long end =
                        System.nanoTime();

                if (response == null) {

                    System.out.println(
                            "Servidor encerrou a conexão."
                    );

                    break;
                }

                /*
                 * Conversão:
                 *
                 * nanossegundos -> milissegundos
                 */
                double rtt =
                        (end - start)
                        / 1_000_000.0;

                totalRTT += rtt;

                if (rtt > maxRTT) {
                    maxRTT = rtt;
                }

                System.out.println(
                        "Resposta: "
                        + response
                );

                System.out.printf(
                        "RTT: %.3f ms%n",
                        rtt
                );

                System.out.println(
                        "-------------------------------------"
                );
            }

            long totalEnd =
                    System.nanoTime();

            double totalTime =
                    (totalEnd - totalStart)
                    / 1_000_000.0;

            double averageRTT =
                    totalRTT / N;

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
                    "Protocolo: TCP"
            );

            System.out.println(
                    "Requisições: "
                    + N
            );

            System.out.printf(
                    "Tempo total: %.3f ms%n",
                    totalTime
            );

            System.out.printf(
                    "RTT médio: %.3f ms%n",
                    averageRTT
            );

            System.out.printf(
                    "RTT máximo: %.3f ms%n",
                    maxRTT
            );

            System.out.println(
                    "Retransmissões da aplicação: 0"
            );

            System.out.println(
                    "Requisições perdidas: 0"
            );

        } catch (ConnectException e) {

            System.err.println(
                    "Não foi possível conectar ao servidor."
            );

            System.err.println(
                    "Verifique se o CalcServerTCP está executando."
            );

        } catch (IOException e) {

            System.err.println(
                    "Erro de comunicação: "
                    + e.getMessage()
            );
        }
    }


    /**
     * Gera uma requisição aleatória.
     */
    private static String generateRequest(int sequence) {

        /*
         * Valores entre 0 e 100.
         *
         * Arredondados para duas casas decimais.
         */
        double operand1 =
                Math.round(
                        random.nextDouble()
                        * 10000
                ) / 100.0;

        double operand2 =
                Math.round(
                        random.nextDouble()
                        * 10000
                ) / 100.0;

        String[] operators = {
                "+",
                "-",
                "*",
                "/"
        };

        String operator =
                operators[
                        random.nextInt(
                                operators.length
                        )
                ];

        /*
         * Em algumas divisões colocamos zero
         * propositalmente para testar o tratamento
         * de divisão por zero do servidor.
         */
        if (
                operator.equals("/")
                && random.nextInt(10) == 0
        ) {

            operand2 = 0;
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
}