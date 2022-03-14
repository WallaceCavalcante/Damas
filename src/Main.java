public class Main {
    public static void main(String[] args) {
        Tabuleiro tab = new Tabuleiro(8);

        tab.imprimirTabuleiro();
        tab.moverPeca(tab.pegaMovimentoPecas(tab.comecarGame()));
        tab.moverPeca(tab.pegaMovimentoPecas(tab.VEZ_JOGADOR));

        // tab.moverPeca(tab.pegaMovimentoPecas());

        //tab.moverPeca("7A,6B");

    }
}
