import java.util.*;

public class Tabuleiro {

    private static final String ALFABETO = "0ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String VEZ_JOGADOR = "";

    public String[][] matriz;
    private int tamanho;
    private LinkedList<String> listaDasPecasObrigadoComer;
    private LinkedList<String> listaPecasComidas;
    private LinkedList<String> listaValidaComePeca;

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

        matriz[5][8] = "P";
        matriz[3][1] = "P";
        matriz[4][7] = "B";
        matriz[2][5] = "B";
        matriz[2][3] = "B";
        matriz[4][3] = "B";
        matriz[4][5] = "B";
        matriz[2][7] = "B";
        matriz[1][3] = "B";
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
        listaDasPecasObrigadoComer = new LinkedList<>();
        listaPecasComidas = new LinkedList<>();
        listaValidaComePeca = validaComePeca();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Qual coordenada da peça que você gostaria de mover?");


        String coordenadaAnterior = scanner.nextLine().toUpperCase();

        if (listaDasPecasObrigadoComer.isEmpty()) {
            while (!coordenadaAnterior.matches("[1-9][a-zA-H]")) {
                System.out.println("Coordenada inválida, por favor digite no padrão correto (LinhaColuna)");
                coordenadaAnterior = scanner.nextLine().toUpperCase();
            }

        } else {
            while (!listaDasPecasObrigadoComer.contains(coordenadaAnterior) && !coordenadaAnterior.equals("")) {
                System.out.println("Coordenada inválida, você é obrigado a comer a peça, você pode escolher as seguintes peças: ");
                listaDasPecasObrigadoComer.forEach(a -> System.out.print(a + ", "));
                System.out.println();
                coordenadaAnterior = scanner.nextLine().toUpperCase();
            }
        }
        String atualDama = "";
        while (condicional == 0) {
            int linha = getLinha(coordenadaAnterior);
            int coluna = getColuna(coordenadaAnterior);
            if(peca.equals("P")){
                atualDama = "𝓟";
            }else if(peca.equals("B")){
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
        if (listaDasPecasObrigadoComer.isEmpty()) {
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
            //if (!matriz[linha][coluna].equals(atualDama)){
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
            listaPecasComidas.remove(0);
            matriz[linhaListaComida][colunaListaComida] = "*";
            //}
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

        if (listaValidaComePeca.size() <= 2) {
            if (VEZ_JOGADOR.equals("P") && novaLinha == 1) {
                matriz[novaLinha][novaColuna] = "𝓟";
            } else if (VEZ_JOGADOR.equals("B") && novaLinha == 8) {
                matriz[novaLinha][novaColuna] = "𝓑";
            }
            VEZ_JOGADOR = setaVezProximoJogador(peca);
        }
        imprimirTabuleiro();
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
    private LinkedList<String> validaComePeca() {

        List<String> listaDePecaQuePodeComer = new ArrayList<>();
        LinkedList<String> listaDePosicaoParaComer = new LinkedList<>();
        LinkedList<String> listaInterseccoes = new LinkedList<>();

        for (int linha = 1; linha < tamanho; linha++) {
            for (int coluna = 1; coluna < tamanho; coluna++) {
                if (linha < 7 && coluna < 7) {
                    if (Objects.equals(this.matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna + 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna + 1], "*")) {
                        if (Objects.equals(this.matriz[linha + 2][coluna + 2], "*")) {
                            listaInterseccoes.add(String.valueOf(linha + 1).concat(matriz[0][coluna + 1]));
                            listaDePecaQuePodeComer.add(String.valueOf(linha).concat(matriz[0][coluna]));
                            listaDePosicaoParaComer.add(String.valueOf(linha + 2).concat(matriz[0][coluna + 2]));
                        }
                    }
                }
                if (linha < 7 && coluna > 2) {
                    if (Objects.equals(this.matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna - 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna - 1], "*")) {
                        if (Objects.equals(this.matriz[linha + 2][coluna - 2], "*")) {
                            listaInterseccoes.add(String.valueOf(linha + 1).concat(matriz[0][coluna - 1]));
                            listaDePecaQuePodeComer.add(String.valueOf(linha).concat(matriz[0][coluna]));
                            listaDePosicaoParaComer.add(String.valueOf(linha + 2).concat(matriz[0][coluna - 2]));
                        }

                    }
                }
                if (linha > 2 && coluna > 2) {
                    if (Objects.equals(this.matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna - 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna - 1], "*")) {
                        if (Objects.equals(this.matriz[linha - 2][coluna - 2], "*")) {
                            listaInterseccoes.add(String.valueOf(linha - 1).concat(matriz[0][coluna - 1]));
                            listaDePecaQuePodeComer.add(String.valueOf(linha).concat(matriz[0][coluna]));
                            listaDePosicaoParaComer.add(String.valueOf(linha - 2).concat(matriz[0][coluna - 2]));
                        }

                    }
                }
                if (linha > 2 && coluna < 7) {
                    if (Objects.equals(this.matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna + 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna + 1], "*")) {
                        if (Objects.equals(this.matriz[linha - 2][coluna + 2], "*")) {
                            listaInterseccoes.add(String.valueOf(linha - 1).concat(matriz[0][coluna + 1]));

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
        if (!listaDePosicaoParaComer.isEmpty()) {
            validaComePeca(listaDePosicaoParaComer.get(0), listaDePosicaoParaComer, listaInterseccoes);
        }

        System.out.println("Posição do local que terá que mover para comer a peça: ");
        listaDePosicaoParaComer.forEach(a -> System.out.print(a + ", "));
        listaDasPecasObrigadoComer.addAll(listaDePecaQuePodeComer);
        if (!listaInterseccoes.isEmpty()) {
            listaPecasComidas.addAll(listaInterseccoes);
        }
        System.out.println();
        return listaDePosicaoParaComer;
    }

    private void validaComePeca(String coordenada, LinkedList<String> listaDePosicaoParaComer, LinkedList<String> listaInterseccoes) {
        int linha = getLinha(coordenada);
        int coluna = getColuna(coordenada);

        if (linha < 7 && coluna < 7) {

            if (!Objects.equals(this.matriz[linha + 1][coluna + 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna + 1], "*")) {
                if (Objects.equals(this.matriz[linha + 2][coluna + 2], "*")) {
                    if (!listaInterseccoes.contains(String.valueOf(linha + 1).concat(matriz[0][coluna + 1]))) {
                        listaInterseccoes.add(String.valueOf(linha + 1).concat(matriz[0][coluna + 1]));
                        listaDePosicaoParaComer.add(String.valueOf(linha + 2).concat(matriz[0][coluna + 2]));
                        validaComePeca(String.valueOf(linha + 2).concat(matriz[0][coluna + 2]), listaDePosicaoParaComer, listaInterseccoes);
                    }

                }
            }
        }
        if (linha < 7 && coluna > 2) {
            if (!Objects.equals(this.matriz[linha + 1][coluna - 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna - 1], "*")) {
                if (Objects.equals(this.matriz[linha + 2][coluna - 2], "*")) {
                    if (!listaInterseccoes.contains(String.valueOf(linha + 1).concat(matriz[0][coluna - 1]))) {
                        listaInterseccoes.add(String.valueOf(linha + 1).concat(matriz[0][coluna - 1]));
                        listaDePosicaoParaComer.add(String.valueOf(linha + 2).concat(matriz[0][coluna - 2]));
                        validaComePeca(String.valueOf(linha + 2).concat(matriz[0][coluna - 2]), listaDePosicaoParaComer, listaInterseccoes);
                    }

                }
            }
        }
        if (linha > 2 && coluna > 2) {
            if (!Objects.equals(this.matriz[linha - 1][coluna - 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna - 1], "*")) {
                if (Objects.equals(this.matriz[linha - 2][coluna - 2], "*")) {
                    if (!listaInterseccoes.contains(String.valueOf(linha - 1).concat(matriz[0][coluna - 1]))) {
                        listaInterseccoes.add(String.valueOf(linha - 1).concat(matriz[0][coluna - 1]));
                        listaDePosicaoParaComer.add(String.valueOf(linha - 2).concat(matriz[0][coluna - 2]));
                        validaComePeca(String.valueOf(linha - 2).concat(matriz[0][coluna - 2]), listaDePosicaoParaComer, listaInterseccoes);
                    }

                }
            }
        }
        if (linha > 2 && coluna < 7) {
            if (!Objects.equals(this.matriz[linha - 1][coluna + 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna + 1], "*")) {
                if (Objects.equals(this.matriz[linha - 2][coluna + 2], "*")) {
                    if (!listaInterseccoes.contains(String.valueOf(linha - 1).concat(matriz[0][coluna + 1]))) {
                        listaInterseccoes.add(String.valueOf(linha - 1).concat(matriz[0][coluna + 1]));
                        listaDePosicaoParaComer.add(String.valueOf(linha - 2).concat(matriz[0][coluna + 2]));
                        validaComePeca(String.valueOf(linha - 2).concat(matriz[0][coluna + 2]), listaDePosicaoParaComer, listaInterseccoes);
                    }

                }
            }
        }
        listaDePosicaoParaComer.add("|");

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

    //Para ele não somar o B que já foi comido, ele deve validar que não é a posição da peça que ele passou por cima
    //Deve ser criada um int que soma a quantidade de peças comidas e ele deve ser zerado  no inicio do método
    //para ser possivel pegar a diferença de quantidades que se pode comer e deve ser passado esse int por parametro
    //para o método;
    private LinkedList<String> validaComePecaDama() {

        List<String> listaDePecaQuePodeComer = new ArrayList<>();
        LinkedList<String> listaDePosicaoParaComer = new LinkedList<>();
        LinkedList<String> listaInterseccoes = new LinkedList<>();

        for (int linha = 1; linha < tamanho; linha++) {
            for (int coluna = 1; coluna < tamanho; coluna++) {
                if (linha < 7 && coluna < 7) {
                    if (Objects.equals(this.matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna + 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna + 1], "*")) {
                        if (Objects.equals(this.matriz[linha + 2][coluna + 2], "*")) {
                            listaInterseccoes.add(String.valueOf(linha + 1).concat(matriz[0][coluna + 1]));
                            listaDePecaQuePodeComer.add(String.valueOf(linha).concat(matriz[0][coluna]));
                            listaDePosicaoParaComer.add(String.valueOf(linha + 2).concat(matriz[0][coluna + 2]));
                        }
                    }
                }
                if (linha < 7 && coluna > 2) {
                    if (Objects.equals(this.matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna - 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha + 1][coluna - 1], "*")) {
                        if (Objects.equals(this.matriz[linha + 2][coluna - 2], "*")) {
                            listaInterseccoes.add(String.valueOf(linha + 1).concat(matriz[0][coluna - 1]));
                            listaDePecaQuePodeComer.add(String.valueOf(linha).concat(matriz[0][coluna]));
                            listaDePosicaoParaComer.add(String.valueOf(linha + 2).concat(matriz[0][coluna - 2]));
                        }

                    }
                }
                if (linha > 2 && coluna > 2) {
                    if (Objects.equals(this.matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna - 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna - 1], "*")) {
                        if (Objects.equals(this.matriz[linha - 2][coluna - 2], "*")) {
                            listaInterseccoes.add(String.valueOf(linha - 1).concat(matriz[0][coluna - 1]));
                            listaDePecaQuePodeComer.add(String.valueOf(linha).concat(matriz[0][coluna]));
                            listaDePosicaoParaComer.add(String.valueOf(linha - 2).concat(matriz[0][coluna - 2]));
                        }

                    }
                }
                if (linha > 2 && coluna < 7) {
                    if (Objects.equals(this.matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna + 1], VEZ_JOGADOR) && !Objects.equals(this.matriz[linha - 1][coluna + 1], "*")) {
                        if (Objects.equals(this.matriz[linha - 2][coluna + 2], "*")) {
                            listaInterseccoes.add(String.valueOf(linha - 1).concat(matriz[0][coluna + 1]));

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
        if (!listaDePosicaoParaComer.isEmpty()) {
            validaComePeca(listaDePosicaoParaComer.get(0), listaDePosicaoParaComer, listaInterseccoes);
        }

        System.out.println("Posição do local que terá que mover para comer a peça: ");
        listaDePosicaoParaComer.forEach(a -> System.out.print(a + ", "));
        listaDasPecasObrigadoComer.addAll(listaDePecaQuePodeComer);
        if (!listaInterseccoes.isEmpty()) {
            listaPecasComidas.addAll(listaInterseccoes);
        }
        System.out.println();
        return listaDePosicaoParaComer;
    }


    private int getLinha(String coordenada) {
        return Integer.parseInt(String.valueOf(coordenada.charAt(0)));
    }

    private int getColuna(String coordenada) {
        return Character.getNumericValue(coordenada.charAt(1)) - 9;
    }
}

