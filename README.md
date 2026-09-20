# CALC_SOCKETS_JAVA_VAGNER
Implementar o mesmo serviço cliente-servidor duas vezes, uma vez sobre UDP, outra sobre TCP, e comparar empiricamente o comportamento dos dois protocolos diante de perda de mensagens. O trabalho conecta diretamente com o que foi visto no capítulo: a API de sockets, o modelo de requisição-e-resposta e a discussão sobre quando usar cada protocolo.

# Instruções

1) Cada pasta representa uma aplicação, PROTO, TCP,UDP

2) Em cada pasta tem um Servidor e um Cliente, só executar normalmente:
- TCP(CalcClienteTPC e CalcServidorTCP)
- UDP(CalcClienteUDP e CalcServidorUDP)
- PROTO(CalcClientePROTO e CalcServidorPROTO)


# Resultado das execuções


=====================================
       CALCULADORA TCP - CLIENTE - V. 1.0
=====================================
Servidor: localhost:5000
Quantidade de requisições: 20

Conectado ao servidor.

Requisição 0
Enviando: CALC:0:70.68:+:99.01
Resposta: RESULT:0:169.69
RTT: 7.794 ms
-------------------------------------
Requisição 1
Enviando: CALC:1:10.70:+:41.08
Resposta: RESULT:1:51.78
RTT: 2.906 ms
-------------------------------------
Requisição 2
Enviando: CALC:2:61.99:-:97.15
Resposta: RESULT:2:-35.160000000000004
RTT: 0.799 ms
-------------------------------------
Requisição 3
Enviando: CALC:3:80.33:*:21.31
Resposta: RESULT:3:1711.8322999999998
RTT: 0.492 ms
-------------------------------------
Requisição 4
Enviando: CALC:4:17.33:*:90.24
Resposta: RESULT:4:1563.8591999999996
RTT: 0.825 ms
-------------------------------------
Requisição 5
Enviando: CALC:5:24.84:+:74.41
Resposta: RESULT:5:99.25
RTT: 0.907 ms
-------------------------------------
Requisição 6
Enviando: CALC:6:31.63:/:21.14
Resposta: RESULT:6:1.4962157048249762
RTT: 0.981 ms
-------------------------------------
Requisição 7
Enviando: CALC:7:43.78:*:93.19
Resposta: RESULT:7:4079.8582
RTT: 0.845 ms
-------------------------------------
Requisição 8
Enviando: CALC:8:94.06:-:12.22
Resposta: RESULT:8:81.84
RTT: 3.275 ms
-------------------------------------
Requisição 9
Enviando: CALC:9:7.04:-:24.13
Resposta: RESULT:9:-17.09
RTT: 1.009 ms
-------------------------------------
Requisição 10
Enviando: CALC:10:29.51:-:43.66
Resposta: RESULT:10:-14.149999999999995
RTT: 0.681 ms
-------------------------------------
Requisição 11
Enviando: CALC:11:3.80:/:94.45
Resposta: RESULT:11:0.040232927474854414
RTT: 0.678 ms
-------------------------------------
Requisição 12
Enviando: CALC:12:44.10:-:37.28
Resposta: RESULT:12:6.82
RTT: 0.663 ms
-------------------------------------
Requisição 13
Enviando: CALC:13:27.62:+:77.84
Resposta: RESULT:13:105.46000000000001
RTT: 0.714 ms
-------------------------------------
Requisição 14
Enviando: CALC:14:15.16:+:30.37
Resposta: RESULT:14:45.53
RTT: 1.221 ms
-------------------------------------
Requisição 15
Enviando: CALC:15:2.63:-:2.37
Resposta: RESULT:15:0.2599999999999998
RTT: 1.325 ms
-------------------------------------
Requisição 16
Enviando: CALC:16:16.40:*:24.40
Resposta: RESULT:16:400.15999999999997
RTT: 1.197 ms
-------------------------------------
Requisição 17
Enviando: CALC:17:84.18:/:51.09
Resposta: RESULT:17:1.647680563711098
RTT: 1.268 ms
-------------------------------------
Requisição 18
Enviando: CALC:18:78.07:*:23.30
Resposta: RESULT:18:1819.031
RTT: 0.539 ms
-------------------------------------
Requisição 19
Enviando: CALC:19:85.07:+:1.07
Resposta: RESULT:19:86.13999999999999
RTT: 0.559 ms
-------------------------------------

