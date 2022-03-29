import java.util.*;

public class Tabuleiro {

    private static final String ALFABETO = "0ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String VEZ_JOGADOR = "";
    private int contadorMovimentoDamaSemComerNada = 0;
    public String[][] matriz;
    private int tamanho;
    private LinkedHashSet<String> listaDasPecasObrigadoComer;
    private LinkedList<String> listaPecasComidas;
    private LinkedList<String> listaValidaComePeca;
    private LinkedHashSet<String> listaDasDamasObrigadoComer;
    private LinkedList<String> listaDeValidaDamaComePeca;
    private LinkedList<String> listaPecasComidasPelaDama;
    private LinkedHashSet<String> listaPecasTravadas;
    private String ultimaPecaUtilizadoParaComer = "";

    //Preenche o tabuleiro com asterisco
    public Tabuleiro(int tamanho) {
        tamanho++;


        this.tamanho = tamanho;

        matriz = new String[tamanho][tamanho];

        matriz[0][0] = "  ";

        for (int coluna = 1; coluna < this.tamanho; coluna++) {
            matriz[0][coluna] = String.valueOf(ALFABETO.charAt(coluna));
        }

        for (int linha = 1; linha < this.tamanho; linha++) {
            String numeroDaLinha = "";

            numeroDaLinha += linha + " ";

            matriz[linha][0] = numeroDaLinha;
        }

        for (int linha = 1; linha < this.tamanho; linha++) {
            for (int coluna = 1; coluna < this.tamanho; coluna++) {
                matriz[linha][coluna] = "*";
            }
        }
        //preenche as peças do tabuleiro(B e P)
        matrizDefault();
    }

