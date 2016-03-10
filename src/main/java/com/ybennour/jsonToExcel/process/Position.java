package com.ybennour.jsonToExcel.process;

public class Position implements Comparable<Position> {
    public int line;

    public int col;

    public Position(int line, int col) {
        this.line = line;
        this.col = col;
    }

    public int compareTo(Position o) {
        if (this.line > o.line) {
            return 1;
        } else if (this.line < o.line) {
            return -1;
        } else {
            if (this.col > o.col) {
                return 1;
            } else if (this.col < o.col) {
                return -1;
            }
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Position) {
            return this.col == ((Position) o).col && this.line == ((Position) o).line;
        }
        return false;
    }
}