=====================================
        RESULTADO DO EXPERIMENTO
=====================================
Protocolo: TCP
Requisições: 20
Tempo total: 85.399 ms
RTT médio: 1.434 ms
RTT máximo: 7.794 ms
Retransmissões da aplicação: 0
Requisições perdidas: 0

=====================================
       CALCULADORA UDP - CLIENTE
=====================================
Servidor: localhost:5001
Requisições: 20
Timeout: 500 ms
Máximo de tentativas: 5

Requisição 0: CALC:0:51.29:+:8.87
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:0:60.16
  RTT da tentativa bem-sucedida: 22.762 ms
-------------------------------------
Requisição 1: CALC:1:4.17:-:0.25
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:1:3.92
  RTT da tentativa bem-sucedida: 1.576 ms
-------------------------------------
Requisição 2: CALC:2:67.69:*:94.67
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:2:6408.2123
  RTT da tentativa bem-sucedida: 5.342 ms
-------------------------------------
Requisição 3: CALC:3:74.18:/:36.39
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:3:2.0384721077219017
  RTT da tentativa bem-sucedida: 3.771 ms
-------------------------------------
Requisição 4: CALC:4:1.69:*:20.73
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:4:35.033699999999996
  RTT da tentativa bem-sucedida: 1.877 ms
-------------------------------------
Requisição 5: CALC:5:11.67:/:0.00
  Envio inicial (tentativa 1/5)
  Resposta: ERROR:5:divisao por zero
  RTT da tentativa bem-sucedida: 7.200 ms
-------------------------------------
Requisição 6: CALC:6:48.09:-:54.93
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:6:-6.839999999999996
  RTT da tentativa bem-sucedida: 0.828 ms
-------------------------------------
Requisição 7: CALC:7:77.58:-:59.02
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:7:18.559999999999995
  RTT da tentativa bem-sucedida: 1.138 ms
-------------------------------------
Requisição 8: CALC:8:63.13:-:30.16
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:8:32.97
  RTT da tentativa bem-sucedida: 5.127 ms
-------------------------------------
Requisição 9: CALC:9:32.80:/:56.87
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:9:0.5767540003516792
  RTT da tentativa bem-sucedida: 1.645 ms
-------------------------------------
Requisição 10: CALC:10:23.98:-:22.76
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:10:1.2199999999999989
  RTT da tentativa bem-sucedida: 0.963 ms
-------------------------------------
Requisição 11: CALC:11:34.76:+:5.88
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:11:40.64
  RTT da tentativa bem-sucedida: 0.994 ms
-------------------------------------
Requisição 12: CALC:12:72.56:-:33.46
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:12:39.1
  RTT da tentativa bem-sucedida: 3.372 ms
-------------------------------------
Requisição 13: CALC:13:54.64:*:24.94
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:13:1362.7216
  RTT da tentativa bem-sucedida: 1.261 ms
-------------------------------------
Requisição 14: CALC:14:64.25:+:67.85
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:14:132.1
  RTT da tentativa bem-sucedida: 1.010 ms
-------------------------------------
Requisição 15: CALC:15:79.43:+:36.40
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:15:115.83000000000001
  RTT da tentativa bem-sucedida: 1.056 ms
-------------------------------------
Requisição 16: CALC:16:39.71:+:79.58
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:16:119.28999999999999
  RTT da tentativa bem-sucedida: 0.955 ms
-------------------------------------
Requisição 17: CALC:17:44.44:+:57.66
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:17:102.1
  RTT da tentativa bem-sucedida: 0.668 ms
