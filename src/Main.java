public class Main {
    public static void main(String[] args) {
        Tabuleiro tab = new Tabuleiro(8);
        tab.imprimirTabuleiro();
        tab.moverPeca(tab.pegaMovimentoPecas(tab.comecarGame()));
        while(true){
            tab.moverPeca(tab.pegaMovimentoPecas(Tabuleiro.VEZ_JOGADOR));
        }

        // tab.moverPeca(tab.pegaMovimentoPecas());

        //tab.moverPeca("7A,6B");

    }
}
