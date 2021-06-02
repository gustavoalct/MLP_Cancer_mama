package MLP;

import java.util.ArrayList;
import java.util.List;

import arquivo.Amostra;

class Treinamento {

    // Todas as amostras para treinamento
    private ArrayList<List<String>> amostras;

    // Posição atual da amostra 
    private int indiceAmostra;

    // Tamanho das camadas ocultas
    private static int tamCamada1 = 3;
    private static int tamCamada2 = 2;

    // Saida de cada  neuronio de cada camada oculta e camada de saida
    private double [] saidaCamada1, saidaCamada2;
    private double saidaObtida;

    // BIAS de cada neuronio de cada camada oculta
    private double[] biasCamada1, biasCamada2;
    private double biasSaida;
    
    // Peso BIAS de cada neuronio de cada camada oculta
    private double[] pesoBiasCamada1, pesoBiasCamada2;
    private double pesoBiasSaida;


    // Gradiente de cada neuronio de cada camada oculta
    private double[] gradienteCamada1, gradienteCamada2;
    private double gradienteSaida;

    // Pesos entre as camadas camadas 
    private double[][] pesoEntrada_x_Camada1, pesoCamada1_x_Camada2;
    private double[] pesoCamada2_x_saida;

    // Valor em porcentagem da taxa de aprendizado
    private double taxaAprendizado = 0.10;

   // Quantidade de Amostras utilizadas no treinamento
   int qntAmostrasTreinamento = (int) ((Amostra.benigno + Amostra.maligno) * Amostra.porcentagemUtilizada);