-------------------------------------
Requisição 18: CALC:18:86.22:*:36.42
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:18:3140.1324
  RTT da tentativa bem-sucedida: 1.200 ms
-------------------------------------
Requisição 19: CALC:19:85.86:-:41.25
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:19:44.61
  RTT da tentativa bem-sucedida: 3.231 ms
-------------------------------------

=====================================
        RESULTADO DO EXPERIMENTO
=====================================
Protocolo: UDP
Requisições previstas: 20
Requisições respondidas: 20
Requisições perdidas definitivamente: 0
Retransmissões: 0
Tempo total: 192.055 ms
RTT médio (requisições respondidas): 3.299 ms
RTT máximo (requisições respondidas): 22.762 ms

=====================================
       CALCULADORA UDP - CLIENTE
=====================================
Servidor: localhost:5001
Requisições: 20
Timeout: 500 ms
Máximo de tentativas: 5

Requisição 0: CALC:0:38.08:+:68.89
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:0:106.97
  RTT da tentativa bem-sucedida: 19.072 ms
-------------------------------------
Requisição 1: CALC:1:13.45:/:11.00
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:1:1.2227272727272727
  RTT da tentativa bem-sucedida: 1.666 ms
-------------------------------------
Requisição 2: CALC:2:74.11:*:77.85
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:2:5769.4635
  RTT da tentativa bem-sucedida: 1.453 ms
-------------------------------------
Requisição 3: CALC:3:75.17:+:30.96
  Envio inicial (tentativa 1/5)
  TIMEOUT após 500 ms.
  Retransmissão 1 (tentativa 2/5)
  Resposta: RESULT:3:106.13
  RTT da tentativa bem-sucedida: 0.975 ms
-------------------------------------
Requisição 4: CALC:4:75.69:/:56.39
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:4:1.3422592658272743
  RTT da tentativa bem-sucedida: 0.887 ms
-------------------------------------
Requisição 5: CALC:5:98.13:/:0.00
  Envio inicial (tentativa 1/5)
  TIMEOUT após 500 ms.
  Retransmissão 1 (tentativa 2/5)
  Resposta: ERROR:5:divisao por zero
  RTT da tentativa bem-sucedida: 2.117 ms
-------------------------------------
Requisição 6: CALC:6:96.07:/:55.10
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:6:1.7435571687840288
  RTT da tentativa bem-sucedida: 0.743 ms
-------------------------------------
Requisição 7: CALC:7:96.95:+:94.08
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:7:191.03
  RTT da tentativa bem-sucedida: 0.603 ms
-------------------------------------
Requisição 8: CALC:8:82.90:-:11.13
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:8:71.77000000000001
  RTT da tentativa bem-sucedida: 0.982 ms
-------------------------------------
Requisição 9: CALC:9:45.59:+:14.40
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:9:59.99
  RTT da tentativa bem-sucedida: 0.906 ms
-------------------------------------
Requisição 10: CALC:10:68.01:+:71.22
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:10:139.23000000000002
  RTT da tentativa bem-sucedida: 1.309 ms
-------------------------------------
Requisição 11: CALC:11:12.10:-:56.34
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:11:-44.24
  RTT da tentativa bem-sucedida: 0.841 ms
-------------------------------------
Requisição 12: CALC:12:47.81:/:22.87
  Envio inicial (tentativa 1/5)
  TIMEOUT após 500 ms.
  Retransmissão 1 (tentativa 2/5)
  Resposta: RESULT:12:2.090511587232182
  RTT da tentativa bem-sucedida: 0.927 ms
-------------------------------------
Requisição 13: CALC:13:44.96:*:49.13
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:13:2208.8848000000003
  RTT da tentativa bem-sucedida: 0.609 ms
-------------------------------------
Requisição 14: CALC:14:40.28:*:57.44
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:14:2313.6832
  RTT da tentativa bem-sucedida: 0.505 ms
-------------------------------------
Requisição 15: CALC:15:79.85:-:63.36
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:15:16.489999999999995
  RTT da tentativa bem-sucedida: 0.622 ms
