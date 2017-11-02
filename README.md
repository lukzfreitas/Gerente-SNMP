# Gerente-SNMP
Trabalho desenvolvido na linguagem Java para a disciplina de Gerência de Redes do curso de Sistemas de Informação PUCRS.

Esta aplicação tem como objetivo realizar a monitaração de envio e recebimento de taxas de pacotes SNMP. A partir desta aplicação é possivel selecionar o envio e recebimento das taxas de pacotes dos seguintes protocolos: IP, TCP, UDP, SNMP e a taxa de utilização de link.

É possível monitorar diversos dispositivos informando seu ip, além do tempo de periodicidade em segundos para realizar requisições ao agente. Ao selecionar um OID correspondente a taxa de pacote é possível visualizar uma tela que gera um gráfico em tempo real onde é possivel identificar o número de taxas de pacotes recebidos ou enviados.

Para a realizaçao deste trabalho foi utilizada biblioteca SNMPJ4 ver [documentação](http://www.snmp4j.org/).

# Exemplos
["Tela Inicial da aplicação"]
![TELA_INICIAL](https://raw.githubusercontent.com/lukzfreitas/Gerente-SNMP/master/Imagens/telaInicial.PNG)
["Exemplo de gráfico taxa de pacotes de IP enviados"]
![TELA_GRAFICO](https://raw.githubusercontent.com/lukzfreitas/Gerente-SNMP/master/Imagens/telaGrafico.PNG)
