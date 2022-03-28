public class Main {
    public static boolean vencedor = false;

    public static void main(String[] args) {
        Tabuleiro tabuleiro = new Tabuleiro(8);
        tabuleiro.imprimirTabuleiro();
        //pergunta quem irá começar o jogo P ou B
        tabuleiro.moverPeca(tabuleiro.pegaMovimentoPecas(tabuleiro.comecarGame()));
        //enquanto não tiver um vencador o jogo continua
        while(!vencedor){
            int b = 0;
            int p = 0;
            tabuleiro.moverPeca(tabuleiro.pegaMovimentoPecas(Tabuleiro.VEZ_JOGADOR));
            //valida se ainda tem peças de P ou B na mesa
            for (int linha = 1; linha < 9; linha++) {
                for (int coluna = 1; coluna < 9; coluna++) {
                    if(tabuleiro.matriz[linha][coluna].equals("P") || tabuleiro.matriz[linha][coluna].equals("𝓟")){
                        p++;
                    }else if(tabuleiro.matriz[linha][coluna].equals("B") || tabuleiro.matriz[linha][coluna].equals("𝓑")){
                        b++;
                    }
                }
            }
            //caso não tiver P, as peças brancas ganham
            if(p == 0){
                System.out.println("As peças brancas ganharam");
                vencedor = true;
            }
            //caso não tiver B, as peças pretas ganham
            else if(b == 0){
                System.out.println("As peças pretas ganharam");
                vencedor = true;
            }
        }
        System.out.println("Muito obrigado por jogar o nosso jogo!");
        System.out.println();
    }
}
