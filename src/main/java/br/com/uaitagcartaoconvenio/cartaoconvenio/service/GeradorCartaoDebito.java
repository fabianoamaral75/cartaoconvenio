/*
 Explicação do Código:
   ==> Geração dos Dígitos:
       * O código gera os primeiros 15 dígitos do cartão de forma aleatória.
       * O último dígito (dígito verificador) é calculado usando o algoritmo de Luhn.

   ==> Algoritmo de Luhn:
       * O algoritmo de Luhn é usado para validar números de cartão de crédito/débito.
       * Ele funciona da seguinte forma:
           - Multiplica cada segundo dígito (da direita para a esquerda) por 2.
           - Se o resultado da multiplicação for maior que 9, soma os dígitos do resultado.
           - Soma todos os dígitos.
           - O dígito verificador é o número que, quando adicionado à soma, faz com que o total seja um múltiplo de 10.

   ==> Formato do Número do Cartão:
       * O número gerado é apenas um exemplo e não está associado a nenhum banco ou instituição financeira real.

   ==> Exemplo de Saída:
       * Número do cartão gerado: 1234567890123452
 */


package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.Random;

public class GeradorCartaoDebito {
	
    // Gera um número de cartão de débito válido
    public static String gerarNumeroCartao() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        // Gera os primeiros 15 dígitos (sem o dígito verificador)
        for (int i = 0; i < 15; i++) {
            sb.append(random.nextInt(10));
        }

        // Calcula o dígito verificador usando o algoritmo de Luhn
        String numeroSemDigito = sb.toString();
        int digitoVerificador = calcularDigitoVerificador(numeroSemDigito);

        // Adiciona o dígito verificador ao número do cartão
        sb.append(digitoVerificador);

        return sb.toString();
    }

    // Implementação do algoritmo de Luhn para calcular o dígito verificador
    public static int calcularDigitoVerificador(String numero) {
        int soma = 0;
        boolean alternar = false;

        // Percorre o número da direita para a esquerda
        for (int i = numero.length() - 1; i >= 0; i--) {
            int digito = Integer.parseInt(numero.substring(i, i + 1));

            if (alternar) {
                digito *= 2;
                if (digito > 9) {
                    digito = (digito % 10) + 1;
                }
            }

            soma += digito;
            alternar = !alternar;
        }

        // Calcula o dígito verificador
        int digitoVerificador = (10 - (soma % 10)) % 10;
        return digitoVerificador;
    }
}
