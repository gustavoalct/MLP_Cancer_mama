
package MLP;

import arquivo.Amostra;

public class Main {

    public static void main(String[] args){

        double erroToleravel = 0.00000005;
        int vezesQueAmostraRoda = 1;
        int maxEpocas =  180;


        // Le o arquivo salvando todas as amostras em um ArrayList de List
        Amostra amostras = new Amostra();

        // Trata as amostras ausentes
        amostras.trataAmostra();

        // Separa as amostras de treinamento
        amostras.amostraTreinamento();

        // Normaliza as amostras
        amostras.normaliza();
        
        // Passa as amostras de treino para a Classe Treinamento
        Treinamento treino = new Treinamento(amostras.getAmostrasTreinamento());
        System.out.println("-------------------------------- Pesos apos o Treinamento --------------------------------");
        treino.mostraPesos();

        double erroAtual = 0, erroAnterior;
        int epocas = 0;
        
        // Pega tempo de inicio do treinamento
        long tempoInicial = System.currentTimeMillis();
        
        // Realiza o treinamento
        int qntAmostraTreinamento = (int) ((Amostra.benigno + Amostra.maligno) * Amostra.porcentagemUtilizada); 
        do{
            erroAnterior = erroAtual;
            erroAtual = 0;
            for (int i = 0; i < qntAmostraTreinamento ; i++) {
                erroAtual += treino.propagacao();
                treino.retropropagacao();
            }
            erroAtual /= Amostra.benigno + Amostra.maligno;
            epocas++;
        }while(epocas < maxEpocas && Math.abs(erroAtual - erroAnterior) > erroToleravel);
        
        // Pega tempo depois do treinamento
        long tempoFinal = System.currentTimeMillis();

        System.out.println("-------------------------------- Pesos apos o Treinamento --------------------------------");
        treino.mostraPesos();
        System.out.println("\n\n--------------------------------Informações--------------------------------");
        System.out.println("Quantidade de épocas: " + ((int) epocas / vezesQueAmostraRoda));
        treino.classifica(amostras.getAmostras());
        System.out.println("Tempo de Treinamento:" + (tempoFinal - tempoInicial));
        System.out.println("TEMPO FINAL: "+ tempoFinal );

        
    }
    
}