-------------------------------------
Requisição 16: CALC:16:80.93:*:54.11
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:16:4379.1223
  RTT da tentativa bem-sucedida: 1.119 ms
-------------------------------------
Requisição 17: CALC:17:58.71:+:20.93
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:17:79.64
  RTT da tentativa bem-sucedida: 0.924 ms
-------------------------------------
Requisição 18: CALC:18:38.84:+:86.03
  Envio inicial (tentativa 1/5)
  TIMEOUT após 500 ms.
  Retransmissão 1 (tentativa 2/5)
  Resposta: RESULT:18:124.87
  RTT da tentativa bem-sucedida: 0.678 ms
-------------------------------------
Requisição 19: CALC:19:52.84:-:5.34
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:19:47.5
  RTT da tentativa bem-sucedida: 0.876 ms
-------------------------------------

=====================================
        RESULTADO DO EXPERIMENTO
=====================================
Protocolo: UDP
Requisições previstas: 20
Requisições respondidas: 20
Requisições perdidas definitivamente: 0
Retransmissões: 4
Tempo total: 2141.422 ms
RTT médio (requisições respondidas): 1.891 ms
RTT máximo (requisições respondidas): 19.072 ms
PS C:\Users\henri\Desktop\CALC_SOCKETS_JAVA_VAGNER\UDP> 

30%

PS C:\Users\henri\Desktop\CALC_SOCKETS_JAVA_VAGNER\UDP> java CalcClientUDP                
=====================================
       CALCULADORA UDP - CLIENTE
=====================================
Servidor: localhost:5001
Requisições: 20
Timeout: 500 ms
Máximo de tentativas: 5

Requisição 0: CALC:0:46.81:*:47.98
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:0:2245.9438
  RTT da tentativa bem-sucedida: 17.474 ms
-------------------------------------
Requisição 1: CALC:1:27.35:/:0.00
  Envio inicial (tentativa 1/5)
  TIMEOUT após 500 ms.
  Retransmissão 1 (tentativa 2/5)
  TIMEOUT após 500 ms.
  Retransmissão 2 (tentativa 3/5)
  Resposta: ERROR:1:divisao por zero
  RTT da tentativa bem-sucedida: 9.163 ms
-------------------------------------
Requisição 2: CALC:2:91.79:+:33.66
  Envio inicial (tentativa 1/5)
  TIMEOUT após 500 ms.
  Retransmissão 1 (tentativa 2/5)
  TIMEOUT após 500 ms.
  Retransmissão 2 (tentativa 3/5)
  Resposta: RESULT:2:125.45
  RTT da tentativa bem-sucedida: 1.117 ms
-------------------------------------
Requisição 3: CALC:3:21.93:+:77.19
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:3:99.12
  RTT da tentativa bem-sucedida: 0.484 ms
-------------------------------------
Requisição 4: CALC:4:27.25:*:83.06
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:4:2263.385
  RTT da tentativa bem-sucedida: 0.511 ms
-------------------------------------
Requisição 5: CALC:5:60.96:/:76.19
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:5:0.8001050006562541
  RTT da tentativa bem-sucedida: 0.569 ms
-------------------------------------
Requisição 6: CALC:6:68.88:/:41.79
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:6:1.6482412060301508
  RTT da tentativa bem-sucedida: 0.630 ms
-------------------------------------
Requisição 7: CALC:7:54.99:*:94.48
  Envio inicial (tentativa 1/5)
  TIMEOUT após 500 ms.
  Retransmissão 1 (tentativa 2/5)
  Resposta: RESULT:7:5195.4552
  RTT da tentativa bem-sucedida: 2.479 ms
-------------------------------------
Requisição 8: CALC:8:39.87:+:81.46
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:8:121.32999999999998
  RTT da tentativa bem-sucedida: 2.386 ms
-------------------------------------
Requisição 9: CALC:9:68.45:*:6.04
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:9:413.43800000000005
  RTT da tentativa bem-sucedida: 2.628 ms