    //preenche as peças do tabuleiro(B e P)
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
                    matriz[linha][coluna] = "B";
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
                    matriz[linha][coluna] = "P";
                    coluna = coluna + 2;
                }
            }
        }
    }


    public void matrizTeste() {

        matriz[7][3] = "B";
        matriz[8][2] = "P";
        matriz[5][5] = "B";
        matriz[5][3] = "B";
        matriz[3][7] = "B";
    }


    public void imprimirTabuleiro() {

        for (int linha = 0; linha < tamanho; linha++) {
            System.out.print("    ");
            for (int coluna = 0; coluna < tamanho; coluna++) {
                System.out.print("  ");
                System.out.print(matriz[linha][coluna]);
            }
            System.out.println("");
        }

        System.out.println("-----------------------------------");
    }

    //pergunta qual a peça que irá começar o game
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
        //popula as listas chamando a validação para saber se alguma peça tem que comer outra peça
        listaDasPecasObrigadoComer = new LinkedHashSet<>();
        listaPecasComidas = new LinkedList<>();
        listaValidaComePeca = validaComePeca();
        listaDasDamasObrigadoComer = new LinkedHashSet<>();
        //popula as listas chamando a validação para saber se alguma dama tem que comer alguma peça
        listaPecasComidasPelaDama = new LinkedList<>();
        listaDeValidaDamaComePeca = validaComePecaDama();
        //valida se tem alguma peça travada no tabuleiro(se não pode se mover para nenhum local)
        listaPecasTravadas = validaPecaTravada();

        //pega quem é a atual dama a partir da vez do jogador
        String atualDama = "";
        if (peca.equals("P")) {
            atualDama = "𝓟";
        } else if (peca.equals("B")) {
            atualDama = "𝓑";
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Qual coordenada da peça que você gostaria de mover?");

        String coordenadaAnterior = scanner.nextLine().toUpperCase();

        if (!ultimaPecaUtilizadoParaComer.equals(VEZ_JOGADOR) && !ultimaPecaUtilizadoParaComer.equals(atualDama)) {
            ultimaPecaUtilizadoParaComer = "";
        }

        //valida se não é obrigado a comer nada e se digitou corretamente a coordenada ou se a peça não está travada
        if (listaDasPecasObrigadoComer.isEmpty() && listaDasDamasObrigadoComer.isEmpty()) {
            while (!coordenadaAnterior.matches("[1-9][a-zA-H]") || listaPecasTravadas.contains(coordenadaAnterior)) {
                System.out.println("Coordenada inválida, essa peça está travada ou você digitou uma coordenada inválida. Padrão correto:(LinhaColuna)");
                coordenadaAnterior = scanner.nextLine().toUpperCase();
            }
            //caso for obrigado a comer entra aqui
        } else {
            //valida se a peça normal pode comer mais peças do que a dama e se caso ele estiver em um combo ele tem que selecionar a mesma peça
            if (listaValidaComePeca.size() > listaDeValidaDamaComePeca.size() || ultimaPecaUtilizadoParaComer.equals(VEZ_JOGADOR)) {
                System.out.println("Você é obrigado a comer, escolha a peça: ");
                listaDasPecasObrigadoComer.forEach(a -> System.out.print(a + ", "));
                System.out.println();
                //valida se o jogador escolheu uma peça valida, no caso a que pode comer mais peças
                while (!listaDasPecasObrigadoComer.contains(coordenadaAnterior) && !coordenadaAnterior.equals("")) {
                    System.out.println("Coordenada inválida, você é obrigado a comer a peça, você pode escolher as seguintes peças: ");
                    listaDasPecasObrigadoComer.forEach(a -> System.out.print(a + ", "));
                    System.out.println();
                    coordenadaAnterior = scanner.nextLine().toUpperCase();
                }
                //valida se a peça normal pode comer uma quantidade igual de peças que a dama
            } else if (listaValidaComePeca.size() == listaDeValidaDamaComePeca.size()) {
                //valida se esta em combo com a dama, caso sim ele é obrigado a escolher a mesma peça
                if (ultimaPecaUtilizadoParaComer.equals(atualDama)) {
                    System.out.println("Você é obrigado a comer, escolha a peça: ");
                    listaDasDamasObrigadoComer.forEach(a -> System.out.print(a + ", "));
                    System.out.println();
                    //valida se o jogador escolheu uma peça valida, no caso ele tem a opção de escolher entre as peças ou a dama(já que a quantidade de peças que podem ser comidas são iguais)
                    while (!listaDasDamasObrigadoComer.contains(coordenadaAnterior) && !coordenadaAnterior.equals("")) {
                        System.out.println("Coordenada inválida, você é obrigado a comer a peça, você pode escolher as seguintes peças: ");
                        listaDasDamasObrigadoComer.forEach(a -> System.out.print(a + ", "));
                        System.out.println();
                        coordenadaAnterior = scanner.nextLine().toUpperCase();
                    }
                    //caso não estiver em combo pode selecionar qualquer peça para comer(já que aqui é um caso que as peças tem a mesma quantidade de peças para comer)
                } else {
                    System.out.println("Você é obrigado a comer, escolha a peça: ");
                    listaDasPecasObrigadoComer.forEach(a -> System.out.print(a + ", "));
                    listaDasDamasObrigadoComer.forEach(a -> System.out.print(a + ", "));
                    System.out.println();
                    //valida se o jogador escolheu uma peça valida, no caso ele tem a opção de escolher entre as peças ou a dama(já que a quantidade de peças que podem ser comidas são iguais)
                    while (!listaDasPecasObrigadoComer.contains(coordenadaAnterior) && !listaDasDamasObrigadoComer.contains(coordenadaAnterior) && !coordenadaAnterior.equals("")) {
                        System.out.println("Coordenada inválida, você é obrigado a comer a peça, você pode escolher as seguintes peças: ");
                        listaDasPecasObrigadoComer.forEach(a -> System.out.print(a + ", "));
                        listaDasDamasObrigadoComer.forEach(a -> System.out.print(a + ", "));
                        System.out.println();
                        coordenadaAnterior = scanner.nextLine().toUpperCase();
                    }
                }

                //caso a dama pode comer mais peças cai aqui e não estiver em combo com uma peça normal
            } else {
                System.out.println("Você é obrigado a comer, escolha a peça: ");
                listaDasDamasObrigadoComer.forEach(a -> System.out.print(a + ", "));
                System.out.println();
                //valida se o jogador escolheu uma peça valida, no caso a dama que pode comer mais peças
                while (!coordenadaAnterior.equals("") && !listaDasDamasObrigadoComer.contains(coordenadaAnterior)) {
                    System.out.println("Coordenada inválida, você é obrigado a comer a peça, você pode escolher as seguintes peças: ");
                    listaDasDamasObrigadoComer.forEach(a -> System.out.print(a + ", "));
                    System.out.println();
                    coordenadaAnterior = scanner.nextLine().toUpperCase();
                }
            }
        }

        while (condicional == 0) {
            //valida se a coordenada é válida
            if (coordenadaAnterior.matches("[1-9][a-zA-H]")) {
                int linha = getLinha(coordenadaAnterior);
                int coluna = getColuna(coordenadaAnterior);
                //válida se a posição escolhida não é a peça da vez do jogador e válida se a peça não está travada
                if (!(peca.equals(matriz[linha][coluna])) && !atualDama.equals(matriz[linha][coluna]) || listaDasPecasObrigadoComer.size() == 0 && listaDasDamasObrigadoComer.size() == 0 && listaPecasTravadas.contains(coordenadaAnterior)) {
                    System.out.println("A peça está travada ou não tem uma peça sua nessa coordenada, agora é a vez do jogador de: " + peca);
                    coordenadaAnterior = scanner.nextLine().toUpperCase();
                } else {
                    condicional = 1;
                }
            } else {
                System.out.println("Por favor digite uma coordenada válida");
                coordenadaAnterior = scanner.nextLine().toUpperCase();
            }
        }
        //começa o fluxo pra qual coordenada a peça vai se mover
        String novaCoordenada = "";
        //caso não for obrigado a comer nenhuma peça
        if (listaDasPecasObrigadoComer.isEmpty() && listaDasDamasObrigadoComer.isEmpty()) {
            //valida se não é uma dama a peça escolhida
            if (!matriz[getLinha(coordenadaAnterior)][getColuna(coordenadaAnterior)].equals(atualDama)) {
                contadorMovimentoDamaSemComerNada = 0;
                System.out.println("Você pode mover a sua peça para as posições printadas abaixo:");
                //valida as possiveis jogadas da peça normal
                List<String> possiveisJogadas = validaJogadasDisponiveisDaPeca(getLinha(coordenadaAnterior), getColuna(coordenadaAnterior));
                System.out.println("Para qual coordenada gostaria de mover a peça escolhida?");
                novaCoordenada = scanner.nextLine().toUpperCase();
                //valida se ele escolheu uma jogada possivel
                while (!possiveisJogadas.contains(novaCoordenada)) {
                    System.out.println("Coordenada inválida, por favor digite no padrão correto (LinhaColuna)");
                    novaCoordenada = scanner.nextLine().toUpperCase();
                }
                //caso for uma dama entra aqui
            } else {
                //conta +1 pra caso ele mover a dama e não comer ninguem
                contadorMovimentoDamaSemComerNada++;
                System.out.println("Você pode mover a sua dama para as posições printadas abaixo:");
                //valida as possiveis jogadas da dama escolhida
                LinkedHashSet<String> possiveisJogadas = validaJogadasDisponiveisDaDama(getLinha(coordenadaAnterior), getColuna(coordenadaAnterior));
                System.out.println("Para qual coordenada gostaria de mover a peça escolhida?");
                novaCoordenada = scanner.nextLine().toUpperCase();
                //valida se ele escolheu uma jogada possivel
                while (!possiveisJogadas.contains(novaCoordenada)) {
                    System.out.println("Coordenada inválida, por favor digite no padrão correto (LinhaColuna)");
                    novaCoordenada = scanner.nextLine().toUpperCase();
                }
            }
            //caso ele for obrigado a comer alguma peça ele cai aqui
        } else {
            //zera o contador
            contadorMovimentoDamaSemComerNada = 0;
            //valida se a peça não é uma dama
            if (!matriz[getLinha(coordenadaAnterior)][getColuna(coordenadaAnterior)].equals(atualDama)) {
                System.out.println("Você pode mover a sua peça para a posição abaixo: ");
                System.out.println(listaValidaComePeca.get(0));
                System.out.println("Para qual coordenada gostaria de mover a peça escolhida?");
                novaCoordenada = scanner.nextLine().toUpperCase();
                //a posição válida seria a da primeira peça da lista se posições que ele tem que ir(caso tiver um combo também é valida somente a primeira posição)
                while (!listaValidaComePeca.get(0).equals(novaCoordenada)) {
                    System.out.println("Coordenada inválida, por favor digite a seguinte coordenada: ");
                    System.out.println(listaValidaComePeca.get(0));
                    novaCoordenada = scanner.nextLine().toUpperCase();
                }
                //caso for um combo ele seta a posição anterior como asterisco
                int linhaListaComida = getLinha(listaPecasComidas.get(0));
                int colunaListaComida = getColuna(listaPecasComidas.get(0));
                matriz[linhaListaComida][colunaListaComida] = "*";

                //caso for uma dama cai aqui
            } else {
                //valida se é a ultima peça que a dama pode comer(caso for a ultima a dama pode escolher qualquer casa disponível atrás da peça alvo)
                if (listaDeValidaDamaComePeca.size() == 1) {
                    listaDeValidaDamaComePeca = damaComeUltimaPeca(coordenadaAnterior, listaPecasComidasPelaDama.get(0));
                }
                System.out.println("Você pode mover a sua peça para a posição abaixo: ");
                System.out.println(listaDeValidaDamaComePeca.get(0));
                System.out.println("Para qual coordenada gostaria de mover a peça escolhida?");
                novaCoordenada = scanner.nextLine().toUpperCase();
                //a posição válida seria a da primeira peça da lista se posições que ele tem que ir(caso tiver um combo também é valida somente a primeira posição)
                while (!listaDeValidaDamaComePeca.get(0).equals(novaCoordenada)) {
                    System.out.println("Coordenada inválida, por favor digite a seguinte coordenada: ");
                    System.out.println(listaDeValidaDamaComePeca.get(0));
                    novaCoordenada = scanner.nextLine().toUpperCase();
                }
                //caso for um combo ele seta a posição anterior como asterisco
                int linhaListaComida = getLinha(listaPecasComidasPelaDama.get(0));
                int colunaListaComida = getColuna(listaPecasComidasPelaDama.get(0));
                matriz[linhaListaComida][colunaListaComida] = "*";
            }
        }

        ultimaPecaUtilizadoParaComer = matriz[getLinha(coordenadaAnterior)][getColuna(coordenadaAnterior)];

        //passa a posição da peça escolhida e a posição alvo para o método moverPeça
        System.out.println();
        return coordenadaAnterior + "," + novaCoordenada;
    }

    //muda a vez do jogador
    public String setaVezProximoJogador(String peca) {

        peca.toUpperCase();
        if (peca.equals("B") || peca.equals("𝓑")) {
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

        String peca = matriz[linhaAnterior][colunaAnterior];

        //pega quem é a atual dama a partir da vez do jogador
        String atualDama = "";
        if (VEZ_JOGADOR.equals("P")) {
            atualDama = "𝓟";
        } else if (VEZ_JOGADOR.equals("B")) {
            atualDama = "𝓑";
        }

        int condicional = 0;
        String pecaWhile = "";

        int novaLinha = getLinha(novaCoordenada);
        int novaColuna = getColuna(novaCoordenada);
        //se a peça escolhida não for uma dama
        if (!matriz[linhaAnterior][colunaAnterior].equals("𝓟") && !matriz[linhaAnterior][colunaAnterior].equals("𝓑")) {
            //se não for obrigado a comer
            if (listaDasPecasObrigadoComer.isEmpty()) {
                while (condicional != 1) {
                    //limita o movimento apenas 1 casa para diagonal de cada lado
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
                //se ele for obrigado a comer e não for uma dama
                while (condicional != 1) {
                    //limita o movimento apenas 2 casa para diagonal de cada lado(como vai comer ele vai duas casa para diagonal)
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

        //caso for a ultima peça que terá que ser comida, muda a vez do jogador
        //caso for a ultima peça que terá que ser comida, valida se a peça está na linha alvo para se tornar uma dama
        if (listaValidaComePeca.size() <= 1 && listaPecasComidasPelaDama.size() < 1) {
            if (VEZ_JOGADOR.equals("P") && novaLinha == 1) {
                matriz[novaLinha][novaColuna] = "𝓟";
            } else if (VEZ_JOGADOR.equals("B") && novaLinha == 8) {
                matriz[novaLinha][novaColuna] = "𝓑";
            }
            VEZ_JOGADOR = setaVezProximoJogador(peca);
            //caso for a ultima peça que terá que ser comida e se a dama poder comer mais peças do que o normal,
            //é validado se a ultima peça jogada foi uma peça comum, caso sim ela é promovida se estiver na linha de promoção
        } else if (listaValidaComePeca.size() <= 1 && listaDeValidaDamaComePeca.size() >= 1 && !ultimaPecaUtilizadoParaComer.equals(atualDama)) {
            if (ultimaPecaUtilizadoParaComer.equals("P")) {
                if (VEZ_JOGADOR.equals("P") && novaLinha == 1) {
                    matriz[novaLinha][novaColuna] = "𝓟";
                }
            } else if (ultimaPecaUtilizadoParaComer.equals("B")) {
                if (VEZ_JOGADOR.equals("B") && novaLinha == 8) {
                    matriz[novaLinha][novaColuna] = "𝓑";
                }
            }
            VEZ_JOGADOR = setaVezProximoJogador(peca);
        } else if (listaValidaComePeca.size() <= 1 && listaDeValidaDamaComePeca.size() <= 1) {
            if (ultimaPecaUtilizadoParaComer.equals("P")) {
                if (VEZ_JOGADOR.equals("P") && novaLinha == 1) {
                    matriz[novaLinha][novaColuna] = "𝓟";
                }
            } else if (ultimaPecaUtilizadoParaComer.equals("B")) {
                if (VEZ_JOGADOR.equals("B") && novaLinha == 8) {
                    matriz[novaLinha][novaColuna] = "𝓑";
                }
            }
            VEZ_JOGADOR = setaVezProximoJogador(peca);
        }

        imprimirTabuleiro();
        //valida se cada jogador moveu uma dama nas ultimas jogadas, 10 vezes de cada lado, se sim o jogo acaba empatado
        if (contadorMovimentoDamaSemComerNada >= 20) {
            System.out.println("O JOGO FOI EMPATADO POR MUITO TEMPO SEM AÇÕES PROGRESSISTAS");
            Main.vencedor = true;
        }

    }

    //roda pelo tabuleiro completo validando se tem alguma peça travada daquela vez do jogador
    private LinkedHashSet<String> validaPecaTravada() {
        LinkedHashSet<String> possiveisJogadas = new LinkedHashSet<>();

        for (int linha = 1; linha < tamanho; linha++) {
            for (int coluna = 1; coluna < tamanho; coluna++) {
                if (VEZ_JOGADOR.equals("P")) {
                    if (linha > 1 && coluna > 1 && coluna < 8) {
                        if (Objects.equals(matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(matriz[linha - 1][coluna - 1], "*") && !Objects.equals(matriz[linha - 1][coluna + 1], "*")) {
                            possiveisJogadas.add(String.valueOf(linha).concat(String.valueOf(matriz[0][coluna])));
                        }
                    } else if (linha > 1 && coluna == 1) {
                        if (Objects.equals(matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(matriz[linha - 1][coluna + 1], "*")) {
                            possiveisJogadas.add(String.valueOf(linha).concat(String.valueOf(matriz[0][coluna])));
                        }
                    } else if (linha > 1 && coluna == 8) {
                        if (Objects.equals(matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(matriz[linha - 1][coluna - 1], "*")) {
                            possiveisJogadas.add(String.valueOf(linha).concat(String.valueOf(matriz[0][coluna])));
                        }
                    }
                } else {
                    if (linha < 8 && coluna < 8 && coluna > 1) {
                        if (Objects.equals(matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(matriz[linha + 1][coluna + 1], "*") && !Objects.equals(matriz[linha + 1][coluna - 1], "*")) {
                            possiveisJogadas.add(String.valueOf(linha).concat(String.valueOf(matriz[0][coluna])));
                        }
                    } else if (linha < 8 && coluna == 1) {
                        if (Objects.equals(matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(matriz[linha + 1][coluna + 1], "*")) {
                            possiveisJogadas.add(String.valueOf(linha).concat(String.valueOf(matriz[0][coluna])));
                        }
                    } else if (linha < 8 && coluna == 8) {
                        if (Objects.equals(matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(matriz[linha + 1][coluna - 1], "*")) {
                            possiveisJogadas.add(String.valueOf(linha).concat(String.valueOf(matriz[0][coluna])));
                        }
                    }
                }
            }
        }
        return possiveisJogadas;
    }

    //valida as jogadas disponiveis da peça escolhida(movimento normal, sem comer)
    private List<String> validaJogadasDisponiveisDaPeca(int linha, int coluna) {
        List<String> possiveisJogadas = new ArrayList<>();

        if (VEZ_JOGADOR.equals("P")) {
            if (linha > 1 && coluna > 1) {
                if (Objects.equals(matriz[linha - 1][coluna - 1], "*")) {
                    possiveisJogadas.add(String.valueOf(linha - 1).concat(String.valueOf(matriz[0][coluna - 1])));
                }
            }
            if (linha > 1 && coluna < 8) {
                if (Objects.equals(matriz[linha - 1][coluna + 1], "*")) {
                    possiveisJogadas.add(String.valueOf(linha - 1).concat(String.valueOf(matriz[0][coluna + 1])));
                }
            }
        } else {
            if (linha < 8 && coluna < 8) {
                if (Objects.equals(matriz[linha + 1][coluna + 1], "*")) {
                    possiveisJogadas.add(String.valueOf(linha + 1).concat(String.valueOf(matriz[0][coluna + 1])));
                }
            }
            if (linha < 8 && coluna > 1) {
                if (Objects.equals(matriz[linha + 1][coluna - 1], "*")) {
                    possiveisJogadas.add(String.valueOf(linha + 1).concat(String.valueOf(matriz[0][coluna - 1])));
                }
            }
        }

        possiveisJogadas.forEach(a -> System.out.print(a + ", "));
        System.out.println();
        return possiveisJogadas;
    }

    //roda pelo tabuleiro completo validando se tem alguma peça que é obrigado a comer na vez daquele jogador
    private LinkedList<String> validaComePeca() {
        LinkedList<String> listaDePosicaoParaComer = new LinkedList<>();

        String dama = "";
        if (VEZ_JOGADOR.equals("P")) {
            dama = "𝓟";
        } else if (VEZ_JOGADOR.equals("B")) {
            dama = "𝓑";
        }

        for (int linha = 1; linha < tamanho; linha++) {
            for (int coluna = 1; coluna < tamanho; coluna++) {
                if (linha < 7 && coluna < 7) {
                    if (Objects.equals(matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(matriz[linha + 1][coluna + 1], VEZ_JOGADOR) && !Objects.equals(matriz[linha + 1][coluna + 1], dama) && !Objects.equals(matriz[linha + 1][coluna + 1], "*")) {
                        if (Objects.equals(matriz[linha + 2][coluna + 2], "*")) {
                            listaPecasComidas.add(String.valueOf(linha + 1).concat(matriz[0][coluna + 1]));
                            listaDasPecasObrigadoComer.add(String.valueOf(linha).concat(matriz[0][coluna]));
                            listaDePosicaoParaComer.add(String.valueOf(linha + 2).concat(matriz[0][coluna + 2]));
                        }
                    }
                }
                if (linha < 7 && coluna > 2) {
                    if (Objects.equals(matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(matriz[linha + 1][coluna - 1], VEZ_JOGADOR) && !Objects.equals(matriz[linha + 1][coluna - 1], dama) && !Objects.equals(matriz[linha + 1][coluna - 1], "*")) {
                        if (Objects.equals(matriz[linha + 2][coluna - 2], "*")) {
                            listaPecasComidas.add(String.valueOf(linha + 1).concat(matriz[0][coluna - 1]));
                            listaDasPecasObrigadoComer.add(String.valueOf(linha).concat(matriz[0][coluna]));
                            listaDePosicaoParaComer.add(String.valueOf(linha + 2).concat(matriz[0][coluna - 2]));
                        }

                    }
                }
                if (linha > 2 && coluna > 2) {
                    if (Objects.equals(matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(matriz[linha - 1][coluna - 1], VEZ_JOGADOR) && !Objects.equals(matriz[linha - 1][coluna - 1], dama) && !Objects.equals(matriz[linha - 1][coluna - 1], "*")) {
                        if (Objects.equals(matriz[linha - 2][coluna - 2], "*")) {
                            listaPecasComidas.add(String.valueOf(linha - 1).concat(matriz[0][coluna - 1]));
                            listaDasPecasObrigadoComer.add(String.valueOf(linha).concat(matriz[0][coluna]));
                            listaDePosicaoParaComer.add(String.valueOf(linha - 2).concat(matriz[0][coluna - 2]));
                        }

                    }
                }
                if (linha > 2 && coluna < 7) {
                    if (Objects.equals(matriz[linha][coluna], VEZ_JOGADOR) && !Objects.equals(matriz[linha - 1][coluna + 1], VEZ_JOGADOR) && !Objects.equals(matriz[linha - 1][coluna + 1], dama) && !Objects.equals(matriz[linha - 1][coluna + 1], "*")) {
                        if (Objects.equals(matriz[linha - 2][coluna + 2], "*")) {
                            listaPecasComidas.add(String.valueOf(linha - 1).concat(matriz[0][coluna + 1]));
                            listaDasPecasObrigadoComer.add(String.valueOf(linha).concat(matriz[0][coluna]));
                            listaDePosicaoParaComer.add(String.valueOf(linha - 2).concat(matriz[0][coluna + 2]));
                        }
                    }
                }
            }
        }
        //começa o ciclo para descobrir o caminho com mais peças a serem comidas
        if (!listaDePosicaoParaComer.isEmpty()) {
            //salva na lista auxiliar as possibilidades de caminhos possiveis até agora
            LinkedList<String> listaAux = new LinkedList<>(listaDePosicaoParaComer);
            LinkedList<String> listaDePosicaoParaComerAux = new LinkedList<>();
            //salva na lista auxiliar as peças que são obrigadas a comer
            LinkedList<String> listaDePecaObrigadoAComerAux = new LinkedList<>(listaDasPecasObrigadoComer);
            LinkedList<String> listaDePecaObrigadoAComerAuxSP = new LinkedList<>();
            //salva na lista auxiliar as possibilidades de peças que irão ser comidas
            LinkedList<String> listaPecasComidasAux = new LinkedList<>(listaPecasComidas);
            LinkedList<String> listaPecasComidasAuxSP = new LinkedList<>();
            int tamanhoLista = 0;

            //roda a lista auxiliar para ver os caminhos possiveis
            for (int i = 0; i < listaAux.size(); i++) {
                //zera a lista de posição e adiciona apenas o caminho que ele irá validar
                listaDePosicaoParaComer.clear();
                listaDePosicaoParaComer.add(listaAux.get(i));
                //zera a lista de peças obrigadas a comer
                listaDasPecasObrigadoComer.clear();
                //zera a lista de peças que serão comidas e adiciona a peça que ele comerá seguindo aquele caminho
                listaPecasComidasAuxSP.clear();
                listaPecasComidas.clear();
                listaPecasComidas.add(listaPecasComidasAux.get(i));

                //enquanto a lista de peças obrigadas a comer for maior que I, ele consegue atribuir o item da lista de peças obrigadas a comer
                //dentro da lista final de peças obrigadas a comer(validando o maior caminho, se é da primeira peça obrigada a comer ou da segunda)
                if (listaDePecaObrigadoAComerAux.size() > i) {
                    listaDasPecasObrigadoComer.add(listaDePecaObrigadoAComerAux.get(i));
                }
                //chama a recursão
                validaComePeca(listaAux.get(i), listaDePosicaoParaComer);
                //valida se o caminho percorrido é maior do que o anterior
                if (listaDePosicaoParaComer.size() > tamanhoLista) {
                    //caso for maior ele seta o tamanho da lista com o tamanho do caminho que foi passado
                    listaDePecaObrigadoAComerAuxSP.clear();
                    tamanhoLista = listaDePosicaoParaComer.size();
                    //seta as peças que serão comidas nesse caminho percorrido
                    listaDePosicaoParaComerAux = listaDePosicaoParaComer;
                    listaPecasComidasAuxSP = listaPecasComidas;
                    //seta a peça que tem o maior caminho
                    listaDePecaObrigadoAComerAuxSP.addAll(listaDasPecasObrigadoComer);
                    //caso ambas as peças que são obrigadas a comer possam comer a mesma quantidade de peça, o jogador pode selecionar qual ele irá utilizar
                }else if(listaDePosicaoParaComer.size() == tamanhoLista){
                    listaDePecaObrigadoAComerAuxSP.addAll(listaDasPecasObrigadoComer);
                }
            }
            //apenas setas os valores finais após a recursão nas listas que serão utilizadas nos métodos principais
            listaPecasComidas = listaPecasComidasAuxSP;
            listaDasPecasObrigadoComer.clear();
            listaDasPecasObrigadoComer.addAll(listaDePecaObrigadoAComerAuxSP);
            listaDePosicaoParaComer = listaDePosicaoParaComerAux;

        }
        return listaDePosicaoParaComer;
    }

    //começa a recursão para validar o caminho com mais peças a comer
    private void validaComePeca(String coordenada, LinkedList<String> listaDePosicaoParaComer) {
        int linha = getLinha(coordenada);
        int coluna = getColuna(coordenada);

        String dama = "";
        if (VEZ_JOGADOR.equals("P")) {
            dama = "𝓟";
        } else if (VEZ_JOGADOR.equals("B")) {
            dama = "𝓑";
        }

        if (linha < 7 && coluna < 7) {

            if (!Objects.equals(matriz[linha + 1][coluna + 1], VEZ_JOGADOR) && !Objects.equals(matriz[linha + 1][coluna + 1], dama) && !Objects.equals(matriz[linha + 1][coluna + 1], "*")) {
                if (Objects.equals(matriz[linha + 2][coluna + 2], "*")) {
                    if (!listaPecasComidas.contains(String.valueOf(linha + 1).concat(matriz[0][coluna + 1]))) {
                        listaPecasComidas.add(String.valueOf(linha + 1).concat(matriz[0][coluna + 1]));

                        if (listaDePosicaoParaComer.size() > 0) {
                            if (!listaDePosicaoParaComer.get(listaDePosicaoParaComer.size() - 1).equals(coordenada)) {
                                listaDePosicaoParaComer.add(coordenada);
                            }
                        }

                        listaDePosicaoParaComer.add(String.valueOf(linha + 2).concat(matriz[0][coluna + 2]));
                        validaComePeca(String.valueOf(linha + 2).concat(matriz[0][coluna + 2]), listaDePosicaoParaComer);
                    }

                }
            }
        }
        if (linha < 7 && coluna > 2) {
            if (!Objects.equals(matriz[linha + 1][coluna - 1], VEZ_JOGADOR) && !Objects.equals(matriz[linha + 1][coluna - 1], dama) && !Objects.equals(matriz[linha + 1][coluna - 1], "*")) {
                if (Objects.equals(matriz[linha + 2][coluna - 2], "*")) {
                    if (!listaPecasComidas.contains(String.valueOf(linha + 1).concat(matriz[0][coluna - 1]))) {
                        listaPecasComidas.add(String.valueOf(linha + 1).concat(matriz[0][coluna - 1]));

                        if (listaDePosicaoParaComer.size() > 0) {
                            if (!listaDePosicaoParaComer.get(listaDePosicaoParaComer.size() - 1).equals(coordenada)) {
                                listaDePosicaoParaComer.add(coordenada);
                            }
                        }

                        listaDePosicaoParaComer.add(String.valueOf(linha + 2).concat(matriz[0][coluna - 2]));
                        validaComePeca(String.valueOf(linha + 2).concat(matriz[0][coluna - 2]), listaDePosicaoParaComer);
                    }

                }
            }
        }
        if (linha > 2 && coluna > 2) {
            if (!Objects.equals(matriz[linha - 1][coluna - 1], VEZ_JOGADOR) && !Objects.equals(matriz[linha - 1][coluna - 1], dama) && !Objects.equals(matriz[linha - 1][coluna - 1], "*")) {
                if (Objects.equals(matriz[linha - 2][coluna - 2], "*")) {
                    if (!listaPecasComidas.contains(String.valueOf(linha - 1).concat(matriz[0][coluna - 1]))) {
                        listaPecasComidas.add(String.valueOf(linha - 1).concat(matriz[0][coluna - 1]));

                        if (listaDePosicaoParaComer.size() > 0) {
                            if (!listaDePosicaoParaComer.get(listaDePosicaoParaComer.size() - 1).equals(coordenada)) {
                                listaDePosicaoParaComer.add(coordenada);
                            }
                        }

                        listaDePosicaoParaComer.add(String.valueOf(linha - 2).concat(matriz[0][coluna - 2]));
                        validaComePeca(String.valueOf(linha - 2).concat(matriz[0][coluna - 2]), listaDePosicaoParaComer);
                    }

                }
            }
        }
        if (linha > 2 && coluna < 7) {
            if (!Objects.equals(matriz[linha - 1][coluna + 1], VEZ_JOGADOR) && !Objects.equals(matriz[linha - 1][coluna + 1], dama) && !Objects.equals(matriz[linha - 1][coluna + 1], "*")) {
                if (Objects.equals(matriz[linha - 2][coluna + 2], "*")) {
                    if (!listaPecasComidas.contains(String.valueOf(linha - 1).concat(matriz[0][coluna + 1]))) {
                        listaPecasComidas.add(String.valueOf(linha - 1).concat(matriz[0][coluna + 1]));

                        if (listaDePosicaoParaComer.size() > 0) {
                            if (!listaDePosicaoParaComer.get(listaDePosicaoParaComer.size() - 1).equals(coordenada)) {
                                listaDePosicaoParaComer.add(coordenada);
                            }
                        }

                        listaDePosicaoParaComer.add(String.valueOf(linha - 2).concat(matriz[0][coluna + 2]));
                        validaComePeca(String.valueOf(linha - 2).concat(matriz[0][coluna + 2]), listaDePosicaoParaComer);
                    }

                }
            }
        }
    }

    //valida as jogadas disponíveis da dama escolhida(movimento normal, sem comer)
    private LinkedHashSet<String> validaJogadasDisponiveisDaDama(int linha, int coluna) {
        LinkedHashSet<String> possiveisJogadas = new LinkedHashSet<>();

        if (linha <= 8 && coluna <= 8) {
            int l = linha;
            int c = coluna;
            while (l <= 8 && c <= 8) {
                if (Objects.equals(matriz[l][c], "*")) {
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
                if (Objects.equals(matriz[l][c], "*")) {
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
                if (Objects.equals(matriz[l][c], "*")) {
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
                if (Objects.equals(matriz[l][c], "*")) {
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

    //roda pelo tabuleiro completo validando se tem alguma dama que é obrigado a comer daquele jogador
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

        //começa o ciclo para descobrir o caminho com mais peças a serem comidas
        if (!listaDePosicaoParaComer.isEmpty()) {
            //salva na lista auxiliar as possibilidades de caminhos possiveis até agora
            LinkedList<String> listaAux = new LinkedList<>(listaDePosicaoParaComer);
            LinkedList<String> listaDePosicaoParaComerAux = new LinkedList<>();
            //salva na lista auxiliar as damas que são obrigadas a comer
            LinkedList<String> listaDePecaObrigadoAComerAux = new LinkedList<>(listaDasDamasObrigadoComer);
            LinkedList<String> listaDePecaObrigadoAComerAuxSP = new LinkedList<>();
            //salva na lista auxiliar as peças que serão comidas
            LinkedList<String> listaPecasComidasAux = new LinkedList<>(listaPecasComidasPelaDama);
            LinkedList<String> listaPecasComidasAuxSP = new LinkedList<>();
            int tamanhoLista = 0;

            for (int i = 0; i < listaAux.size(); i++) {
                //zera a lista de posição e adiciona apenas o caminho que ele irá validar
                listaDePosicaoParaComer.clear();
                listaDePosicaoParaComer.add(listaAux.get(i));
                //zera a lista de peças obrigadas a comer
                listaDasDamasObrigadoComer.clear();
                //zera a lista de peças que serão comidas e adiciona a peça que ele comerá seguindo aquele caminho
                listaPecasComidasAuxSP.clear();
                listaPecasComidasPelaDama.clear();
                listaPecasComidasPelaDama.add(listaPecasComidasAux.get(i));

                //enquanto a lista de damas obrigadas a comer for maior que I, ele consegue atribuir o item da lista de peças obrigadas a comer
                //dentro da lista final de peças obrigadas a comer(validando o maior caminho, se é da primeira peça obrigada a comer ou da segunda)
                if (listaDePecaObrigadoAComerAux.size() > i) {
                    listaDasDamasObrigadoComer.add(listaDePecaObrigadoAComerAux.get(i));
                }

                validaComePecaDama(listaAux.get(i), listaDePosicaoParaComer);
                if (listaDePosicaoParaComer.size() > tamanhoLista) {
                    //caso for maior ele seta o tamanho da lista com o tamanho do caminho que foi passado
                    listaDePecaObrigadoAComerAuxSP.clear();
                    tamanhoLista = listaDePosicaoParaComer.size();
                    //seta as peças que serão comidas nesse caminho percorrido
                    listaDePosicaoParaComerAux = listaDePosicaoParaComer;
                    listaPecasComidasAuxSP = listaPecasComidasPelaDama;
                    //seta a peça que tem o maior caminho
                    listaDePecaObrigadoAComerAuxSP.addAll(listaDasDamasObrigadoComer);
                }
            }
            //apenas setas os valores finais após a recursão nas listas que serão utilizadas nos métodos principais
            listaPecasComidasPelaDama = listaPecasComidasAuxSP;
            listaDasDamasObrigadoComer.clear();
            listaDasDamasObrigadoComer.addAll(listaDePecaObrigadoAComerAuxSP);
            listaDePosicaoParaComer = listaDePosicaoParaComerAux;
        }
        return listaDePosicaoParaComer;
    }

    //começa a recursão para validar o caminho com mais peças a comer
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

    //reescreve a lista de posições que a dama pode ir caso for a ultima peça a ser comida por ela
    private LinkedList<String> damaComeUltimaPeca(String coordenada, String coordenadaDaPecaComida) {
        int linha = getLinha(coordenada);
        int coluna = getColuna(coordenada);
        int linhaPecaComida = getLinha(coordenadaDaPecaComida);
        int colunaPecaComida = getColuna(coordenadaDaPecaComida);
        String dama = "";

        LinkedList<String> listaDePosicaoParaComer = new LinkedList<>();

        if (VEZ_JOGADOR.equals("P")) {
            dama = "𝓟";
        } else if (VEZ_JOGADOR.equals("B")) {
            dama = "𝓑";
        }

        int l = linha;
        int c = coluna;
        while (l < 8 && c < 8) {
            if (l == linhaPecaComida && c == colunaPecaComida) {
                int laux = l;
                int caux = c;
                while (laux < 9 && caux < 9) {
                    if (Objects.equals(matriz[laux][caux], VEZ_JOGADOR) || Objects.equals(matriz[laux][caux], dama)) {
                        break;
                    }
                    if (Objects.equals(matriz[laux][caux], "*")) {
                        listaDePosicaoParaComer.add(String.valueOf(laux).concat(String.valueOf(matriz[0][caux])));
                    }
                    laux++;
                    caux++;
                }
            }
            l++;
            c++;
        }
        l = linha;
        c = coluna;
        while (l < 8 && c > 1) {
            if (l == linhaPecaComida && c == colunaPecaComida) {
                int laux = l;
                int caux = c;
                while (laux < 9 && caux > 0) {
                    if (Objects.equals(matriz[laux][caux], VEZ_JOGADOR) || Objects.equals(matriz[laux][caux], dama)) {
                        break;
                    }
                    if (Objects.equals(matriz[laux][caux], "*")) {
                        listaDePosicaoParaComer.add(String.valueOf(laux).concat(String.valueOf(matriz[0][caux])));
                    }
                    laux++;
                    caux--;
                }
            }
            l++;
            c--;
        }
        l = linha;
        c = coluna;
        while (l > 1 && c > 1) {
            if (l == linhaPecaComida && c == colunaPecaComida) {
                int laux = l;
                int caux = c;
                while (laux > 0 && caux > 0) {
                    if (Objects.equals(matriz[laux][caux], VEZ_JOGADOR) || Objects.equals(matriz[laux][caux], dama)) {
                        break;
                    }
                    if (Objects.equals(matriz[laux][caux], "*")) {
                        listaDePosicaoParaComer.add(String.valueOf(laux).concat(String.valueOf(matriz[0][caux])));
                    }
                    laux--;
                    caux--;
                }
            }
            l--;
            c--;
        }
        l = linha;
        c = coluna;
        while (l > 1 && c < 8) {
            if (l == linhaPecaComida && c == colunaPecaComida) {
                int laux = l;
                int caux = c;
                while (laux > 0 && caux < 9) {
                    if (Objects.equals(matriz[laux][caux], VEZ_JOGADOR) || Objects.equals(matriz[laux][caux], dama)) {
                        break;
                    }
                    if (Objects.equals(matriz[laux][caux], "*")) {
                        listaDePosicaoParaComer.add(String.valueOf(laux).concat(String.valueOf(matriz[0][caux])));
                    }
                    laux--;
                    caux++;
                }
            }
            l--;
            c++;
        }
        return listaDePosicaoParaComer;
    }


    private int getLinha(String coordenada) {
        return Integer.parseInt(String.valueOf(coordenada.charAt(0)));
    }

    private int getColuna(String coordenada) {
        return Character.getNumericValue(coordenada.charAt(1)) - 9;
    }
}

