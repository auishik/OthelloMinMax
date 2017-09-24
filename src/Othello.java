import java.util.Scanner;

public class Othello {
    public State state;

    Othello() {
        state = init_state();
    }

    public State init_state() {
        int[][] matrix = new int[8][8];
        matrix[3][3] = State.MIN_DISC;
        matrix[4][4] = State.MIN_DISC;
        matrix[3][4] = State.MAX_DISC;
        matrix[4][3] = State.MAX_DISC;

        int[][] neighbour = new int[8][8];
        neighbour[3][3] = 3;
        neighbour[4][4] = 3;
        neighbour[3][4] = 3;
        neighbour[4][3] = 3;

        return new State(matrix, neighbour, State.MAX_DISC, 0, 7);
    }

    public void print_matrix(State state) {
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(((state.matrix[i][j] == -1) ? 2 : state.matrix[i][j]) + "  ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Othello othello = new Othello();
        Scanner scanner = new Scanner(System.in);
        int starter = scanner.nextInt();

        if(starter == State.MIN_DISC) {
            System.out.println("Give opponent move coordinates : ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            Move move = new Move(othello.state, row, col, State.MIN_DISC);
            if(!move.is_legit()) {
                System.out.println("Opponent move not valid");
                return;
            }
            othello.state = othello.state.result(move);
        }

        while(true) {
            System.out.println("Computing move ... ");
            MinMax minMax = new MinMax(othello.state, 6);
            if(minMax.next_move == null) {
                System.out.println("No move for max");
                othello.print_matrix(othello.state);
                othello.state = minMax.next_state;
            } else {
                othello.state = minMax.next_state;
                System.out.println("Max move " + minMax.next_move.row + " " + minMax.next_move.col);
                othello.print_matrix(othello.state);
            }

            System.out.println("Give opponent move coordinates : ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            if(row == -1) {
                System.out.println("Opponent passes");
            } else {
                Move move = new Move(othello.state, row, col, State.MIN_DISC);
                if(!move.is_legit()) {
                    System.out.println("Opponent move not valid");
                    othello.print_matrix(othello.state);
                    return;
                }
                othello.state = othello.state.result(move);
                othello.print_matrix(othello.state);
            }

            minMax = null;
        }
    }
}
