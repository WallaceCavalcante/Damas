import java.util.*;

public class Tabuleiro {

    private static final String ALFABETO = "0ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String VEZ_JOGADOR = "";

    private String[][] matriz;
    private int tamanho;

    public Tabuleiro(int tamanho) {
        tamanho++;

        this.tamanho = tamanho;

        this.matriz = new String[tamanho][tamanho];

        this.matriz[0][0] = "  ";

        for (int coluna = 1; coluna < this.tamanho; coluna++) {
            this.matriz[0][coluna] = String.valueOf(ALFABETO.charAt(coluna));
        }

        for (int linha = 1; linha < this.tamanho; linha++) {
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

        for (int linha = 0; linha < tamanho; linha++) {
            System.out.print("    ");
            for (int coluna = 0; coluna < tamanho; coluna++) {
                System.out.print("  ");
                System.out.print(this.matriz[linha][coluna]);
            }
            System.out.println("");
        }

        System.out.println("-----------------------------------");
    }

    public String comecarGame() {
        String peca = "";

        Scanner sc = new Scanner(System.in);
        System.out.println("Quem irá começar o game? Insira valores como: P ou B");
        peca = sc.nextLine().toUpperCase();
        while (!peca.equals("B") && !peca.equals("P")) {
            System.out.println("Valor inválido, por favor informe valores como P ou B:");
            peca = sc.nextLine().toUpperCase();
        }
        VEZ_JOGADOR = peca;
        return peca;
    }

    // NAO DEU TEMPO DE MEXER/CONTINUAR
    public String pegaMovimentoPecas(String peca) {

        int condicional = 0;
        validaComePeca();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Qual coordenada da peça que você gostaria de mover?");
        String coordenadaAnterior = scanner.nextLine().toUpperCase();
        while (!coordenadaAnterior.matches("[1-9][A-H]")) {
            System.out.println("Coordenada inválida, por favor digite no padrão correto (LinhaColuna)");
            coordenadaAnterior = scanner.nextLine().toUpperCase();
        }

        while (condicional == 0) {
            int linha = getLinha(coordenadaAnterior);
            int coluna = getColuna(coordenadaAnterior);

            if (!(peca.equals(this.matriz[linha][coluna]))) {
                System.out.println("Por favor informe uma coordenada que seja sua peça, a peça: " + peca);
                coordenadaAnterior = scanner.nextLine().toUpperCase();
            } else {
                condicional = 1;
            }
        }


        System.out.println("Você pode mover a sua peça para as posições printadas abaixo:");
        List<String> possiveisJogadas = validaJogadasDisponiveisDaPeca(getLinha(coordenadaAnterior), getColuna(coordenadaAnterior));
        System.out.println("Para qual coordenada gostaria de mover a peça escolhida?");
        String novaCoordenada = scanner.nextLine().toUpperCase();
        while (!possiveisJogadas.contains(novaCoordenada)) {
            System.out.println("Coordenada inválida, por favor digite no padrão correto (LinhaColuna)");
            novaCoordenada = scanner.nextLine().toUpperCase();
        }
        System.out.println();
        return coordenadaAnterior + "," + novaCoordenada;
    }

    public String setaVezProximoJogador(String peca) {

        peca.toUpperCase();
        if (peca.equals("B")) {
            System.out.println("Agora que a vez do jogador das pecas P:");
            peca = "P";
        } else {
            System.out.println("Agora que a vez do jogador das pecas B:");
            peca = "B";
        }
        return peca;
    }

    // NAO DEU TEMPO DE MEXER/CONTINUAR
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
        String pecaWhile = "";

        int novaLinha = getLinha(novaCoordenada);
        int novaColuna = getColuna(novaCoordenada);
// validar se a linha anterior eh maior ou menor que a novaLinha, se for maior pegar linhaAnteior+1, se for menor linhaAnterior-1 o mesmo para coluna
        while (condicional != 1) {
            if ((novaLinha + novaColuna) % 2 != 0 ||
                    linhaAnterior - novaLinha >= 2 || linhaAnterior - novaLinha <= -2
                    || colunaAnterior - novaColuna >= 2 || colunaAnterior - novaColuna <= -2) {
                System.out.println("Esta coordenada não é valida, por favor informe uma coordenada valida:");
                pecaWhile = scanner.nextLine();
                novaLinha = getLinha(pecaWhile);
                novaColuna = getColuna(pecaWhile);
            } else {
                condicional = 1;
            }
        }
        this.matriz[novaLinha][novaColuna] = peca;
        imprimirTabuleiro();
        VEZ_JOGADOR = setaVezProximoJogador(peca);
        //validaPecaProxima(novaLinha, novaColuna);
    }

