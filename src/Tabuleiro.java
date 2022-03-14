import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

public class Tabuleiro {

    // Letras para as colunas
    private static final String ALFABETO = "0ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // Criando o Tabuleiro
    private String[][] matriz;
    private int tamanho;

    public Tabuleiro(int tamanho) {
        tamanho++;

        this.tamanho = tamanho;

        this.matriz = new String[tamanho][tamanho];

        this.matriz[0][0] = "  ";

        for(int coluna = 1; coluna < this.tamanho; coluna++) {
            this.matriz[0][coluna] = String.valueOf(ALFABETO.charAt(coluna));
        }

        for(int linha = 1; linha < this.tamanho; linha++) {
            String numeroDaLinha = "";

            numeroDaLinha += String.valueOf(linha) + " ";

            this.matriz[linha][0] = numeroDaLinha;
        }

        for (int linha = 1; linha < this.tamanho; linha++) {
            for (int coluna = 1; coluna < this.tamanho; coluna++) {
                this.matriz[linha][coluna] = "*";
            }
        }

        if (this.tamanho == 9) {
            int linhasDePecas = 3;

            for (int linha = 1; linha <= linhasDePecas; linha++) {
                int coluna = 0;
                if (linha % 2 == 0)
                    coluna = 2;
                else
                    coluna = 1;

                while (coluna < this.tamanho) {
                    this.matriz[linha][coluna] = "B";
                    coluna = coluna + 2;
                }
            }
            for (int linha = this.tamanho - 1; linha >= (this.tamanho - linhasDePecas); linha--) {
                int coluna = 0;
                if (linha % 2 == 0)
                    coluna = 2;
                else
                    coluna = 1;

                while (coluna < this.tamanho) {
                    this.matriz[linha][coluna] = "P";
                    coluna = coluna + 2;
                }
            }
        }
    }

    public void imprimirTabuleiro() {

        for (int linha = 0 ; linha < tamanho; linha++) {
            for (int coluna = 0; coluna < tamanho ; coluna++) {
                System.out.print(this.matriz[linha][coluna]);
            }

            System.out.println("");
        }

        System.out.println("-----------------------------------");
    }

    public String comecarGame(){
        String peca = "";
        int condicional = 0;

        Scanner sc = new Scanner(System.in);
        System.out.println("Quem irá começar o game? Insira valores como: P ou B");
        peca = sc.nextLine().toUpperCase();

        while(condicional == 0){
            if(peca.equals("B") || peca.equals("P")){
                condicional = 1;
            }else{
                System.out.println("Valor inválido, por favor informe valores como P ou B:");
                peca = sc.nextLine().toUpperCase();
            }
        }
        return peca;
    }

    public void verificaMovimentoPeca(String peca,String coordenada){

        if(peca.equals("B")){
        }else if(peca.equals("P")){
        }
    }

    public String pegaMovimentoPecas(String peca){

        int condicional = 0;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Qual coordenada da peça que você gostaria de mover?");
        String coordenadaAnterior = scanner.nextLine().toUpperCase();

        while(condicional == 0){
            int linha = getLinha(coordenadaAnterior);
            int coluna = getColuna(coordenadaAnterior);

            if(!(peca.equals(this.matriz[linha][coluna]))){
                System.out.println("Por favor informe uma coordenada que seja sua peça, a peça: " + peca);
                coordenadaAnterior = scanner.nextLine().toUpperCase();
            }
            else{
                condicional = 1;
            }
        }
        condicional = 0;

        System.out.println("Para qual coordenada gostaria de mover a peça escolhida?");
        String novaCoordenada = scanner.nextLine().toUpperCase();

        // VOLTAR E CONTINUAR ESSA PARTE AQUI FOI ONDE EU PAREI NA ULTIMA VEZ QUE MEXI, NA PARTE DA NOVACOORDENADA PARA FAZER ELA ANDAR SÓ NO RAIO DA PEÇA DELA

       /* while(condicional == 0){
            int linha = getLinha(novaCoordenada);

            if(peca.equals("B")){
                if(linha > 4){
                    System.out.println("Por favor informe uma coordenada válida para sua peça, na coordenada: " + coordenadaAnterior );
                    novaCoordenada = scanner.nextLine();
                }
            }
            else{
                condicional = 1;
            }
        }*/
        System.out.println();
        return coordenadaAnterior + "," + novaCoordenada;
    }


    public void moverPeca(String coordenadas) {
        Scanner scanner = new Scanner(System.in);
        String[] vetorCoordenadas = coordenadas.split(",");

        String coordenadaAnterior = vetorCoordenadas[0].toUpperCase();
        String novaCoordenada = vetorCoordenadas[1].toUpperCase();

        int linhaAnterior = getLinha(coordenadaAnterior);
        int colunaAnterior = getColuna(coordenadaAnterior);

        String peca = this.matriz[linhaAnterior][colunaAnterior];

        this.matriz[linhaAnterior][colunaAnterior] = "*";

        int condicional = 0;
        String pecaWhile="";

        int novaLinha = getLinha(novaCoordenada);
        int novaColuna = getColuna(novaCoordenada);

        while(condicional!=1) {
            if ((novaLinha + novaColuna) % 2 != 0) {
                System.out.println("Esta coordenada não é valida, por favor informe uma coordenada valida:");
                pecaWhile = scanner.nextLine();
                novaLinha = getLinha(pecaWhile);
                novaColuna = getColuna(pecaWhile);
            }
         /*   else if (Objects.equals(this.matriz[novaLinha][novaColuna], peca)) {
                System.out.println("Esta coordenada ja está preenchida, por favor informe uma coordenada valida:");
                pecaWhile = scanner.nextLine();
                novaLinha = getLinha(pecaWhile);
                novaColuna = getColuna(pecaWhile);
            } */else {
                condicional = 1;
            }
        }
        this.matriz[novaLinha][novaColuna] = peca;
        imprimirTabuleiro();
    }

    private int getLinha(String coordenada) {
        return Integer.valueOf(coordenada.substring(0, 1));
    }

    private int getColuna(String coordenada) {
        return ALFABETO.indexOf(coordenada.substring(1, 2));
    }
}