import java.util.Scanner;
import java.util.Random;

public class Pexeso {
    private static final int BOARD_SIZE = 4;
    private static final char HIDDEN = '*';
    private static char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
    private static boolean[][] revealed = new boolean[BOARD_SIZE][BOARD_SIZE];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean playAgain;

        do {
            initializeBoard();
            shuffleBoard();
            
            System.out.println("Pexeso!");
            System.out.println("Najdi všechny společné páry, aby jsi vyhrál.");

            while (!gameWon()) {
                printBoard();
                int[] firstPick = getUserInput(scanner, "První karta (řádek a sloupec): ");
                int[] secondPick = getUserInput(scanner, "Druhá karta (řádek a sloupec): ");
                
                if (isMatch(firstPick, secondPick)) {   //metodou isMatch porovná první a druhý input pokud se rovnají je to match
                    System.out.println("Nalezli jste pár!");
                    revealed[firstPick[0]][firstPick[1]] = true;
                    revealed[secondPick[0]][secondPick[1]] = true;
                } else {
                    System.out.println("To není správný pár.");
                    pause(1000);
                }
            }

            System.out.println("Našel jsi všechny páry!");

            playAgain = askPlayAgain(scanner);
        } while (playAgain);

        scanner.close();
    }

    private static void initializeBoard() {             //inicializuje board (nepromíchaný)
        char[] cards = {'A', 'A', 'B', 'B', 'C', 'C', 'D', 'D', 
                        'E', 'E', 'F', 'F', 'G', 'G', 'H', 'H'};
        int index = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = cards[index++];
                revealed[i][j] = false;
            }
        }
    }

    private static void shuffleBoard() {         // promíchá boardu pomocí Java.Random
        Random rand = new Random();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                int x = rand.nextInt(BOARD_SIZE);
                int y = rand.nextInt(BOARD_SIZE);
                char temp = board[i][j];
                board[i][j] = board[x][y];
                board[x][y] = temp;
            }
        }
    }

    private static void printBoard() {    // vyprintí nevyřešenou boardu
        System.out.println("   0 1 2 3");
        System.out.println("  --------");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print(i + "| ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (revealed[i][j]) {
                    System.out.print(board[i][j] + " ");
                } else {
                    System.out.print(HIDDEN + " ");
                }
            }
            System.out.println();
        }
    }

    private static int[] getUserInput(Scanner scanner, String prompt) {   //user input who would have guess
        int row, col;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            String[] parts = input.split(";");   // rozdělí input  středníkem
            if (parts.length == 2) {
                try {
                    row = Integer.parseInt(parts[0]); //reodělené části se přiřadí k row a col
                    col = Integer.parseInt(parts[1]);
                    if (row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE && !revealed[row][col]) {   //správný formát inputu
                        return new int[]{row, col};
                    } else {
                        System.out.println("Buď jsi špatně zadal souřadnice, nebo už je karta odhalena."); //buť číslo moc velký nebo už je karta done
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Neplatný formát. Použij 'řádek;sloupec'.");   //pokud se chce třeba převést písmeno (špatně převedená variable)
                }
            } else {
                System.out.println("Neplatný formát. Použij 'řádek;sloupec'."); // doslova všecky ostatní exceptions :D
            }
        }
    }

    private static boolean isMatch(int[] firstPick, int[] secondPick) { //ověření spávný kombinace 
        int row1 = firstPick[0], col1 = firstPick[1];            //ziskani souřadnice 1
        int row2 = secondPick[0], col2 = secondPick[1];     //ziskani souřadnice 2
        return board[row1][col1] == board[row2][col2];      //pokud jsou stejný true 
    }

    private static boolean gameWon() {   //výtěztví vrací 1/0
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) { //tyhle cykly procházejí herní desku a kontrolují schodu se revealed 
                if (!revealed[i][j]) {             //deskou pokud je neshoda hraje se dál pokud není máš vyhráno
                    return false;
                }
            }
        }
        return true;
    }

    private static void pause(int milliseconds) {  // pozastaví hru na určitou dobu využívam po špatném guessu
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static boolean askPlayAgain(Scanner scanner) {    //zda chce hrát znova pokud ano hodí restart 
        System.out.print("Chcete hrát znovu? (ano/ne): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("ano");
    }
}