-------------------------------------
Requisição 10: CALC:10:76.50:+:94.66
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:10:171.16
  RTT da tentativa bem-sucedida: 2.792 ms
-------------------------------------
Requisição 11: CALC:11:69.75:-:43.77
  Envio inicial (tentativa 1/5)
  TIMEOUT após 500 ms.
  Retransmissão 1 (tentativa 2/5)
  Resposta: RESULT:11:25.979999999999997
  RTT da tentativa bem-sucedida: 2.268 ms
-------------------------------------
Requisição 12: CALC:12:42.34:/:79.83
  Envio inicial (tentativa 1/5)
  TIMEOUT após 500 ms.
  Retransmissão 1 (tentativa 2/5)
  TIMEOUT após 500 ms.
  Retransmissão 2 (tentativa 3/5)
  TIMEOUT após 500 ms.
  Retransmissão 3 (tentativa 4/5)
  Resposta: RESULT:12:0.530377051233872
  RTT da tentativa bem-sucedida: 0.790 ms
-------------------------------------
Requisição 13: CALC:13:48.20:+:98.95
  Envio inicial (tentativa 1/5)
  TIMEOUT após 500 ms.
  Retransmissão 1 (tentativa 2/5)
  Resposta: RESULT:13:147.15
  RTT da tentativa bem-sucedida: 1.447 ms
-------------------------------------
Requisição 14: CALC:14:65.81:*:47.47
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:14:3124.0007
  RTT da tentativa bem-sucedida: 1.804 ms
-------------------------------------
Requisição 15: CALC:15:54.30:+:29.94
  Envio inicial (tentativa 1/5)
  TIMEOUT após 500 ms.
  Retransmissão 1 (tentativa 2/5)
  Resposta: RESULT:15:84.24
  RTT da tentativa bem-sucedida: 2.763 ms
-------------------------------------
Requisição 16: CALC:16:83.90:*:72.61
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:16:6091.979
  RTT da tentativa bem-sucedida: 3.034 ms
-------------------------------------
Requisição 17: CALC:17:63.35:*:53.34
  Envio inicial (tentativa 1/5)
  Resposta: RESULT:17:3379.0890000000004
  RTT da tentativa bem-sucedida: 1.896 ms
-------------------------------------
Requisição 18: CALC:18:21.17:/:37.03
  Envio inicial (tentativa 1/5)
  TIMEOUT após 500 ms.
  Retransmissão 1 (tentativa 2/5)
  Resposta: RESULT:18:0.5716986227383203
  RTT da tentativa bem-sucedida: 2.248 ms
-------------------------------------
Requisição 19: CALC:19:69.29:*:81.42
  Envio inicial (tentativa 1/5)
  TIMEOUT após 500 ms.
  Retransmissão 1 (tentativa 2/5)
  TIMEOUT após 500 ms.
  Retransmissão 2 (tentativa 3/5)
  Resposta: RESULT:19:5641.591800000001
  RTT da tentativa bem-sucedida: 3.350 ms
-------------------------------------

=====================================
        RESULTADO DO EXPERIMENTO
=====================================
Protocolo: UDP
Requisições previstas: 20
Requisições respondidas: 20
Requisições perdidas definitivamente: 0
Retransmissões: 14
Tempo total: 7227.606 ms
RTT médio (requisições respondidas): 2.992 ms
RTT máximo (requisições respondidas): 17.474 ms
PS C:\Users\henri\Desktop\CALC_SOCKETS_JAVA_VAGNER\UDP> 


PROTO


PS C:\Users\henri\Desktop\CALC_SOCKETS_JAVA_VAGNER> cd .\PROTO\                                          
PS C:\Users\henri\Desktop\CALC_SOCKETS_JAVA_VAGNER\PROTO> java -cp ".;protobuf-java.jar" CalcClientProto
=====================================
    CALCULADORA PROTOBUF - CLIENTE
=====================================
Servidor: localhost:5000
Quantidade de requisicoes: 20

Conectado ao servidor.

