import java.util.Scanner;

public class TestRunner {
    public static void print_matrix(State state) {
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(((state.matrix[i][j] == -1) ? 2 : state.matrix[i][j]) + "  ");
            }
            System.out.println();
        }
    }

    public static void compute_neigbhours(State state) {
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(state.matrix[i][j] != State.EMPTY_DISC) {
                    for (int k = -1; k <= 1; k++) {
                        for (int l = -1; l <= 1; l++) {
                            int m = i + k;
                            int n = j + l;
                            if(m < 0 || n < 0 || m > 7 || n > 7 || (k == 0 && l == 0)) {
                                continue;
                            }
                            if(state.matrix[m][n] != State.EMPTY_DISC) {
                                state.neighbour[i][j]++;
                            }
                        }
                    }
                }
            }
        }
    }
    public static void main(String args[]) {
        int[][] matrix = new int[8][8];
        matrix[3][5] = State.MAX_DISC;
        matrix[4][4] = State.MAX_DISC;
        matrix[4][5] = State.MAX_DISC;
        matrix[5][4] = State.MAX_DISC;
        matrix[5][5] = State.MAX_DISC;

        matrix[1][3] = State.MIN_DISC;
        matrix[2][3] = State.MIN_DISC;
        matrix[2][5] = State.MIN_DISC;
        matrix[3][3] = State.MIN_DISC;
        matrix[3][4] = State.MIN_DISC;
        matrix[4][2] = State.MIN_DISC;
        matrix[4][3] = State.MIN_DISC;

        int[][] neighbour = new int[8][8];

        State state = new State(matrix, neighbour, State.MAX_DISC, 0, 100);
        compute_neigbhours(state);
        state.init_successors();
        System.out.println("Test Done");
    }
}