    private void validaPecaProxima(int novaLinha, int novaColuna) {
        List<String> a = new ArrayList<>();
        if (novaLinha < 8 && novaColuna > 1 && novaLinha > 1 && novaColuna < 8) {
            if (Objects.equals(this.matriz[novaLinha + 1][novaColuna + 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[novaLinha + 1][novaColuna + 1], "*")) {
                if (Objects.equals(this.matriz[novaLinha - 1][novaColuna - 1], "*")) {
                    a.add(String.valueOf(novaLinha - 1).concat(String.valueOf(novaColuna - 1)));
                    validaCombo(novaLinha - 1, novaColuna - 1, a);
                }

            }
            if (Objects.equals(this.matriz[novaLinha + 1][novaColuna - 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[novaLinha + 1][novaColuna - 1], "*")) {
                if (Objects.equals(this.matriz[novaLinha - 1][novaColuna + 1], "*")) {
                    a.add(String.valueOf(novaLinha - 1).concat(String.valueOf(novaColuna + 1)));
                    validaPecaProxima(novaLinha - 1, novaColuna + 1);
                }

            } else if (Objects.equals(this.matriz[novaLinha - 1][novaColuna - 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[novaLinha - 1][novaColuna - 1], "*")) {
                if (Objects.equals(this.matriz[novaLinha + 1][novaColuna + 1], "*")) {
                    a.add(String.valueOf(novaLinha + 1).concat(String.valueOf(novaColuna + 1)));
                    validaPecaProxima(novaLinha + 1, novaColuna + 1);
                }

            } else if (Objects.equals(this.matriz[novaLinha - 1][novaColuna + 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[novaLinha - 1][novaColuna + 1], "*")) {
                if (Objects.equals(this.matriz[novaLinha + 1][novaColuna - 1], "*")) {
                    a.add(String.valueOf(novaLinha + 1).concat(String.valueOf(novaColuna - 1)));
                    validaPecaProxima(novaLinha + 1, novaColuna - 1);
                }
            }
        }

        a.forEach(System.out::println);

    }

    private List<String> validaJogadasDisponiveis() {
        List<String> possiveisJogadas = new ArrayList<>();

        for (int linha = 1; linha < tamanho; linha++) {
            for (int coluna = 1; coluna < tamanho; coluna++) {
                if (matriz[linha][coluna].equals(VEZ_JOGADOR)) {

                    if (linha < 8 && coluna < 8) {
                        if (Objects.equals(this.matriz[linha + 1][coluna + 1], "*")) {
                            possiveisJogadas.add(String.valueOf(linha + 1).concat(String.valueOf(coluna + 1)));
                        }
                    }
                    if (linha < 8 && coluna > 1) {
                        if (Objects.equals(this.matriz[linha + 1][coluna - 1], "*")) {
                            possiveisJogadas.add(String.valueOf(linha + 1).concat(String.valueOf(coluna - 1)));
                        }
                    }
                    if (linha > 1 && coluna > 1) {
                        if (Objects.equals(this.matriz[linha - 1][coluna - 1], "*")) {
                            possiveisJogadas.add(String.valueOf(linha - 1).concat(String.valueOf(coluna - 1)));
                        }
                    }
                    if (linha > 1 && coluna < 8) {
                        if (Objects.equals(this.matriz[linha - 1][coluna + 1], "*")) {
                            possiveisJogadas.add(String.valueOf(linha - 1).concat(String.valueOf(coluna + 1)));
                        }
                    }
                }
            }
        }
        possiveisJogadas.forEach(a -> System.out.print(a + ", "));
        System.out.println();
        return possiveisJogadas;
    }

    private List<String> validaJogadasDisponiveisDaPeca(int linha, int coluna) {
        List<String> possiveisJogadas = new ArrayList<>();

        if (linha < 8 && coluna < 8) {
            if (Objects.equals(this.matriz[linha + 1][coluna + 1], "*")) {
                possiveisJogadas.add(String.valueOf(linha + 1).concat(String.valueOf(matriz[0][coluna + 1])));
            }
        }
        if (linha < 8 && coluna > 1) {
            if (Objects.equals(this.matriz[linha + 1][coluna - 1], "*")) {
                possiveisJogadas.add(String.valueOf(linha + 1).concat(String.valueOf(matriz[0][coluna - 1])));
            }
        }
        if (linha > 1 && coluna > 1) {
            if (Objects.equals(this.matriz[linha - 1][coluna - 1], "*")) {
                possiveisJogadas.add(String.valueOf(linha - 1).concat(String.valueOf(matriz[0][coluna - 1])));
            }
        }
        if (linha > 1 && coluna < 8) {
            if (Objects.equals(this.matriz[linha - 1][coluna + 1], "*")) {
                possiveisJogadas.add(String.valueOf(linha - 1).concat(String.valueOf(matriz[0][coluna + 1])));
            }
        }
        possiveisJogadas.forEach(a -> System.out.print(a + ", "));
        System.out.println();
        return possiveisJogadas;
    }

    //Para ele não somar o B que já foi comido, ele deve validar que não é a posição da peça que ele passou por cima
    //Deve ser criada um int que soma a quantidade de peças comidas e ele deve ser zerado  no inicio do método
    //para ser possivel pegar a diferença de quantidades que se pode comer e deve ser passado esse int por parametro
    //para o método;
    private void validaComePeca() {
        List<String> listaDePecaQuePodeComer = new ArrayList<>();
        List<String> listaDePosicaoParaComer = new ArrayList<>();
        LinkedList<String> listaDePosicaoParaComerNoLoop = new LinkedList<>();
        for (int linha = 1; linha < tamanho; linha++) {
            for (int coluna = 1; coluna < tamanho; coluna++) {
                if (linha < 7 && coluna < 7) {
                    if (Objects.equals(this.matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna + 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna + 1], "*")) {
                        if (Objects.equals(this.matriz[linha + 2][coluna + 2], "*")) {
                            listaDePecaQuePodeComer.add(String.valueOf(linha).concat(matriz[0][coluna]));
                            listaDePosicaoParaComer.add(String.valueOf(linha + 2).concat(matriz[0][coluna + 2]));
                        }
                    }
                }
                if (linha < 7 && coluna > 2) {
                    if (Objects.equals(this.matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna - 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna - 1], "*")) {
                        if (Objects.equals(this.matriz[linha + 2][coluna - 2], "*")) {
                            listaDePecaQuePodeComer.add(String.valueOf(linha).concat(matriz[0][coluna]));
                            listaDePosicaoParaComer.add(String.valueOf(linha + 2).concat(matriz[0][coluna - 2]));
                        }

                    }
                }
                if (linha > 2 && coluna > 2) {
                    if (Objects.equals(this.matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna - 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna - 1], "*")) {
                        if (Objects.equals(this.matriz[linha - 2][coluna - 2], "*")) {
                            listaDePecaQuePodeComer.add(String.valueOf(linha).concat(matriz[0][coluna]));
                            listaDePosicaoParaComer.add(String.valueOf(linha - 2).concat(matriz[0][coluna - 2]));
                        }

                    }
                }
                if (linha > 2 && coluna < 7) {
                    if (Objects.equals(this.matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna + 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna + 1], "*")) {
                        if (Objects.equals(this.matriz[linha - 2][coluna + 2], "*")) {
                            listaDePecaQuePodeComer.add(String.valueOf(linha).concat(matriz[0][coluna]));
                            listaDePosicaoParaComer.add(String.valueOf(linha - 2).concat(matriz[0][coluna + 2]));
                        }
                    }
                }
            }
        }
        System.out.println("Posição da peça que pode comer: ");
        listaDePecaQuePodeComer.forEach(a -> System.out.print(a + ", "));
        System.out.println();
        validaComePeca(listaDePosicaoParaComer.get(0),listaDePecaQuePodeComer.get(0), listaDePosicaoParaComerNoLoop);

        System.out.println("Posição do local que terá que mover para comer a peça: ");
        listaDePosicaoParaComer.forEach(a -> System.out.print(a + ", "));
        System.out.println();

    }