Requisicao 0
  Operacao: CALC:0:24.44:*:39.94
  Resposta: RESULT:0:976.1336
  RTT: 38.936 ms
  Tamanho requisicao:
    Protobuf: 21 bytes
    Texto:    20 bytes
  Tamanho resposta:
    Protobuf: 11 bytes
    Texto:    17 bytes
-------------------------------------
Requisicao 1
  Operacao: CALC:1:4.11:-:17.74
  Resposta: RESULT:1:-13.629999999999999
  RTT: 1.743 ms
  Tamanho requisicao:
    Protobuf: 23 bytes
    Texto:    19 bytes
  Tamanho resposta:
    Protobuf: 13 bytes
    Texto:    28 bytes
-------------------------------------
Requisicao 2
  Operacao: CALC:2:35.43:*:63.55
  Resposta: RESULT:2:2251.5764999999997
  RTT: 1.394 ms
  Tamanho requisicao:
    Protobuf: 23 bytes
    Texto:    20 bytes
  Tamanho resposta:
    Protobuf: 13 bytes
    Texto:    27 bytes
-------------------------------------
Requisicao 3
  Operacao: CALC:3:58.87:*:97.95
  Resposta: RESULT:3:5766.3165
  RTT: 2.082 ms
  Tamanho requisicao:
    Protobuf: 23 bytes
    Texto:    20 bytes
  Tamanho resposta:
    Protobuf: 13 bytes
    Texto:    18 bytes
-------------------------------------
Requisicao 4
  Operacao: CALC:4:65.63:*:58.26
  Resposta: RESULT:4:3823.6037999999994
  RTT: 1.831 ms
  Tamanho requisicao:
    Protobuf: 23 bytes
    Texto:    20 bytes
  Tamanho resposta:
    Protobuf: 13 bytes
    Texto:    27 bytes
-------------------------------------
Requisicao 5
  Operacao: CALC:5:50.61:*:20.68
  Resposta: RESULT:5:1046.6148
  RTT: 1.229 ms
  Tamanho requisicao:
    Protobuf: 23 bytes
    Texto:    20 bytes
  Tamanho resposta:
    Protobuf: 13 bytes
    Texto:    18 bytes
-------------------------------------
Requisicao 6
  Operacao: CALC:6:83.00:*:82.61
  Resposta: RESULT:6:6856.63
  RTT: 2.531 ms
  Tamanho requisicao:
    Protobuf: 23 bytes
    Texto:    20 bytes
  Tamanho resposta:
    Protobuf: 13 bytes
    Texto:    16 bytes
-------------------------------------
Requisicao 7
  Operacao: CALC:7:95.63:/:58.47
  Resposta: RESULT:7:1.6355395929536514
  RTT: 1.738 ms
  Tamanho requisicao:
    Protobuf: 23 bytes
    Texto:    20 bytes
  Tamanho resposta:
    Protobuf: 13 bytes
    Texto:    27 bytes
-------------------------------------
Requisicao 8
  Operacao: CALC:8:20.28:-:89.81
  Resposta: RESULT:8:-69.53
  RTT: 1.841 ms
  Tamanho requisicao:
    Protobuf: 23 bytes
    Texto:    20 bytes
  Tamanho resposta:
    Protobuf: 13 bytes
    Texto:    15 bytes
-------------------------------------
Requisicao 9
  Operacao: CALC:9:40.21:*:79.64
  Resposta: RESULT:9:3202.3244
  RTT: 1.529 ms
  Tamanho requisicao:
    Protobuf: 23 bytes
    Texto:    20 bytes
  Tamanho resposta:
    Protobuf: 13 bytes
    Texto:    18 bytes
-------------------------------------
Requisicao 10
  Operacao: CALC:10:16.96:/:27.68
  Resposta: RESULT:10:0.6127167630057804
  RTT: 2.386 ms
  Tamanho requisicao:
    Protobuf: 23 bytes
    Texto:    21 bytes
  Tamanho resposta:
    Protobuf: 13 bytes
    Texto:    28 bytes
