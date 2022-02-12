import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String tabuleiro[][] = new String[8][8];
        instanciaMatrizZerada(tabuleiro);
        preenchePeçasBrancas(tabuleiro);
        preenchePeçasPretas(tabuleiro);
        printaMatriz(tabuleiro);
        aplicaMovimento(tabuleiro, moverPeças(tabuleiro));
    }


    public static void printaMatriz(String[][] array){
        for (String[] strings : array) {
            for (String anString : strings) {
                System.out.print(anString + "     ");
            }
            System.out.println("\n");
        }
    }

    public static void instanciaMatrizZerada(String[][] array){
        for (String[] strings : array) {
            Arrays.fill(strings, "*");
        }
    }

    public static void preenchePeçasBrancas(String [][] array){
        for (int i = 0; i < array.length ; i++) {
            for (int j = 0; j < array[i].length ; j++) {
                if (i == 5 || i == 7) {
                    if(j % 2 == 0){
                        array[i][j] = "B";
                    }
                }
                else if (i == 6 && j % 2 == 1){
                    array[i][j] = "B";
                }
            }
        }
    }

    public static void preenchePeçasPretas(String [][] array){
        for (int i = 0; i < array.length ; i++) {
            for (int j = 0; j < array[i].length ; j++) {
                if (i == 0 || i == 2) {
                    if(j % 2 == 0){
                        array[i][j] = "P";
                    }
                }
                else if (i == 1 && j % 2 == 1){
                    array[i][j] = "P";
                }
            }
        }
    }

    public static String[] moverPeças(String[][] array){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Qual peça você gostaria de mover?");
        String peca = scanner.nextLine();
        System.out.println("Para onde gostaria de mover a peça escolhida?");
        String movement = scanner.nextLine();
        System.out.println();
        return new String[]{peca, movement};
    }

    public static void aplicaMovimento(String[][] array, String[] movement){
        //movement[0] == peça
        //movement[1] == para onde a peça vai se mover
        String tipoPeca = "*";
        for (int i = 0; i < array.length ; i++) {
            for (int j = 0; j < array[i].length ; j++) {
                if ((i + "," + j).equals(movement[0])) {
                    tipoPeca = array[i][j];
                    array[i][j] = "*";
                }
            }
        }
        for (int i = 0; i < array.length ; i++) {
            for (int j = 0; j < array[i].length ; j++) {
                if((i + "," + j).equals(movement[1])){
                    array[i][j] = tipoPeca;
                }
            }
        }
        printaMatriz(array);
    }
}
