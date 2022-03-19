public class Main {
    public static void main(String[] args) {
        Tabuleiro tab = new Tabuleiro(8);
        tab.imprimirTabuleiro();
        tab.moverPeca(tab.pegaMovimentoPecas(tab.comecarGame()));
        boolean vencedor = false;
        while(!vencedor){
            int b = 0;
            int p = 0;
            tab.moverPeca(tab.pegaMovimentoPecas(Tabuleiro.VEZ_JOGADOR));
            for (int linha = 1; linha < 9; linha++) {
                for (int coluna = 1; coluna < 9; coluna++) {
                    if(tab.matriz[linha][coluna].equals("P") || tab.matriz[linha][coluna].equals("𝓟")){
                        p++;
                    }else if(tab.matriz[linha][coluna].equals("B") || tab.matriz[linha][coluna].equals("𝓑")){
                        b++;
                    }
                }
            }
            if(p == 0){
                System.out.println("As peças brancas ganharam");
                vencedor = true;
            }else if(b == 0){
                System.out.println("As peças pretas ganharam");
                vencedor = true;
            }
        }
        System.out.println("Muito obrigado por jogar o nosso jogo!");
        System.out.println();
    }
}
