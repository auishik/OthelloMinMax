import java.util.LinkedList;

public class MinMax {
    public State root;
    public State next_state;
    public Move next_move;
    public int total_nodes;

    public MinMax(State root, int depth_limit) {
        this.root = root;
        this.root.depth = 0;
        this.root.legit_moves = null;
        this.root.successors = null;
        this.root.eval = null;
        this.root.depth_limit = depth_limit;
        total_nodes = 0;
        gen_tree();

        if(root.legit_moves == null || root.legit_moves.isEmpty()) {
            next_move = null;
            next_state = root.successors.get(0);
            return;
        }

        double max = Double.MIN_VALUE;
        double min = 0;
        for(int i = 0; i < root.successors.size(); i++) {
            min = gen_minmax(root.successors.get(i));
            if(max < min) {
                max = min;
                next_state = root.successors.get(i);
                next_move = root.legit_moves.get(i);
            }
        }
        root = null;
        next_state.successors = null;
    }

    public double gen_minmax(State state) {
        if(state.successors == null || state.successors.isEmpty()) {
//            System.out.println(state.depth);
            return state.eval();
        }

        if(state.turn == State.MAX_DISC) {
            double max = Double.MIN_VALUE;
            double min = 0;
            for(State successor : state.successors) {
                min = gen_minmax(successor);
                if(max < min) {
                    max = min;
                }
            }
//            System.out.println(state.depth);
            return max;
        }

        if(state.turn == State.MIN_DISC) {
            double min = Double.MAX_VALUE;
            double max = 0;
            for(State successor : state.successors) {
                max = gen_minmax(successor);
                if(max < min) {
                    min = max;
                }
            }
//            System.out.println(state.depth);
            return min;
        }

        return state.eval();
    }

    public void gen_tree() {
        LinkedList<State> queue = new LinkedList<>();
        queue.add(root);
        State state;
        double avg_branching_factor = 0;
        double count = 0;
        int dead = 0;
        while(!queue.isEmpty()) {
            state = queue.pollFirst();
            total_nodes++;
            if(!state.cutoff()) {
                System.out.println("Enter");
                if(state.init_successors()) {
                    for(State successor : state.successors) {
                        queue.add(successor);
                    }
                    dead = 0;
                } else {
                    if(dead != 1) {
                        int[][] matrix = new int[8][];
                        for (int i = 0; i < matrix.length; i++) {
                            matrix[i] = state.matrix[i].clone();
                        }
                        int[][] neighbour = new int[8][];
                        for (int i = 0; i < neighbour.length; i++) {
                            neighbour[i] = state.neighbour[i].clone();
                        }
                        state.successors.add(new State(matrix, neighbour, -state.turn, state.depth + 1, state.depth_limit));
                        dead++;
                    } else {
                        state.successors = null;
                    }
                }
            }
            count++;
                System.out.println(state.depth + " " + (state.legit_moves != null ? (avg_branching_factor = (state.legit_moves.size() / count) + (1 - 1 / count) * avg_branching_factor) : "null") + " " + queue.size() + " " + total_nodes + " " + Runtime.getRuntime().totalMemory()  + " " + Runtime.getRuntime().freeMemory());
        }
        state = null;
        queue = null;
    }
}
