import java.util.*;

public class Tabuleiro {

    private static final String ALFABETO = "0ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String VEZ_JOGADOR = "";

    public String[][] matriz;
    private int tamanho;
    private LinkedHashSet<String> listaDasPecasObrigadoComer;
    private LinkedList<String> listaPecasComidas;
    private LinkedList<String> listaValidaComePeca;
    private LinkedHashSet<String> listaDasDamasObrigadoComer;
    private LinkedList<String> listaDeValidaDamaComePeca;
    private LinkedList<String> listaPecasComidasPelaDama;

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

            numeroDaLinha += linha + " ";

            this.matriz[linha][0] = numeroDaLinha;
        }

        for (int linha = 1; linha < this.tamanho; linha++) {
            for (int coluna = 1; coluna < this.tamanho; coluna++) {
                this.matriz[linha][coluna] = "*";
            }
        }
        matrizTeste();
    }

    public void matrizDefault() {
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


    public void matrizTeste() {

//        matriz[5][8] = "P";
//        matriz[3][1] = "P";
//        matriz[4][7] = "B";
//        matriz[2][5] = "B";
//        matriz[2][3] = "B";
//        matriz[4][3] = "B";
//        matriz[4][5] = "B";
//        matriz[2][7] = "B";
//        matriz[1][3] = "B";

        matriz[7][3] = "𝓟";
        matriz[6][4] = "B";
        matriz[4][4] = "B";
        matriz[4][6] = "B";
        matriz[2][6] = "B";
        matriz[2][4] = "B";
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

    public String pegaMovimentoPecas(String peca) {

        int condicional = 0;
        listaDasPecasObrigadoComer = new LinkedHashSet<>();
        listaPecasComidas = new LinkedList<>();
        listaValidaComePeca = validaComePeca();
        listaDasDamasObrigadoComer = new LinkedHashSet<>();
        listaPecasComidasPelaDama = new LinkedList<>();
        listaDeValidaDamaComePeca = validaComePecaDama();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Qual coordenada da peça que você gostaria de mover?");

        String coordenadaAnterior = scanner.nextLine().toUpperCase();

        if (listaDasPecasObrigadoComer.isEmpty() && listaDasDamasObrigadoComer.isEmpty()) {
            while (!coordenadaAnterior.matches("[1-9][a-zA-H]")) {
                System.out.println("Coordenada inválida, por favor digite no padrão correto (LinhaColuna)");
                coordenadaAnterior = scanner.nextLine().toUpperCase();
            }

        } else {
            while (!listaDasPecasObrigadoComer.contains(coordenadaAnterior) && !coordenadaAnterior.equals("") &&
                    !listaDasDamasObrigadoComer.contains(coordenadaAnterior)) {
                System.out.println("Coordenada inválida, você é obrigado a comer a peça, você pode escolher as seguintes peças: ");
                listaDasPecasObrigadoComer.forEach(a -> System.out.print(a + ", "));
                System.out.print("Damas: ");
                listaDasDamasObrigadoComer.forEach(a -> System.out.print(a + ", "));
                System.out.println();
                coordenadaAnterior = scanner.nextLine().toUpperCase();
            }
            if (listaDasPecasObrigadoComer.size() > 1) {
                listaValidaComePeca.clear();
                listaPecasComidas.clear();
                validaComePeca(coordenadaAnterior, listaValidaComePeca);
            }
        }
        String atualDama = "";
        while (condicional == 0) {
            int linha = getLinha(coordenadaAnterior);
            int coluna = getColuna(coordenadaAnterior);
            if (peca.equals("P")) {
                atualDama = "𝓟";
            } else if (peca.equals("B")) {
                atualDama = "𝓑";
            }

            if (!(peca.equals(matriz[linha][coluna])) && !atualDama.equals(matriz[linha][coluna])) {
                System.out.println("Por favor informe uma coordenada que seja sua peça, a peça: " + peca);
                coordenadaAnterior = scanner.nextLine().toUpperCase();
            } else {
                condicional = 1;
            }
        }
        String novaCoordenada = "";
        if (listaDasPecasObrigadoComer.isEmpty() && listaDasDamasObrigadoComer.isEmpty()) {
            if (!matriz[getLinha(coordenadaAnterior)][getColuna(coordenadaAnterior)].equals(atualDama)) {
                System.out.println("Você pode mover a sua peça para as posições printadas abaixo:");
                List<String> possiveisJogadas = validaJogadasDisponiveisDaPeca(getLinha(coordenadaAnterior), getColuna(coordenadaAnterior));
                System.out.println("Para qual coordenada gostaria de mover a peça escolhida?");
                novaCoordenada = scanner.nextLine().toUpperCase();
                while (!possiveisJogadas.contains(novaCoordenada)) {
                    System.out.println("Coordenada inválida, por favor digite no padrão correto (LinhaColuna)");
                    novaCoordenada = scanner.nextLine().toUpperCase();
                }
            } else {
                System.out.println("Você pode mover a sua dama para as posições printadas abaixo:");
                LinkedHashSet<String> possiveisJogadas = validaJogadasDisponiveisDaDama(getLinha(coordenadaAnterior), getColuna(coordenadaAnterior));
                System.out.println("Para qual coordenada gostaria de mover a peça escolhida?");
                novaCoordenada = scanner.nextLine().toUpperCase();
                while (!possiveisJogadas.contains(novaCoordenada)) {
                    System.out.println("Coordenada inválida, por favor digite no padrão correto (LinhaColuna)");
                    novaCoordenada = scanner.nextLine().toUpperCase();
                }
            }
        } else {
            if (!matriz[getLinha(coordenadaAnterior)][getColuna(coordenadaAnterior)].equals(atualDama)) {
                System.out.println("Você pode mover a sua peça para a posição abaixo: ");
                System.out.println(listaValidaComePeca.get(0));
                System.out.println("Para qual coordenada gostaria de mover a peça escolhida?");
                novaCoordenada = scanner.nextLine().toUpperCase();
                while (!listaValidaComePeca.get(0).equals(novaCoordenada)) {
                    System.out.println("Coordenada inválida, por favor digite a seguinte coordenada: ");
                    System.out.println(listaValidaComePeca.get(0));
                    novaCoordenada = scanner.nextLine().toUpperCase();
                }
                int linhaListaComida = getLinha(listaPecasComidas.get(0));
                int colunaListaComida = getColuna(listaPecasComidas.get(0));
                matriz[linhaListaComida][colunaListaComida] = "*";

            } else {
                System.out.println("Você pode mover a sua peça para a posição abaixo: ");
                System.out.println(listaDeValidaDamaComePeca.get(0));
                System.out.println("Para qual coordenada gostaria de mover a peça escolhida?");
                novaCoordenada = scanner.nextLine().toUpperCase();
                while (!listaDeValidaDamaComePeca.get(0).equals(novaCoordenada)) {
                    System.out.println("Coordenada inválida, por favor digite a seguinte coordenada: ");
                    System.out.println(listaDeValidaDamaComePeca.get(0));
                    novaCoordenada = scanner.nextLine().toUpperCase();
                }
                int linhaListaComida = getLinha(listaPecasComidasPelaDama.get(0));
                int colunaListaComida = getColuna(listaPecasComidasPelaDama.get(0));
                listaPecasComidasPelaDama.remove(0);
                matriz[linhaListaComida][colunaListaComida] = "*";
            }
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

    public void moverPeca(String coordenadas) {
        Scanner scanner = new Scanner(System.in);
        String[] vetorCoordenadas = coordenadas.split(",");

        String coordenadaAnterior = vetorCoordenadas[0].toUpperCase();
        String novaCoordenada = vetorCoordenadas[1].toUpperCase();

        int linhaAnterior = getLinha(coordenadaAnterior);
        int colunaAnterior = getColuna(coordenadaAnterior);

        String peca = this.matriz[linhaAnterior][colunaAnterior];

        int condicional = 0;
        String pecaWhile = "";

        int novaLinha = getLinha(novaCoordenada);
        int novaColuna = getColuna(novaCoordenada);
        if (!matriz[linhaAnterior][colunaAnterior].equals("𝓟")) {
            if (listaDasPecasObrigadoComer.isEmpty()) {
                while (condicional != 1) {
                    if (linhaAnterior - novaLinha >= 2 || linhaAnterior - novaLinha <= -2 || colunaAnterior - novaColuna >= 2 || colunaAnterior - novaColuna <= -2) {
                        System.out.println("Esta coordenada não é valida, por favor informe uma coordenada valida:");
                        pecaWhile = scanner.nextLine();
                        while (!pecaWhile.matches("[1-9][a-zA-H]")) {
                            System.out.println("Esta coordenada não é valida, por favor, digite no formato correto: LinhaColuna: ");
                            pecaWhile = scanner.nextLine();
                        }
                        novaLinha = getLinha(pecaWhile);
                        novaColuna = getColuna(pecaWhile);
                    } else {
                        condicional = 1;
                    }
                }
            } else {
                while (condicional != 1) {
                    if (linhaAnterior - novaLinha >= 3 || linhaAnterior - novaLinha <= -3
                            || colunaAnterior - novaColuna >= 3 || colunaAnterior - novaColuna <= -3) {
                        System.out.println("Esta coordenada não é valida, por favor informe uma coordenada valida:");
                        pecaWhile = scanner.nextLine();
                        novaLinha = getLinha(pecaWhile);
                        novaColuna = getColuna(pecaWhile);
                    } else {
                        condicional = 1;
                    }
                }
            }
        }
        matriz[linhaAnterior][colunaAnterior] = "*";
        matriz[novaLinha][novaColuna] = peca;

        if (listaDasPecasObrigadoComer.size() > 1 || listaDasDamasObrigadoComer.size() > 1) {
            if (listaValidaComePeca.size() <= (listaDasPecasObrigadoComer.size() + 1) && listaDeValidaDamaComePeca.size() <= (listaDasDamasObrigadoComer.size() + 1)) {
                if (VEZ_JOGADOR.equals("P") && novaLinha == 1) {
                    matriz[novaLinha][novaColuna] = "𝓟";
                } else if (VEZ_JOGADOR.equals("B") && novaLinha == 8) {
                    matriz[novaLinha][novaColuna] = "𝓑";
                }
                VEZ_JOGADOR = setaVezProximoJogador(peca);
            }
        } else {
            if (listaValidaComePeca.size() < 1 && listaDeValidaDamaComePeca.size() < 1) {
                if (VEZ_JOGADOR.equals("P") && novaLinha == 1) {
                    matriz[novaLinha][novaColuna] = "𝓟";
                } else if (VEZ_JOGADOR.equals("B") && novaLinha == 8) {
                    matriz[novaLinha][novaColuna] = "𝓑";
                }
                VEZ_JOGADOR = setaVezProximoJogador(peca);
            }
        }
        imprimirTabuleiro();
    }

    private List<String> validaJogadasDisponiveisDaPeca(int linha, int coluna) {
        List<String> possiveisJogadas = new ArrayList<>();

        if (VEZ_JOGADOR.equals("P")) {
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
        } else {
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
        }

        possiveisJogadas.forEach(a -> System.out.print(a + ", "));
        System.out.println();
        return possiveisJogadas;
    }

    private LinkedList<String> validaComePeca() {

        LinkedList<String> listaDePosicaoParaComer = new LinkedList<>();

        for (int linha = 1; linha < tamanho; linha++) {
            for (int coluna = 1; coluna < tamanho; coluna++) {
                if (linha < 7 && coluna < 7) {
                    if (Objects.equals(this.matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna + 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna + 1], "*")) {
                        if (Objects.equals(this.matriz[linha + 2][coluna + 2], "*")) {
                            listaPecasComidas.add(String.valueOf(linha + 1).concat(matriz[0][coluna + 1]));
                            listaDasPecasObrigadoComer.add(String.valueOf(linha).concat(matriz[0][coluna]));
                            listaDePosicaoParaComer.add(String.valueOf(linha + 2).concat(matriz[0][coluna + 2]));
                        }
                    }
                }
                if (linha < 7 && coluna > 2) {
                    if (Objects.equals(this.matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna - 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna - 1], "*")) {
                        if (Objects.equals(this.matriz[linha + 2][coluna - 2], "*")) {
                            listaPecasComidas.add(String.valueOf(linha + 1).concat(matriz[0][coluna - 1]));
                            listaDasPecasObrigadoComer.add(String.valueOf(linha).concat(matriz[0][coluna]));
                            listaDePosicaoParaComer.add(String.valueOf(linha + 2).concat(matriz[0][coluna - 2]));
                        }

                    }
                }
                if (linha > 2 && coluna > 2) {
                    if (Objects.equals(this.matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna - 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna - 1], "*")) {
                        if (Objects.equals(this.matriz[linha - 2][coluna - 2], "*")) {
                            listaPecasComidas.add(String.valueOf(linha - 1).concat(matriz[0][coluna - 1]));
                            listaDasPecasObrigadoComer.add(String.valueOf(linha).concat(matriz[0][coluna]));
                            listaDePosicaoParaComer.add(String.valueOf(linha - 2).concat(matriz[0][coluna - 2]));
                        }

                    }
                }
                if (linha > 2 && coluna < 7) {
                    if (Objects.equals(this.matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna + 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna + 1], "*")) {
                        if (Objects.equals(this.matriz[linha - 2][coluna + 2], "*")) {
                            listaPecasComidas.add(String.valueOf(linha - 1).concat(matriz[0][coluna + 1]));
                            listaDasPecasObrigadoComer.add(String.valueOf(linha).concat(matriz[0][coluna]));
                            listaDePosicaoParaComer.add(String.valueOf(linha - 2).concat(matriz[0][coluna + 2]));
                        }
                    }
                }
            }
        }
        if (!listaDePosicaoParaComer.isEmpty()) {
            LinkedList<String> listaAux = new LinkedList<>(listaDePosicaoParaComer);
            LinkedList<String> listaDePosicaoParaComerAux = new LinkedList<>();
            LinkedList<String> listaPecasComidasAux = new LinkedList<>(listaPecasComidas);
            LinkedList<String> listaPecasComidasAuxSP = new LinkedList<>();
            int tamanhoLista = 0;

            for (int i = 0; i < listaAux.size(); i++) {
                listaDePosicaoParaComer.clear();
                listaDePosicaoParaComer.add(listaAux.get(i));
                listaPecasComidasAuxSP.clear();
                listaPecasComidas.clear();
                listaPecasComidas.add(listaPecasComidasAux.get(i));

                validaComePeca(listaAux.get(i), listaDePosicaoParaComer);
                if (listaDePosicaoParaComer.size() > tamanhoLista) {
                    tamanhoLista = listaDePosicaoParaComer.size();
                    listaDePosicaoParaComerAux = listaDePosicaoParaComer;
                    listaPecasComidasAuxSP = listaPecasComidas;
                }
            }
            listaPecasComidas = listaPecasComidasAuxSP;
            listaDePosicaoParaComer = listaDePosicaoParaComerAux;
        }

        System.out.println("Posição do local que terá que mover para comer a peça: ");
        listaDePosicaoParaComer.forEach(a -> System.out.print(a + ", "));
        System.out.println();
        return listaDePosicaoParaComer;
    }

    private void validaComePeca(String coordenada, LinkedList<String> listaDePosicaoParaComer) {
        int linha = getLinha(coordenada);
        int coluna = getColuna(coordenada);

        if (linha < 7 && coluna < 7) {

            if (!Objects.equals(this.matriz[linha + 1][coluna + 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna + 1], "*")) {
                if (Objects.equals(this.matriz[linha + 2][coluna + 2], "*")) {
                    if (!listaPecasComidas.contains(String.valueOf(linha + 1).concat(matriz[0][coluna + 1]))) {
                        listaPecasComidas.add(String.valueOf(linha + 1).concat(matriz[0][coluna + 1]));

                        if (!listaDePosicaoParaComer.get(listaDePosicaoParaComer.size() - 1).equals(coordenada)) {
                            listaDePosicaoParaComer.add(coordenada);
                        }

                        listaDePosicaoParaComer.add(String.valueOf(linha + 2).concat(matriz[0][coluna + 2]));
                        validaComePeca(String.valueOf(linha + 2).concat(matriz[0][coluna + 2]), listaDePosicaoParaComer);
                    }

                }
            }
        }
        if (linha < 7 && coluna > 2) {
            if (!Objects.equals(this.matriz[linha + 1][coluna - 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna - 1], "*")) {
                if (Objects.equals(this.matriz[linha + 2][coluna - 2], "*")) {
                    if (!listaPecasComidas.contains(String.valueOf(linha + 1).concat(matriz[0][coluna - 1]))) {
                        listaPecasComidas.add(String.valueOf(linha + 1).concat(matriz[0][coluna - 1]));

                        if (!listaDePosicaoParaComer.get(listaDePosicaoParaComer.size() - 1).equals(coordenada)) {
                            listaDePosicaoParaComer.add(coordenada);
                        }

                        listaDePosicaoParaComer.add(String.valueOf(linha + 2).concat(matriz[0][coluna - 2]));
                        validaComePeca(String.valueOf(linha + 2).concat(matriz[0][coluna - 2]), listaDePosicaoParaComer);
                    }

                }
            }
        }
        if (linha > 2 && coluna > 2) {
            if (!Objects.equals(this.matriz[linha - 1][coluna - 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna - 1], "*")) {
                if (Objects.equals(this.matriz[linha - 2][coluna - 2], "*")) {
                    if (!listaPecasComidas.contains(String.valueOf(linha - 1).concat(matriz[0][coluna - 1]))) {
                        listaPecasComidas.add(String.valueOf(linha - 1).concat(matriz[0][coluna - 1]));

                        if (!listaDePosicaoParaComer.get(listaDePosicaoParaComer.size() - 1).equals(coordenada)) {
                            listaDePosicaoParaComer.add(coordenada);
                        }

                        listaDePosicaoParaComer.add(String.valueOf(linha - 2).concat(matriz[0][coluna - 2]));
                        validaComePeca(String.valueOf(linha - 2).concat(matriz[0][coluna - 2]), listaDePosicaoParaComer);
                    }

                }
            }
        }
        if (linha > 2 && coluna < 7) {
            if (!Objects.equals(this.matriz[linha - 1][coluna + 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna + 1], "*")) {
                if (Objects.equals(this.matriz[linha - 2][coluna + 2], "*")) {
                    if (!listaPecasComidas.contains(String.valueOf(linha - 1).concat(matriz[0][coluna + 1]))) {
                        listaPecasComidas.add(String.valueOf(linha - 1).concat(matriz[0][coluna + 1]));

                        if (!listaDePosicaoParaComer.get(listaDePosicaoParaComer.size() - 1).equals(coordenada)) {
                            listaDePosicaoParaComer.add(coordenada);
                        }

                        listaDePosicaoParaComer.add(String.valueOf(linha - 2).concat(matriz[0][coluna + 2]));
                        validaComePeca(String.valueOf(linha - 2).concat(matriz[0][coluna + 2]), listaDePosicaoParaComer);
                    }

                }
            }
        }


//        if (listaDePosicaoParaComer.size() != 0) {
//            if (!listaDePosicaoParaComer.get(listaDePosicaoParaComer.size() - 1).equals("|") && !listaDePosicaoParaComer.get(listaDePosicaoParaComer.size() - 1).equals(listaDePosicaoParaComer.get(0))) {
//                listaDePosicaoParaComer.add("|");
//                listaDePosicaoParaComer.add(listaDePosicaoParaComer.get(0));
//
//            }
//        }

    }

    private LinkedHashSet<String> validaJogadasDisponiveisDaDama(int linha, int coluna) {
        LinkedHashSet<String> possiveisJogadas = new LinkedHashSet<>();

        if (linha <= 8 && coluna <= 8) {
            int l = linha;
            int c = coluna;
            while (l <= 8 && c <= 8) {
                if (Objects.equals(this.matriz[l][c], "*")) {
                    possiveisJogadas.add(String.valueOf(l).concat(String.valueOf(matriz[0][c])));
                }
                l++;
                c++;
            }
        }
        if (linha <= 8 && coluna >= 1) {
            int l = linha;
            int c = coluna;
            while (l <= 8 && c >= 1) {
                if (Objects.equals(this.matriz[l][c], "*")) {
                    possiveisJogadas.add(String.valueOf(l).concat(String.valueOf(matriz[0][c])));
                }
                l++;
                c--;
            }
        }
        if (linha >= 1 && coluna >= 1) {
            int l = linha;
            int c = coluna;
            while (l >= 1 && c >= 1) {
                if (Objects.equals(this.matriz[l][c], "*")) {
                    possiveisJogadas.add(String.valueOf(l).concat(String.valueOf(matriz[0][c])));
                }
                l--;
                c--;
            }
        }
        if (linha >= 1 && coluna <= 8) {
            int l = linha;
            int c = coluna;
            while (l >= 1 && c <= 8) {
                if (Objects.equals(this.matriz[l][c], "*")) {
                    possiveisJogadas.add(String.valueOf(l).concat(String.valueOf(matriz[0][c])));
                }
                l--;
                c++;
            }
        }
        possiveisJogadas.forEach(a -> System.out.print(a + ", "));
        System.out.println();
        return possiveisJogadas;
    }

    private LinkedList<String> validaComePecaDama() {
        LinkedList<String> listaDePosicaoParaComer = new LinkedList<>();
        String dama = "";

        if (VEZ_JOGADOR.equals("P")) {
            dama = "𝓟";
        } else if (VEZ_JOGADOR.equals("B")) {
            dama = "𝓑";
        }

        for (int linha = 1; linha < tamanho; linha++) {
            for (int coluna = 1; coluna < tamanho; coluna++) {
                if (Objects.equals(matriz[linha][coluna], dama)) {
                    int l = linha;
                    int c = coluna;
                    while (l < 8 && c < 8) {
                        if (!Objects.equals(matriz[l][c], dama) && !Objects.equals(matriz[l][c], VEZ_JOGADOR) && !Objects.equals(matriz[l][c], "*")) {
                            if (Objects.equals(matriz[l + 1][c + 1], "*")) {
                                listaDasDamasObrigadoComer.add(String.valueOf(linha).concat(String.valueOf(matriz[0][coluna])));
                                listaPecasComidasPelaDama.add(String.valueOf(l).concat(String.valueOf(matriz[0][c])));
                                listaDePosicaoParaComer.add(String.valueOf(l + 1).concat(String.valueOf(matriz[0][c + 1])));
                                break;
                            }
                        }
                        l++;
                        c++;
                    }
                }
                if (Objects.equals(matriz[linha][coluna], dama)) {
                    int l = linha;
                    int c = coluna;
                    while (l < 8 && c > 1) {
                        if (!Objects.equals(matriz[l][c], dama) && !Objects.equals(matriz[l][c], VEZ_JOGADOR) && !Objects.equals(matriz[l][c], "*")) {
                            if (Objects.equals(matriz[l + 1][c - 1], "*")) {
                                listaDasDamasObrigadoComer.add(String.valueOf(linha).concat(String.valueOf(matriz[0][coluna])));
                                listaPecasComidasPelaDama.add(String.valueOf(l).concat(String.valueOf(matriz[0][c])));
                                listaDePosicaoParaComer.add(String.valueOf(l + 1).concat(String.valueOf(matriz[0][c - 1])));
                                break;
                            }
                        }
                        l++;
                        c--;
                    }
                }
                if (Objects.equals(matriz[linha][coluna], dama)) {
                    int l = linha;
                    int c = coluna;
                    while (l > 1 && c > 1) {
                        if (!Objects.equals(matriz[l][c], dama) && !Objects.equals(matriz[l][c], VEZ_JOGADOR) && !Objects.equals(matriz[l][c], "*")) {
                            if (Objects.equals(matriz[l - 1][c - 1], "*")) {
                                listaDasDamasObrigadoComer.add(String.valueOf(linha).concat(String.valueOf(matriz[0][coluna])));
                                listaPecasComidasPelaDama.add(String.valueOf(l).concat(String.valueOf(matriz[0][c])));
                                listaDePosicaoParaComer.add(String.valueOf(l - 1).concat(String.valueOf(matriz[0][c - 1])));
                                break;
                            }
                        }
                        l--;
                        c--;
                    }
                }
                if (Objects.equals(matriz[linha][coluna], dama)) {
                    int l = linha;
                    int c = coluna;
                    while (l > 1 && c < 8) {
                        if (!Objects.equals(matriz[l][c], dama) && !Objects.equals(matriz[l][c], VEZ_JOGADOR) && !Objects.equals(matriz[l][c], "*")) {
                            if (Objects.equals(matriz[l - 1][c + 1], "*")) {
                                listaDasDamasObrigadoComer.add(String.valueOf(linha).concat(String.valueOf(matriz[0][coluna])));
                                listaPecasComidasPelaDama.add(String.valueOf(l).concat(String.valueOf(matriz[0][c])));
                                listaDePosicaoParaComer.add(String.valueOf(l - 1).concat(String.valueOf(matriz[0][c + 1])));
                                break;
                            }
                        }
                        l--;
                        c++;
                    }
                }
            }
        }

        if (!listaDePosicaoParaComer.isEmpty()) {
            LinkedList<String> listaAux = new LinkedList<>(listaDePosicaoParaComer);
            LinkedList<String> listaDePosicaoParaComerAux = new LinkedList<>();
            LinkedList<String> listaPecasComidasAux = new LinkedList<>(listaPecasComidasPelaDama);
            LinkedList<String> listaPecasComidasAuxSP = new LinkedList<>();
            int tamanhoLista = 0;

            for (int i = 0; i < listaAux.size(); i++) {
                listaDePosicaoParaComer.clear();
                listaDePosicaoParaComer.add(listaAux.get(i));
                listaPecasComidasAuxSP.clear();
                listaPecasComidasPelaDama.clear();
                listaPecasComidasPelaDama.add(listaPecasComidasAux.get(i));

                validaComePecaDama(listaAux.get(i), listaDePosicaoParaComer);
                if (listaDePosicaoParaComer.size() > tamanhoLista) {
                    tamanhoLista = listaDePosicaoParaComer.size();
                    listaDePosicaoParaComerAux = listaDePosicaoParaComer;
                    listaPecasComidasAuxSP = listaPecasComidasPelaDama;
                }
            }
            listaPecasComidasPelaDama = listaPecasComidasAuxSP;
            listaDePosicaoParaComer = listaDePosicaoParaComerAux;
        }

        listaDePosicaoParaComer.forEach(a -> System.out.print(a + ", "));
        System.out.println();
        return listaDePosicaoParaComer;
    }

    private void validaComePecaDama(String coordenada, LinkedList<String> listaDePosicaoParaComer) {
        int linha = getLinha(coordenada);
        int coluna = getColuna(coordenada);
        String dama = "";

        if (VEZ_JOGADOR.equals("P")) {
            dama = "𝓟";
        } else if (VEZ_JOGADOR.equals("B")) {
            dama = "𝓑";
        }

        int l = linha;
        int c = coluna;
        while (l < 8 && c < 8) {
            if (!Objects.equals(matriz[l][c], dama) && !Objects.equals(matriz[l][c], VEZ_JOGADOR) && !Objects.equals(matriz[l][c], "*")) {
                if (!listaPecasComidasPelaDama.contains(String.valueOf(l).concat(matriz[0][c]))) {
                    if (Objects.equals(matriz[l + 1][c + 1], "*")) {
                        listaPecasComidasPelaDama.add(String.valueOf(l).concat(String.valueOf(matriz[0][c])));

                        if (!listaDePosicaoParaComer.get(listaDePosicaoParaComer.size() - 1).equals(coordenada)) {
                            listaDePosicaoParaComer.add(coordenada);
                        }

                        listaDePosicaoParaComer.add(String.valueOf(l + 1).concat(String.valueOf(matriz[0][c + 1])));
                        validaComePecaDama(String.valueOf(l + 1).concat(String.valueOf(matriz[0][c + 1])), listaDePosicaoParaComer);
                    }
                }
            }
            l++;
            c++;
        }
        l = linha;
        c = coluna;
        while (l < 8 && c > 1) {
            if (!Objects.equals(matriz[l][c], dama) && !Objects.equals(matriz[l][c], VEZ_JOGADOR) && !Objects.equals(matriz[l][c], "*")) {
                if (!listaPecasComidasPelaDama.contains(String.valueOf(l).concat(matriz[0][c]))) {
                    if (Objects.equals(matriz[l + 1][c - 1], "*")) {
                        listaPecasComidasPelaDama.add(String.valueOf(l).concat(String.valueOf(matriz[0][c])));

                        if (!listaDePosicaoParaComer.get(listaDePosicaoParaComer.size() - 1).equals(coordenada)) {
                            listaDePosicaoParaComer.add(coordenada);
                        }

                        listaDePosicaoParaComer.add(String.valueOf(l + 1).concat(String.valueOf(matriz[0][c - 1])));
                        validaComePecaDama(String.valueOf(l + 1).concat(String.valueOf(matriz[0][c - 1])), listaDePosicaoParaComer);
                    }
                }
            }
            l++;
            c--;
        }
        l = linha;
        c = coluna;
        while (l > 1 && c > 1) {
            if (!Objects.equals(matriz[l][c], dama) && !Objects.equals(matriz[l][c], VEZ_JOGADOR) && !Objects.equals(matriz[l][c], "*")) {
                if (!listaPecasComidasPelaDama.contains(String.valueOf(l).concat(matriz[0][c]))) {
                    if (Objects.equals(matriz[l - 1][c - 1], "*")) {
                        listaPecasComidasPelaDama.add(String.valueOf(l).concat(String.valueOf(matriz[0][c])));

                        if (!listaDePosicaoParaComer.get(listaDePosicaoParaComer.size() - 1).equals(coordenada)) {
                            listaDePosicaoParaComer.add(coordenada);
                        }

                        listaDePosicaoParaComer.add(String.valueOf(l - 1).concat(String.valueOf(matriz[0][c - 1])));
                        validaComePecaDama(String.valueOf(l - 1).concat(String.valueOf(matriz[0][c - 1])), listaDePosicaoParaComer);
                    }
                }
            }
            l--;
            c--;
        }
        l = linha;
        c = coluna;
        while (l > 1 && c < 8) {
            if (!Objects.equals(matriz[l][c], dama) && !Objects.equals(matriz[l][c], VEZ_JOGADOR) && !Objects.equals(matriz[l][c], "*")) {
                if (!listaPecasComidasPelaDama.contains(String.valueOf(l).concat(matriz[0][c]))) {
                    if (Objects.equals(matriz[l - 1][c + 1], "*")) {
                        listaPecasComidasPelaDama.add(String.valueOf(l).concat(String.valueOf(matriz[0][c])));

                        if (!listaDePosicaoParaComer.get(listaDePosicaoParaComer.size() - 1).equals(coordenada)) {
                            listaDePosicaoParaComer.add(coordenada);
                        }

                        listaDePosicaoParaComer.add(String.valueOf(l - 1).concat(String.valueOf(matriz[0][c + 1])));
                        validaComePecaDama(String.valueOf(l - 1).concat(String.valueOf(matriz[0][c + 1])), listaDePosicaoParaComer);
                    }
                }
            }
            l--;
            c++;
        }
    }


    private int getLinha(String coordenada) {
        return Integer.parseInt(String.valueOf(coordenada.charAt(0)));
    }

    private int getColuna(String coordenada) {
        return Character.getNumericValue(coordenada.charAt(1)) - 9;
    }
}

