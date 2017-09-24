class Move {
    public State state;
    public int disc;
    public int row;
    public int col;
    public int north;
    public int south;
    public int east;
    public int west;
    public int north_east;
    public int north_west;
    public int south_east;
    public int south_west;

    Move(State state, int row, int col, int disc) {
        this.state = state;
        this.row = row;
        this.col = col;
        this.disc = disc;
        init();
    }

    void init() {
        init_north();
        init_south();
        init_west();
        init_east();
        init_north_east();
        init_north_west();
        init_south_east();
        init_south_west();
    }

    boolean init_north() {
        north = 0;
        boolean flippable = false;
        for(int i = row - 1; i >= 0 && state.matrix[i][col] != State.EMPTY_DISC; i--) {
            if(state.matrix[i][col] == disc) {
                flippable = true;
                break;
            }
            north++;
        }
        if(!flippable) {
            north = 0;
            return false;
        }
        return north != 0;
    }

    boolean init_south() {
        south = 0;
        boolean flippable = false;
        for(int i = row + 1; i < 8 && state.matrix[i][col] != State.EMPTY_DISC; i++) {
            if(state.matrix[i][col] == disc) {
                flippable = true;
                break;
            }
            south++;
        }
        if(!flippable) {
            south = 0;
            return false;
        }
        return south != 0;
    }

    boolean init_west() {
        west = 0;
        boolean flippable = false;
        for(int i = col - 1; i >= 0 && state.matrix[row][i] != State.EMPTY_DISC; i--) {
            if(state.matrix[row][i] == disc) {
                flippable = true;
                break;
            }
            west++;
        }
        if(!flippable) {
            west = 0;
            return false;
        }
        return west != 0;
    }

    boolean init_east() {
        east = 0;
        boolean flippable = false;
        for(int i = col + 1; i < 8 && state.matrix[row][i] != State.EMPTY_DISC; i++) {
            if(state.matrix[row][i] == disc) {
                flippable = true;
                break;
            }
            east++;
        }
        if(!flippable) {
            east = 0;
            return false;
        }
        return east != 0;
    }

    boolean init_north_west() {
        north_west = 0;
        boolean flippable = false;
        for(int i = row - 1, j = col - 1; i >= 0 && j >= 0 && state.matrix[i][j] != State.EMPTY_DISC; i--, j--) {
            if(state.matrix[i][j] == disc) {
                flippable = true;
                break;
            }
            north_west++;
        }
        if(!flippable) {
            north_west = 0;
            return false;
        }
        return north_west != 0;
    }

    boolean init_north_east() {
        north_east = 0;
        boolean flippable = false;
        for(int i = row - 1, j = col + 1; i >= 0 && j < 8 && state.matrix[i][j] != State.EMPTY_DISC; i--, j++) {
            if(state.matrix[i][j] == disc) {
                flippable = true;
                break;
            }
            north_east++;
        }
        if(!flippable) {
            north_east = 0;
            return false;
        }
        return north_east != 0;
    }

    boolean init_south_west() {
        south_west = 0;
        boolean flippable = false;
        for(int i = row + 1, j = col - 1; i < 8 && j >= 0 && state.matrix[i][j] != State.EMPTY_DISC; i++, j--) {
            if(state.matrix[i][j] == disc) {
                flippable = true;
                break;
            }
            south_west++;
        }
        if(!flippable) {
            south_west = 0;
            return false;
        }
        return south_west != 0;
    }

    boolean init_south_east() {
        south_east = 0;
        boolean flippable = false;
        for(int i = row + 1, j = col + 1; i < 8 && j < 8 && state.matrix[i][j] != State.EMPTY_DISC; i++, j++) {
            if(state.matrix[i][j] == disc) {
                flippable = true;
                break;
            }
            south_east++;
        }
        if(!flippable) {
            south_east = 0;
            return false;
        }
        return south_east != 0;
    }

    boolean is_legit() {
        return state.matrix[row][col] == State.EMPTY_DISC && (north + south + west + east + north_east + north_west + south_east + south_west) != 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Move && ((Move) obj).state == this.state && ((Move) obj).row == this.row && ((Move) obj).col == this.col;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() + (row / col + row);
    }
}
