package BlackJack;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class BlackJack {

    static int input_int_pos(String frase) {
        Scanner in = new Scanner(System.in);
        boolean isInt = false;
        int n = 0;
        while (!isInt) {
            try {
                isInt = true;
                System.out.println(frase);
                n = in.nextInt();
            } catch (InputMismatchException ex) {
                in.nextLine();
                isInt = false;
            }
        }
        if (n < 0) {
            n = -n;
        }
        return n;
    }

    static int input_int_neg(String frase) {
        Scanner in = new Scanner(System.in);
        boolean isInt = false;
        int n = 0;
        while (!isInt) {
            try {
                isInt = true;
                System.out.println(frase);
                n = in.nextInt();
            } catch (InputMismatchException ex) {
                in.nextLine();
                isInt = false;
            }
        }
        if (n > 0) {
            n = -n;
        }
        return n;
    }

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        Random r = new Random();
        int giocatori, punteggio;
        int[] redditi;
        int[] scommesse;
        int[] asso;
        int[] punteggi;
        boolean[] sballi;
        boolean[] blackjack;
        String[] nomi;
        String[] mani;
        String[] mazzo;
        String[] semi;

        // Colori per il testo
        String rosso, verde, reset;
        reset = "\u001B[0m";
        rosso = "\u001B[31m";
        verde = "\u001B[32m";

        // Genero carte
        mazzo = new String[13];
        mazzo[0] = "A";
        mazzo[1] = "2";
        mazzo[2] = "3";
        mazzo[3] = "4";
        mazzo[4] = "5";
        mazzo[5] = "6";
        mazzo[6] = "7";
        mazzo[7] = "8";
        mazzo[8] = "9";
        mazzo[9] = "10";
        mazzo[10] = "J";
        mazzo[11] = "Q";
        mazzo[12] = "K";

        // Genero i semi
        semi = new String[4];
        semi[0] = "♠";
        semi[1] = "♣";
        semi[2] = rosso + "♦" + reset;
        semi[3] = rosso + "♥" + reset;

        // Giocatori e statistiche
        giocatori = input_int_pos("Inserisci il numero dei giocatori");
        nomi = new String[giocatori];
        redditi = new int[giocatori];
        scommesse = new int[giocatori];
        mani = new String[(giocatori + 1) * 21];

        for (int i = 0; i < giocatori; i++) {
            System.out.println("Inserisci il nome del giocatore: " + (i + 1));
            nomi[i] = in.nextLine();
            redditi[i] = input_int_pos("Inserisci il reddito di " + verde + nomi[i] + reset);
            scommesse[i] = input_int_pos("Inserisci la scommessa di " + verde + nomi[i] + reset);
            while (scommesse[i] > redditi[i]) {
                System.out.println(rosso + "Scomessa invalida!" + reset);
                scommesse[i] = input_int_pos("Inserisci la scommessa di " + verde + nomi[i] + reset);
            }
        }

//        do {
        // Carte Giocatori
        for (int i = 0; i < giocatori; i++) {
            mani[i * 21] = mazzo[r.nextInt(0, 13)];
            mani[i * 21 + 1] = mazzo[r.nextInt(0, 13)];
        }

        asso = new int[giocatori + 1];
        punteggi = new int[giocatori + 1];

        punteggio = 0;
        String carta;
        mani[giocatori * 21] = mazzo[r.nextInt(0, 13)];
        mani[giocatori * 21 + 1] = mazzo[r.nextInt(0, 13)];

        // Punteggio banco
        for (int i = 0; i < 2; i++) {
            carta = mani[giocatori * 21 + i];
            switch (carta) {
                case "A" ->
                    asso[giocatori]++;
                case "K", "Q", "J" ->
                    punteggio += 10;
                default ->
                    punteggio += Integer.valueOf(carta);
            }
        }
        for (int i = 0; i < asso[giocatori]; i++) {
            if (punteggi[giocatori] + 11 > 21) {
                punteggio++;
            } else {
                punteggio += 11;
            }
        }
        punteggi[giocatori] = punteggio;
        punteggio = 0;

        // Punteggi giocatori
        for (int i = 0; i < giocatori; i++) {
            for (int j = 0; j < 2; j++) {
                carta = mani[i * 21 + j];
                switch (carta) {
                    case "A" ->
                        asso[i]++;
                    case "K", "Q", "J" ->
                        punteggio += 10;
                    default ->
                        punteggio += Integer.valueOf(carta);
                }
            }
            for (int j = 0; j < asso[i]; j++) {
                if (punteggi[i] + 11 > 21) {
                    punteggio++;
                } else {
                    punteggio += 11;
                }
            }
            punteggi[i] = punteggio;
            punteggio = 0;
        }

        // Carte Utente e banco
        for (int i = 0; i < giocatori; i++) {
            System.out.println(verde + nomi[i] + reset + " La tua mano e: " + mani[i * 21] + semi[r.nextInt(0, 4)]
                    + " " + mani[i * 21 + 1] + semi[r.nextInt(0, 4)] + ". Il tuo punteggio e: " + punteggi[i]);
        }
        System.out.println("La prima carta del banco e: " + mani[giocatori * 21] + semi[r.nextInt(0, 4)]);

        String risp = "";
        int numCartePescate;
        sballi = new boolean[giocatori + 1];
        blackjack = new boolean[giocatori];

        // Hit || Stand
        for (int i = 0; i < giocatori; i++) {
            if (punteggi[i] == 21) {
                System.out.println(verde + nomi[i] + "Hai fatto blackjack!" + reset);
                risp = "n";
                blackjack[i] = true;

            }
            numCartePescate = 0;
            carta = "";
            System.out.println(verde + nomi[i] + reset + " " + "Vuoi pescare una carta? (Y/N)");
            risp = in.nextLine();
            while (risp.equals("y") || risp.equals("Y")) {
                punteggio = 0;
                carta = mazzo[r.nextInt(0, 13)];
                mani[i * 21 + 2 + numCartePescate] = carta;
                numCartePescate++;
                asso[i] = 0;
                switch (carta) {
                    case "A" ->
                        asso[i]++;
                    case "K", "Q", "J" ->
                        punteggio += 10;
                    default ->
                        punteggio += Integer.valueOf(carta);
                }
                for (int j = 0; j < asso[i]; j++) {
                    if (punteggi[i] + 11 > 21) {
                        punteggio++;
                    } else {
                        punteggio += 11;
                    }
                }
                punteggi[i] += punteggio;
                System.out.print(verde + nomi[i] + reset + " La tua mano e: ");
                for (int j = 0; j < numCartePescate + 2; j++) {
                    System.out.print(" " + mani[i * 21 + j] + " " + semi[r.nextInt(0, 4)] + " ");
                }
                System.out.println(". Il tuo punteggio e: " + punteggi[i]);
                if (punteggi[i] > 21) {
                    System.out.println(rosso + "Hai sballato " + reset + verde + nomi[i] + reset + rosso + "!" + reset);
                    sballi[i] = true;
                    risp = "n";
                } else {
                    System.out.println("Vuoi pescare un'altra carta? (Y/N)");
                    risp = in.nextLine();
                }
            }
        }
        // Banco
        System.out.println("La mano del banco e: " + mani[giocatori * 21] + semi[r.nextInt(0, 4)] + mani[giocatori * 21 + 1] + semi[r.nextInt(0, 4)]);
        numCartePescate = 0;
        while (punteggi[giocatori] <= 16) {
            System.out.println("Il banco pesca...");
            carta = mazzo[r.nextInt(0, 13)];
            punteggio = 0;
            carta = mazzo[r.nextInt(0, 13)];
            mani[giocatori * 21 + 2 + numCartePescate] = carta;
            numCartePescate++;
            asso[giocatori] = 0;
            switch (carta) {
                case "A" ->
                    asso[giocatori]++;
                case "K", "Q", "J" ->
                    punteggio += 10;
                default ->
                    punteggio += Integer.valueOf(carta);
            }
            for (int j = 0; j < asso[giocatori]; j++) {
                if (punteggi[giocatori] + 11 > 21) {
                    punteggio++;
                } else {
                    punteggio += 11;
                }
            }
            punteggi[giocatori] += punteggio; 
            
            for (int i = 0; i < numCartePescate + 2; i++) {
                System.out.print(mani[giocatori * 21 + i] + " "); 
            }
            System.out.println(" ");

        }

        // Soldi e vittoria W.I.P
//        for (int i = 0; i < giocatori; i++) {
//            if (blackjack[i]) {
//                System.out.println(verde + nomi[i] + reset + "Ha fatto blackjack guadagna: " + verde + (scommesse[i] + scommesse[i] / 2 + 1));
//                redditi[i] += (scommesse[i] + scommesse[i] / 2);
//            }
//        }
        // } while (giocatori > 0);
    }
}