    private void validaComePeca(String coordenada, String coordenadaAntiga, LinkedList<String> listaDePosicaoParaComer) {
        int linha = getLinha(coordenada);
        int coluna = getColuna(coordenada);

        int linhaAntiga = getLinha(coordenadaAntiga);
        int colunaAntiga = getColuna(coordenadaAntiga);

        int interseccaoLinha = linha + linhaAntiga / 2;
        int interseccaoColuna = coluna + colunaAntiga / 2;

        if (linha < 7 && coluna < 7) {
            if(linha+1 != interseccaoLinha || coluna+1 != interseccaoColuna) {
                if (Objects.equals(this.matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna + 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna + 1], "*")) {
                    if (Objects.equals(this.matriz[linha + 2][coluna + 2], "*")) {
                        listaDePosicaoParaComer.add(String.valueOf(linha + 2).concat(matriz[0][coluna + 2]));
                        validaComePeca(String.valueOf(linha + 2).concat(String.valueOf(coluna + 2)), coordenada, listaDePosicaoParaComer);
                    }
                }
            }
        }
        if (linha < 7 && coluna > 2) {
            if(linha+1 != interseccaoLinha || coluna-1 != interseccaoColuna) {
                if (Objects.equals(this.matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna - 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna - 1], "*")) {
                    if (Objects.equals(this.matriz[linha + 2][coluna - 2], "*")) {
                        listaDePosicaoParaComer.add(String.valueOf(linha + 2).concat(matriz[0][coluna - 2]));
                        validaComePeca(String.valueOf(linha + 2).concat(String.valueOf(coluna - 2)), coordenada, listaDePosicaoParaComer);
                    }
                }
            }
        }
        if (linha > 2 && coluna > 2) {
            if(linha-1 != interseccaoLinha || coluna-1 != interseccaoColuna) {
                if (Objects.equals(this.matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna - 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna - 1], "*")) {
                    if (Objects.equals(this.matriz[linha - 2][coluna - 2], "*")) {
                        listaDePosicaoParaComer.add(String.valueOf(linha - 2).concat(matriz[0][coluna - 2]));
                        validaComePeca(String.valueOf(linha - 2).concat(String.valueOf(coluna - 2)), coordenada, listaDePosicaoParaComer);
                    }
                }
            }
        }
        if (linha > 2 && coluna < 7) {
            if (linha - 1 != interseccaoLinha || coluna + 1 != interseccaoColuna) {
                if (Objects.equals(this.matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna + 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna + 1], "*")) {
                    if (Objects.equals(this.matriz[linha - 2][coluna + 2], "*")) {
                        listaDePosicaoParaComer.add(String.valueOf(linha - 2).concat(matriz[0][coluna + 2]));
                        validaComePeca(String.valueOf(linha - 2).concat(String.valueOf(coluna + 2)), coordenada, listaDePosicaoParaComer);
                    }
                }
            }
        }
        System.out.println("Posição do local que terá que mover para comer a peça: ");
        listaDePosicaoParaComer.forEach(a -> System.out.print(a + ", "));
        System.out.println();
    }

    private void validaCombo(int novaLinha, int novaColuna, List<String> listaDePosicao) {
        if (novaLinha < 8 && novaColuna < 8) {
            if (Objects.equals(this.matriz[novaLinha + 1][novaColuna + 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[novaLinha + 1][novaColuna + 1], "*")) {
                if (Objects.equals(this.matriz[novaLinha + 2][novaColuna + 2], "*")) {
                    listaDePosicao.add(String.valueOf(novaLinha + 2).concat(String.valueOf(novaColuna + 2)));
                    validaCombo(novaLinha + 2, novaColuna + 2, listaDePosicao);
                }
            }
        }
        if (novaLinha < 8 && novaColuna > 2) {
            if (Objects.equals(this.matriz[novaLinha + 1][novaColuna - 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[novaLinha + 1][novaColuna - 1], "*")) {
                if (Objects.equals(this.matriz[novaLinha + 2][novaColuna - 2], "*")) {
                    listaDePosicao.add(String.valueOf(novaLinha + 2).concat(String.valueOf(novaColuna - 2)));
                    validaCombo(novaLinha + 2, novaColuna - 2, listaDePosicao);
                }

            }
        }
        if (novaLinha > 2 && novaColuna > 2) {
            if (Objects.equals(this.matriz[novaLinha - 1][novaColuna - 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[novaLinha - 1][novaColuna - 1], "*")) {
                if (Objects.equals(this.matriz[novaLinha - 2][novaColuna - 2], "*")) {
                    listaDePosicao.add(String.valueOf(novaLinha - 2).concat(String.valueOf(novaColuna - 2)));
                    validaCombo(novaLinha - 2, novaLinha - 2, listaDePosicao);
                }

            }
        }
        if (novaLinha > 2 && novaColuna < 8) {
            if (Objects.equals(this.matriz[novaLinha - 1][novaColuna + 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[novaLinha - 1][novaColuna + 1], "*")) {
                if (Objects.equals(this.matriz[novaLinha - 2][novaColuna + 2], "*")) {
                    listaDePosicao.add(String.valueOf(novaLinha - 2).concat(String.valueOf(novaColuna + 2)));
                    validaCombo(novaLinha - 2, novaColuna + 2, listaDePosicao);
                }

            }
        }
        listaDePosicao.forEach(System.out::println);

    }

    private void validaPodeComer(int linha, int coluna) {

    }

    private int getLinha(String coordenada) {
        return Integer.parseInt(String.valueOf(coordenada.charAt(0)));
    }

    private int getColuna(String coordenada) {
        return Character.getNumericValue(coordenada.charAt(1)) - 9;
    }
}

