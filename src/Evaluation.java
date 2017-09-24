public class Evaluation {
    public State state;
    public int corners_gained;
    public int corners_lost;
    public int cx_gained;
    public int cx_lost;
    public int frontier;
    public int mobility;
//    public int[] parity;
    public int[] edge_balance;

    public Evaluation(State state) {
        this.state = state;
    }

    public void init_eval() {
        eval_corners();
        eval_cx();
        eval_edge_balance();
        eval_frontier();
        eval_mobility();
//        eval_parity();
    }

    private boolean is_safe(int[][] matrix, int i, int j, int disc) {
        return false;
    }

//    public void eval_parity() {
//        int[][] matrix = new int[state.matrix.length][];
//        for(int i = 0; i < state.matrix.length; i++) {
//            matrix[i] = state.matrix[i].clone();
//        }
//        int[] parity = new int[10];
//        int count = 0;
//        for(int i = 0; i < 8; i++) {
//            for(int j = 0; j < 8; j++) {
//                if(matrix[i][j] == State.EMPTY_DISC) {
//                    parity[count] = dfs(matrix, i, j);
//                    count++;
//                }
//            }
//        }
//        this.parity = parity;
//        matrix = null;
//    }

    private int dfs(int[][] matrix, int i, int j) {
        if(matrix[i][j] != State.EMPTY_DISC) {
            return 0;
        }
        matrix[i][j] = State.MAX_DISC;
        int count = 1;
        int m, n = 0;
        for(int k = -1; k < 2; k++) {
            for(int l = -1; l < 2; l++) {
                m = i + k;
                n = j + l;
                if(m < 0 || m > 7 || n < 0 || n > 7 || (k == 0 && l == 0)) {
                    continue;
                }
                count += dfs(matrix, m, n);
            }
        }
        return count;
    }

    public int eval_corners() {
        int max_count = 0;
        int min_count = 0;
        for(int i = 0; i < 8; i+=7) {
            for(int j = 0; j < 8; j+=7) {
                if(state.matrix[i][j] == State.MIN_DISC) {
                    min_count++;
                }
                if(state.matrix[i][j] == State.MAX_DISC) {
                    max_count++;
                }
            }
        }
        corners_gained = max_count;
        corners_lost = min_count;
        return max_count - min_count;
    }

    public boolean eval_edge_balance() {
        edge_balance = new int[4];
        boolean north = true;
        if(state.matrix[0][0] == State.EMPTY_DISC && state.matrix[0][7] == State.EMPTY_DISC && (state.matrix[0][1] == State.EMPTY_DISC || state.matrix[0][6] == State.EMPTY_DISC)) {
            int n = 0;
            for(int i = 0; i < 8; i++) {
                //north edge
                if(state.matrix[0][i] == State.MAX_DISC) {
                    n++;
                }
            }
            if(n == 5) {
                north = false;
                edge_balance[0] = -1;
            }
        }
        boolean south = true;
        if(state.matrix[7][0] == State.EMPTY_DISC && state.matrix[7][7] == State.EMPTY_DISC && (state.matrix[7][1] == State.EMPTY_DISC || state.matrix[7][6] == State.EMPTY_DISC)) {
            int n = 0;
            for(int i = 0; i < 8; i++) {
                //north edge
                if(state.matrix[7][i] == State.MAX_DISC) {
                    n++;
                }
            }
            if(n == 5) {
                south = false;
                edge_balance[1] = -1;
            }
        }
        boolean west = true;
        if(state.matrix[0][0] == State.EMPTY_DISC && state.matrix[7][0] == State.EMPTY_DISC && (state.matrix[1][0] == State.EMPTY_DISC || state.matrix[6][0] == State.EMPTY_DISC)) {
            int n = 0;
            for(int i = 0; i < 8; i++) {
                //north edge
                if(state.matrix[i][0] == State.MAX_DISC) {
                    n++;
                }
            }
            if(n == 5) {
                west = false;
                edge_balance[2] = -1;
            }
        }
        boolean east = true;
        if(state.matrix[0][7] == State.EMPTY_DISC && state.matrix[7][7] == State.EMPTY_DISC && (state.matrix[1][7] == State.EMPTY_DISC || state.matrix[6][7] == State.EMPTY_DISC)) {
            int n = 0;
            for(int i = 0; i < 8; i++) {
                //north edge
                if(state.matrix[i][7] == State.MAX_DISC) {
                    n++;
                }
            }
            if(n == 5) {
                east = false;
                edge_balance[3] = -1;
            }
        };

        return east && west && north && south;
    }

    public int eval_frontier() {
        int count = 0;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(state.matrix[i][j] == State.MAX_DISC) {
                    if((i == 0 || i == 7 || j == 0 || j == 7) && state.neighbour[i][j] < 5) {
                        count++;
                        continue;
                    }
                    if(state.neighbour[i][j] < 8) {
                        count++;
                    }
                }
            }
        }
        frontier = count;
        return count;
    }

    public int eval_mobility() {
        int count= 0;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(state.matrix[i][j] == State.MIN_DISC) {
                    if((i == 0 || i == 7 || j == 0 || j == 7) && state.neighbour[i][j] < 5) {
                        count++;
                        continue;
                    }
                    if(state.neighbour[i][j] < 8) {
                        count++;
                    }
                }
            }
        }
        mobility = count;
        return count;
    }

    public int eval_cx() {
        int max_count = 0;
        int min_count = 0;

        for(int i = 1; i < 7; i+=5) {
            for(int j = 1; j < 7; j+=5) {
                if(state.matrix[i][j] == State.MIN_DISC) {
                    min_count++;
                }
                if(state.matrix[i][j] == State.MAX_DISC) {
                    max_count++;
                }
            }
        }

        for(int i = 1; i < 7; i+=5) {
            for(int j = 0; j < 8; j+=7) {
                if(state.matrix[i][j] == State.MIN_DISC) {
                    min_count++;
                }
                if(state.matrix[j][i] == State.MIN_DISC) {
                    min_count++;
                }
                if(state.matrix[i][j] == State.MAX_DISC) {
                    max_count++;
                }
                if(state.matrix[j][i] == State.MAX_DISC) {
                    max_count++;
                }
            }
        }

        cx_gained = max_count;
        cx_lost = min_count;
        return min_count - max_count;
    }
}
