/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arquivo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author 13171000515
 */
public class Amostra {
    private FileReader arq;
    private BufferedReader lerArq;
    private ArrayList<List<String>> amostras;
    private ArrayList<List<String>> amostrasTreinamento;
    public static int maligno = 445 , benigno = 516;
    public static double porcentagemUtilizada = 0.30;
    
    public Amostra(){
        try{
            // Pega caminho absoluto do sistema até esse arquivo
            String filePath = new File("").getAbsolutePath();
            this.arq = new FileReader( filePath + "/mammographic_masses.data");
            this.lerArq = new BufferedReader(this.arq);
            this.amostras = new ArrayList<>();   
            List<String> atributos;
            
            String linha = "";
            
            while(linha != null){
                
        
                linha = this.lerArq.readLine();
                // separa os atributos e adiciona false para saber que a amostra não foi lida
                if(linha != null){
                    linha += ",false";
                    atributos = Arrays.asList(linha.split(","));
                    this.amostras.add(atributos);
                }
                
            }
            
            
        }catch(IOException e){
            System.out.println("ERRO: " + e);
        }
        
    }
    

    /*  Coluna
     * Maior 0:6 1:97 2:4 3:5 4:4 5:1 
     * Menor 0:0 1:18 2:1 3:1 4:1 5:0
     */
    public void normaliza() {
        int maior0 = 6, menor0 = 0;
        int maior1 = 96, menor1 = 18;
        int maior2 = 4, menor2 = 1;
        int maior3 = 5, menor3 = 1;
        int maior4 = 4, menor4 = 1;

        for (List<String> amostra : amostras) {
            amostra.set(0, Double
                    .toString((((Double.parseDouble(amostra.get(0)) - menor0) * (1 - 0)) / (maior0 - menor0)) + 0));
            amostra.set(1, Double
                    .toString((((Double.parseDouble(amostra.get(1)) - menor1) * (1 - 0)) / (maior1 - menor1)) + 0));
            amostra.set(2, Double
                    .toString((((Double.parseDouble(amostra.get(2)) - menor2) * (1 - 0)) / (maior2 - menor2)) + 0));
            amostra.set(3, Double
                    .toString((((Double.parseDouble(amostra.get(3)) - menor3) * (1 - 0)) / (maior3 - menor3)) + 0));
            amostra.set(4, Double
                    .toString((((Double.parseDouble(amostra.get(4)) - menor4) * (1 - 0)) / (maior4 - menor4)) + 0));
        }
    }
    
    // O tratamento das amostras é feito a partir de uma media aritimética simples
    public void trataAmostra(){
        // Faz média das idade
        int idadeMedia = 0, countIdadeMedia = 0;
        for(List<String> amostra: amostras){
            if(!amostra.get(1).equals("?")){
                idadeMedia += Integer.parseInt(amostra.get(1));
                countIdadeMedia++;
            }
                
        }
        idadeMedia /= countIdadeMedia;
        
        // Substitui onde as idades estão ausentes
        for(List<String> amostra: amostras){
            if(amostra.get(1).equals("?"))
                amostra.set(1, Integer.toString(idadeMedia));
        }
        
        // Relação Tipo Tulmor e Formato
        /*           Ben     Mal
        Arredondado: 186  :  38
        Oval:        176  :  35
        Lobular:     50   :  45
        Nominal:     85   :  315
        */
        // Verifica a quantidade qual formato é mais provavel de ser benigno e maligno
        for (List<String> amostra: amostras) {
            if (amostra.get(5).equals("0") && amostra.get(2).equals("?"))
                amostra.set(2, "1");

            if (amostra.get(5).equals("1") && amostra.get(2).equals("?"))
                amostra.set(2, "4");
        }

        // Existem apenas 2 valores ausentes no BI-RADS
        for (List<String> amostra: amostras) {
            if (amostra.get(0).equals("?"))
                amostra.set(0, "2");
        }


        // Maioria das pessoas que tem tumor benigno tem o formato da massa Circunscrita
        // Circunscrita = 316; Microlobulada = 9; Obscura = 43; Mal definida = 89; Espiculada/Nominal = 22
        // Maioria das pessoas que tem tumor maligno tem o formato da massa Mal denifida
        // Circunscrita = 41; Microlobulada = 15; Obscura = 73; Mal definida = 192; Espiculada/Nominal = 114
        for (List<String> amostra: amostras) {
            if (amostra.get(3).equals("?") && amostra.get(5).equals("1"))
                amostra.set(3, "4");
            else if (amostra.get(3).equals("?") && amostra.get(5).equals("0"))
                amostra.set(3, "1");
        }

        // Densidade não interfere no resultado do exame segundo o artigo do proprio site
        // Por isso calculamos a média da densidade
        // Densidade: Alta = 16 ;Isotomica = 59 ; Baixa = 798 ; Contendo gordura 12
        for (List<String> amostra : amostras)
            if (amostra.get(4).equals("?"))
                amostra.set(4, "3"); 
                
    }
    
    
    // Separa as amostras para treinamento
    // Amostras que irão para o treinamento é colocado o Boolean True na frente da estrutura List
    public void amostraTreinamento(){
        int countBenigno = 0, countMaligno = 0;
        this.amostrasTreinamento = new ArrayList<>(); 
        
        for(List<String> amostra: amostras){
            if(amostra.get(5).equals("0") && countBenigno < benigno*porcentagemUtilizada){
                countBenigno++;
                amostra.set(6, "true");
                this.amostrasTreinamento.add(amostra);
            }else if(countMaligno < maligno*porcentagemUtilizada){
                countMaligno++;
                amostra.set(6, "true");
                this.amostrasTreinamento.add(amostra);
            }
            
        }
    }
    
    // Mostra amostras
    public void mostra(){
        for (List<String> amostra : amostras) {
            System.out.println(amostra);
        }
    }
    
    // Mostra amostras de treinamento
    public void mostraTreinamento(){
        for (List<String> amostra : amostrasTreinamento) {
            System.out.println(amostra);
        }
    }

    public ArrayList<List<String>> getAmostrasTreinamento() {
        return amostrasTreinamento;
    }
    
    public ArrayList<List<String>> getAmostras() {
        return amostras;
    }
}
