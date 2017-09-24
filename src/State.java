import java.util.ArrayList;

public class State {
    public static final int EMPTY_DISC = 0;
    public static final int MAX_DISC = 1;
    public static final int MIN_DISC = -1;

    public int[][] matrix;
    public int[][] neighbour;
    public int turn;
    public int depth;
    public int depth_limit;
    public ArrayList<State> successors;
    public ArrayList<Move> legit_moves;
    public Evaluation eval;

    State(int[][] matrix, int[][] neighbour, int turn, int depth, int depth_limit) throws java.lang.OutOfMemoryError {
        this.matrix = matrix;
        this.neighbour = neighbour;
        this.turn = turn;
        this.depth = depth;
        this.depth_limit = depth_limit;
    }

    public boolean init_legit_moves() {
        legit_moves = new ArrayList<>();
        Move move;
        for(int i = 0; i < 8; i+=7) {
            for(int j = 0; j < 8; j++) {
                if(matrix[i][j] != -turn && neighbour[i][j] < 5) {
                    for(int k = -1; k <= 1; k++) {
                        for(int l = -1; l <= 1; l++) {
                            int m = i + k;
                            int n = j + l;
                            if(m < 0 || n < 0 || m > 7 || n > 7 || (k == 0 && l == 0)) {
                                continue;
                            }
                            if(matrix[m][n] == EMPTY_DISC) {
                                move = new Move(this, m, n, turn);
                                if(move.is_legit() && !legit_moves.contains(move)) {
                                    legit_moves.add(move);
                                }
                            }
                        }
                    }
                }

                if(matrix[j][i] != -turn && neighbour[j][i] < 5) {
                    for(int k = -1; k <= 1; k++) {
                        for(int l = -1; l <= 1; l++) {
                            int m = i + k;
                            int n = j + l;
                            if(m < 0 || n < 0 || m > 7 || n > 7 || (k == 0 && l == 0)) {
                                continue;
                            }
                            if(matrix[n][m] == EMPTY_DISC) {
                                move = new Move(this, n, m, turn);
                                if(move.is_legit() && !legit_moves.contains(move)) {
                                    legit_moves.add(move);
                                }
                            }
                        }
                    }
                }
            }
        }

        for(int i = 1; i < 7; i++) {
            for(int j = 1; j < 7; j++) {
                if(matrix[i][j] == -turn && neighbour[i][j] < 8) {
                    for(int k = -1; k <= 1; k++) {
                        for(int l = -1; l <= 1; l++) {
                            if(k == 0 && l == 0) {
                                continue;
                            }
                            if(matrix[i + k][j + l] == EMPTY_DISC) {
                                move = new Move(this, i + k, j + l, turn);
                                if(move.is_legit() && !legit_moves.contains(move)) {
                                    legit_moves.add(move);
                                }
                            }
                        }
                    }
                }
            }
        }
        return !legit_moves.isEmpty();
    }

    public State result(Move move) {
        if(move == null || !move.is_legit()) {
            return null;
        }
        int[][] matrix = new int[this.matrix.length][];
        for(int i = 0; i < this.matrix.length; i++) {
            matrix[i] = this.matrix[i].clone();
        }
        int[][] neighbour = new int[this.neighbour.length][];
        for(int i = 0; i < this.neighbour.length; i++) {
            neighbour[i] = this.neighbour[i].clone();
        }
        State state = new State(matrix, neighbour, -turn, depth + 1, depth_limit);
        state.matrix[move.row][move.col] = turn;
        int neighbours = 0;
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                int k = i + move.row;
                int l = j + move.col;
                if(k < 0 || l < 0 || l > 7 || k > 7 || (i == 0 && j ==0)) {
                    continue;
                }
                if(state.matrix[k][l] != EMPTY_DISC) {
                    state.neighbour[k][l]++;
                    neighbours++;
                }
            }
        }
        state.neighbour[move.row][move.col] = neighbours;
        //north
        if(move.north != 0) {
            for(int i = move.row - 1, j = 0; j < move.north; j++, i--) {
                state.matrix[i][move.col] *= -1;
            }
        }
        //south
        if(move.south != 0) {
            for(int i = move.row + 1, j = 0; j < move.south; j++, i++) {
                state.matrix[i][move.col] *= -1;
            }
        }
        //west
        if(move.west != 0) {
            for(int i = move.col - 1, j = 0; j < move.west; j++, i--) {
                state.matrix[move.row][i] *= -1;
            }
        }
        //east
        if(move.east != 0) {
            for(int i = move.col + 1, j = 0; j < move.east; j++, i++) {
                state.matrix[move.row][i] *= -1;
            }
        }
        //north_west
        if(move.north_west != 0) {
            for(int i = move.row - 1, j = move.col - 1, k = 0; k < move.north_west; i--, j--, k++) {
                state.matrix[i][j] *= -1;
            }
        }
        //north_east
        if(move.north_east != 0) {
            for(int i = move.row - 1, j = move.col + 1, k = 0; k < move.north_east; i--, j++, k++) {
                state.matrix[i][j] *= -1;
            }
        }
        //south_west
        if(move.south_west != 0) {
            for(int i = move.row + 1, j = move.col - 1, k = 0; k < move.south_west; i++, j--, k++) {
                state.matrix[i][j] *= -1;
            }
        }
        //south_east
        if(move.south_east != 0) {
            for(int i = move.row + 1, j = move.col + 1, k = 0; k < move.south_east; i++, j++, k++) {
                state.matrix[i][j] *= -1;
            }
        }
        return state;
    }

    public boolean init_successors() {
        successors = new ArrayList<>();
        if(init_legit_moves()) {
            for(Move move : legit_moves) {
                successors.add(result(move));
            }
            return true;
        }
        return false;
    }

    public double eval() {
        if(eval == null) {
            this.eval = new Evaluation(this);
            eval.init_eval();
        }
        double corners = 60 * (eval.corners_gained - eval.corners_lost) / 4;
        double cx = 45 * (eval.cx_lost - eval.cx_gained) / 8;
        double center = 75 * (eval.mobility - eval.frontier) / (eval.frontier <= 0 ? 1 : eval.frontier);
        double parity = 0;
        int region = 0;
//        for(int i = 0; i < eval.parity.length; i++) {
//            if(eval.parity[i] > 0) {
//                region++;
//                if(eval.parity[i] < 12 && eval.parity[i] % 2 != 0) {
//                    parity++;
//                }
//            }
//        }
//        parity = 30 * parity / (region == 0 ? 1 : region);

        double edge_balance = 0;
        for(int i = 0; i < eval.edge_balance.length; i++) {
            edge_balance += eval.edge_balance[i];
        }
        edge_balance = 80 * (4 + edge_balance) / 4;

        return corners + cx + center + parity + edge_balance;
    }

    public boolean cutoff() {
        return depth > depth_limit || (depth > depth_limit - 2 && (Runtime.getRuntime().freeMemory() < (Runtime.getRuntime().totalMemory() * 0.07)));
    }

    public boolean is_full() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(matrix[i][j] == State.EMPTY_DISC) {
                    return false;
                }
            }
        }

        return true;
    }

    public int count_max() {
        int max = 0;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(matrix[i][j] == State.MAX_DISC) {
                    max++;
                }
            }
        }
        return max;
    }

    public int count_min() {
        int max = 0;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(matrix[i][j] == State.MIN_DISC) {
                    max++;
                }
            }
        }
        return max;
    }
}