-------------------------------------
Requisicao 11
  Operacao: CALC:11:3.70:*:7.30
  Resposta: RESULT:11:27.01
  RTT: 1.551 ms
  Tamanho requisicao:
    Protobuf: 23 bytes
    Texto:    19 bytes
  Tamanho resposta:
    Protobuf: 13 bytes
    Texto:    15 bytes
-------------------------------------
Requisicao 12
  Operacao: CALC:12:0.72:-:76.46
  Resposta: RESULT:12:-75.74
  RTT: 1.454 ms
  Tamanho requisicao:
    Protobuf: 23 bytes
    Texto:    20 bytes
  Tamanho resposta:
    Protobuf: 13 bytes
    Texto:    16 bytes
-------------------------------------
Requisicao 13
  Operacao: CALC:13:97.07:/:23.13
  Resposta: RESULT:13:4.196714223951578
  RTT: 1.542 ms
  Tamanho requisicao:
    Protobuf: 23 bytes
    Texto:    21 bytes
  Tamanho resposta:
    Protobuf: 13 bytes
    Texto:    27 bytes
-------------------------------------
Requisicao 14
  Operacao: CALC:14:95.72:*:0.17
  Resposta: RESULT:14:16.2724
  RTT: 1.593 ms
  Tamanho requisicao:
    Protobuf: 23 bytes
    Texto:    20 bytes
  Tamanho resposta:
    Protobuf: 13 bytes
    Texto:    17 bytes
-------------------------------------
Requisicao 15
  Operacao: CALC:15:71.98:+:30.04
  Resposta: RESULT:15:102.02000000000001
  RTT: 1.402 ms
  Tamanho requisicao:
    Protobuf: 23 bytes
    Texto:    21 bytes
  Tamanho resposta:
    Protobuf: 13 bytes
    Texto:    28 bytes
-------------------------------------
Requisicao 16
  Operacao: CALC:16:29.93:/:18.23
  Resposta: RESULT:16:1.641799232035107
  RTT: 4.726 ms
  Tamanho requisicao:
    Protobuf: 23 bytes
    Texto:    21 bytes
  Tamanho resposta:
    Protobuf: 13 bytes
    Texto:    27 bytes
-------------------------------------
Requisicao 17
  Operacao: CALC:17:46.18:-:95.16
  Resposta: RESULT:17:-48.98
  RTT: 1.431 ms
  Tamanho requisicao:
    Protobuf: 23 bytes
    Texto:    21 bytes
  Tamanho resposta:
    Protobuf: 13 bytes
    Texto:    16 bytes
-------------------------------------
Requisicao 18
  Operacao: CALC:18:80.44:*:7.51
  Resposta: RESULT:18:604.1043999999999
  RTT: 1.591 ms
  Tamanho requisicao:
    Protobuf: 23 bytes
    Texto:    20 bytes
  Tamanho resposta:
    Protobuf: 13 bytes
    Texto:    27 bytes
-------------------------------------
Requisicao 19
  Operacao: CALC:19:42.45:-:87.90
  Resposta: RESULT:19:-45.45
  RTT: 1.571 ms
  Tamanho requisicao:
    Protobuf: 23 bytes
    Texto:    21 bytes
  Tamanho resposta:
    Protobuf: 13 bytes
    Texto:    16 bytes
-------------------------------------

=====================================
        RESULTADO DO EXPERIMENTO
=====================================
Protocolo de transporte: TCP
Serializacao: Protocol Buffers
Requisicoes concluidas: 20/20
Tempo total: 280.013 ms
RTT medio: 3.705 ms
RTT maximo: 38.936 ms

TAMANHO MEDIO DAS REQUISICOES
  Protobuf: 22.90 bytes
  Texto:    20.20 bytes

TAMANHO MEDIO DAS RESPOSTAS
  Protobuf: 12.90 bytes
  Texto:    21.40 bytes

TAMANHO MEDIO GERAL DAS MENSAGENS
  Protobuf: 17.90 bytes
  Texto:    20.80 bytes
  Protobuf foi 13.94% menor nesta execucao.