    public Treinamento(ArrayList<List<String>> amostras) {

        // Instancia todos os objetos
        pesoEntrada_x_Camada1 = new double[5][tamCamada1];
        pesoCamada1_x_Camada2 = new double[5][tamCamada2];
        pesoCamada2_x_saida = new double[tamCamada2];
        saidaCamada1 = new double[tamCamada1];
        saidaCamada2 = new double[tamCamada2];
        gradienteCamada1 = new double[tamCamada1];
        gradienteCamada2 = new double[tamCamada2];
        this.amostras = new ArrayList<>();
        
        // Recebe as amostras para treinamento
        this.amostras = amostras;

        // Posição inicial da Lista de amostras
        this.indiceAmostra = 0;

        // Coloca valores aleatorios no peso entre a Camada de Entrada x Camada Oculta 1
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < tamCamada1; j++){
                pesoEntrada_x_Camada1[i][j] = (Math.random() * 5);
            }
        }

        // Coloca valores aleatorios no peso entre a Camada Oculta 1 x Camada Oculta 2
        for(int i = 0; i < tamCamada1; i++){
            for(int j = 0; j < tamCamada2; j++){
                pesoCamada1_x_Camada2[i][j] = (Math.random() * 5);
            }
        }

        // Coloca valores aleatorios no peso entre a Camada Oculta 2 x Camada Saida
        for(int i = 0; i < tamCamada2; i++){
            pesoCamada2_x_saida[i]= (Math.random() * 5);
        }


        // Coloca valores aleatorios no peso bias da camada 1 e seta o bias como -1
        pesoBiasCamada1 = new double[tamCamada1];
        biasCamada1 = new double[tamCamada1];
        for (int i = 0; i < tamCamada1; i++) {
            pesoBiasCamada1[i] = (Math.random() * 5);
            biasCamada1[i] = -1;
        }

        // Coloca valores aleatorios no peso bias da camada 2 e seta o bias como -1
        pesoBiasCamada2 = new double[tamCamada2];
        biasCamada2 = new double[tamCamada2];
        for (int i = 0; i < tamCamada2; i++) {
            pesoBiasCamada2[i] = (Math.random() * 5);
            biasCamada2[i] = -1;
        }

        // Coloca valor aleatorio no peso do bias da saida e seta o bias como -1
        pesoBiasSaida = (Math.random() * 5);
        biasSaida = -1;
    }


    public void mostra() {

        for (List<String> amostra : amostras) {
            System.out.println(amostra);
        }
    }

    public void mostraPesos(){
        System.out.println();
        // Printa peso camada 1
        System.out.println("Peso da Camada E x 1:");
        for (int i = 0; i < 5; i++) {
            System.out.println("Neuronio: " + i);
            for (int j = 0; j < tamCamada1; j++) {
                System.out.println("\tPara neuronio:" + j + " " +pesoEntrada_x_Camada1[i][j] + "\t");
                System.out.println("\t\tPeso BIAS:" + pesoBiasCamada1[j] + "\n");
            }
            System.out.println();
        }
        System.out.println();
        // Printa peso camada 2
        System.out.println("Peso da Camada 1 x 2:");
        for (int i = 0; i < tamCamada1; i++) {
            System.out.println("Neuronio: " + i);
            for (int j = 0; j < tamCamada2; j++) {
                System.out.println("\tPara neuronio:" + j + " " +pesoCamada1_x_Camada2[i][j] + "\t");
                System.out.println("\t\tPeso BIAS:" + pesoBiasCamada2[j] + "\n");
            }
            System.out.println();
        }
        System.out.println();
        // Printa peso camada 3
        System.out.println("Peso da Camada 2 x S:");
        for (int i = 0; i < tamCamada2; i++) {
            System.out.println("\tPara neuronio:" + i + " " + pesoCamada2_x_saida[i] + "\t");
            System.out.println("\t\tPeso BIAS:" + pesoBiasSaida + "\n");
        }
        System.out.println();
    }


    public double propagacao() {
        // Peso x Entrada da amostra pela camada 1
        for (int i = 0; i < tamCamada1; i++) {
            double somatorio = 0;
            for (int j = 0; j < 5; j++) {
                // Multiplicação do valor da amostra
                somatorio += Double.parseDouble(amostras.get(this.indiceAmostra).get(j)) * pesoEntrada_x_Camada1[j][i];
            }
            saidaCamada1[i] = 1 / (1 + Math.exp(-1 * (somatorio - pesoBiasCamada1[i])));
        }

        // Peso x Entrada da camada 1 * camada 2
        for (int i = 0; i < tamCamada2; i++) {
            double somatorio = 0;
            for (int j = 0; j < tamCamada1; j++) {
                // Multiplicação do valor da amostra
                somatorio += saidaCamada1[j] * pesoCamada1_x_Camada2[j][i];
            }
            saidaCamada2[i] = 1 / (1 + Math.exp(-1 * (somatorio - pesoBiasCamada2[i])));
        }

        // Peso x Entrada da camada 2 * camada Saida
        double somatorio = 0;
        for (int i = 0; i < tamCamada2; i++) {
            // Multiplicação do valor da amostra * BIAS
            somatorio += saidaCamada2[i] * pesoCamada2_x_saida[i];
        }

        this.saidaObtida = 1 / (1 + Math.exp(-1 * (somatorio - pesoBiasSaida)));
        
        // Calcula o erro
        double erro = Math.pow((Integer.parseInt(amostras.get(this.indiceAmostra).get(5)) - (somatorio - pesoBiasSaida)),2);

        // retorna o erro
        return erro;
    }

    public void retropropagacao() {

        double[][] deltaPesoEntrada_x_Camada1 , deltaPesoCamada1_x_Camada2;
        double[] deltaPesoCamada2_x_saida;

        deltaPesoEntrada_x_Camada1 = new double[5][tamCamada1];
        deltaPesoCamada1_x_Camada2 = new double[tamCamada1][tamCamada2];
        deltaPesoCamada2_x_saida = new double[tamCamada2];


        // Gradiente da camada de saida
        gradienteSaida = (double) saidaObtida * (1 - saidaObtida)
                * (Integer.parseInt(amostras.get(this.indiceAmostra).get(5)) - saidaObtida);

        // Altera o peso entre a Camada 2 x Camada de Saida e o peso do BIAS
        for(int i = 0; i < tamCamada2; i++){
            deltaPesoCamada2_x_saida[i] = taxaAprendizado * saidaCamada2[i] * gradienteSaida;
        }
        // Corrige o BIAS da Saida
        pesoBiasSaida += taxaAprendizado * biasSaida * gradienteSaida;

        // Altera o peso entre a Camada 1 x Camada 2 e o BIAS de cada neuronio da camada 2
        for(int i = 0; i < tamCamada2 ; i++){

            gradienteCamada2[i] = (double) saidaCamada2[i] * (1 - saidaCamada2[i]) * (gradienteSaida *  pesoCamada2_x_saida[i]);

            // Corrige o valor dos pesos do neuronio I em relação aos neuronios anterior
            for(int j = 0; j < tamCamada1; j++){
                deltaPesoCamada1_x_Camada2[j][i] = taxaAprendizado * saidaCamada2[i] * gradienteCamada2[i];
            }
            // Corrige o BIAS da Camada 2
            pesoBiasCamada2[i] += taxaAprendizado * biasCamada2[i] * gradienteCamada2[i];
        }

        // Camada de 3 Neuronios
        // Altera o peso entre a Camada Entrada x Camada 1 e o BIAS de cada neuronio da camada 1
        for(int i = 0; i < tamCamada1; i++){
            double somatorio = 0;
            for(int j = 0; j < tamCamada2; j++){
                somatorio += gradienteCamada2[j] *  pesoCamada1_x_Camada2[i][j];
            }
            gradienteCamada1[i] = (double) saidaCamada1[i] * (1 - saidaCamada1[i]) * somatorio;

            // Corrige o valor dos pesos do neuronio I em relação aos neuronios anterior
            for(int j = 0; j < 5; j++){
                deltaPesoEntrada_x_Camada1[j][i] = taxaAprendizado * saidaCamada1[i] * gradienteCamada1[i];
            }

            // Corrige o BIAS da Camada 1
            pesoBiasCamada1[i] += taxaAprendizado * biasCamada1[i] * gradienteCamada1[i];
        }


        // Altera os Pesos da Camada de Entrada x Camada 1
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < tamCamada1; j++) {
                pesoEntrada_x_Camada1[i][j] += deltaPesoEntrada_x_Camada1[i][j];
            }
        }

        // Altera os Pesos da Camada 1 x Camada 2
        for (int i = 0; i < tamCamada1; i++) {
            for (int j = 0; j < tamCamada2; j++) {
                pesoCamada1_x_Camada2[i][j] += deltaPesoCamada1_x_Camada2[i][j];
            }
        }

        // Altera os Pesos da Camada de Entrada x Camada 1
        for (int i = 0; i < tamCamada2; i++) {
            pesoCamada2_x_saida[i] += deltaPesoCamada2_x_saida[i];
        }

        this.indiceAmostra++;
        if(indiceAmostra > qntAmostrasTreinamento){
            this.indiceAmostra = 0;
        }
    }
    
    
    public void classifica(ArrayList<List<String>> amostrasParaAvaliar){

        int acertos = 0;
        int total = 0;

        for (List<String> amostra : amostrasParaAvaliar) {
            if(Boolean.parseBoolean(amostra.get(6)) == false){
                // Peso x Entrada da amostra pela camada 1
                for (int i = 0; i < tamCamada1; i++) {
                    double somatorio = 0;
                    for (int j = 0; j < 5; j++) {
                        // Multiplicação do valor da amostra
                        somatorio += Double.parseDouble(amostra.get(j)) * pesoEntrada_x_Camada1[j][i];
                    }
                    saidaCamada1[i] = 1 / (1 + Math.exp(-1 * (somatorio - pesoBiasCamada1[i])));
                }

                // Peso x Entrada da camada 1 * camada 2
                for (int i = 0; i < tamCamada2; i++) {
                    double somatorio = 0;
                    for (int j = 0; j < tamCamada1; j++) {
                        // Multiplicação do valor da amostra
                        somatorio += saidaCamada1[j] * pesoCamada1_x_Camada2[j][i];
                    }
                    saidaCamada2[i] = 1 / (1 + Math.exp(-1 * (somatorio - pesoBiasCamada2[i])));
                }

                // Peso x Entrada da camada 2 * camada Saida
                double somatorio = 0;
                for (int i = 0; i < tamCamada2; i++) {
                    // Multiplicação do valor da amostra * BIAS
                    somatorio += saidaCamada2[i] * pesoCamada2_x_saida[i];
                }

                this.saidaObtida = 1 / (1 + Math.exp(-1 * (somatorio - pesoBiasSaida)));
 
                if(saidaObtida >= 0.7 && Integer.parseInt(amostra.get(5)) == 1)
                    acertos++;
                else if(saidaObtida <= 0.3 && Integer.parseInt(amostra.get(5)) == 0)
                    acertos++;

                total++;
            }
        }

        System.out.println("Quantidade de Acertos: " + acertos);
        System.out.println("Total de Amostras Avaliadas: " + total);
        System.out.println("Taxa de Acerto: " + ((double) acertos/total));
    }
}