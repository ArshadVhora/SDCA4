package org.example;

import java.io.*;
import java.util.*;

public class Mathdoku {
    private int n;
    private char[][] groups;
    private int[][] board;
    private Map<Character, Cage> cages = new HashMap<>();
    private boolean[][] rowUsed, colUsed;
    private int choices = 0;

    private static class Cage {
        char id;
        int target;
        char op;
        List<int[]> cells = new ArrayList<>();
        Cage(char id) { this.id = id; }
    }

    public boolean loadPuzzle(BufferedReader in) {
        try {
            List<String> gridLines = new ArrayList<>();
            String line;
            // Read first non-empty line
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) break;
            }
            if (line == null) return false;
            int size = line.length();
            gridLines.add(line);
            // Read remaining (n-1) grid lines
            for (int i = 1; i < size; i++) {
                line = in.readLine();
                if (line == null) return false;
                line = line.trim();
                if (line.length() != size) return false;
                gridLines.add(line);
            }
            // Initialize structures
            this.n = size;
            groups = new char[n][n];
            for (int r = 0; r < n; r++) {
                String row = gridLines.get(r);
                for (int c = 0; c < n; c++) {
                    char id = row.charAt(c);
                    groups[r][c] = id;
                    cages.computeIfAbsent(id, Cage::new).cells.add(new int[]{r, c});
                }
            }
            // Read cage constraints
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\s+");
                if (parts.length != 3) return false;
                char id = parts[0].charAt(0);
                int target = Integer.parseInt(parts[1]);
                char op = parts[2].charAt(0);
                Cage c = cages.get(id);
                if (c == null) return false;
                c.target = target;
                c.op = op;
            }
            return true;
        } catch (IOException|NumberFormatException e) {
            return false;
        }
    }

    public boolean readyToSolve() {
        if (n <= 0 || cages.isEmpty()) return false;
        // Basic checks on each cage
        for (Cage c : cages.values()) {
            int sz = c.cells.size();
            if (sz == 0) return false;
            switch (c.op) {
                case '=':
                    if (sz != 1) return false;
                    break;
                case '+': case '*':
                    break; // any size ≥2 is fine
                case '-': case '/':
                    if (sz != 2) return false;
                    break;
                default:
                    return false;
            }
        }
        // Prepare board and marking arrays
        board = new int[n][n];
        rowUsed = new boolean[n][n+1];
        colUsed = new boolean[n][n+1];
        return true;
    }

    public boolean solve() {
        return backtrack(0, 0);
    }

    private boolean backtrack(int r, int c) {
        if (r == n) return true;
        int nr = c == n-1 ? r+1 : r;
        int nc = c == n-1 ? 0   : c+1;
        if (board[r][c] != 0)
            return backtrack(nr, nc);

        for (int v = 1; v <= n; v++) {
            if (rowUsed[r][v] || colUsed[c][v]) continue;
            board[r][c] = v;
            rowUsed[r][v] = colUsed[c][v] = true;
            Cage cage = cages.get(groups[r][c]);
            if (checkPartial(cage) && backtrack(nr, nc)) {
                return true;
            }
            // undo
            board[r][c] = 0;
            rowUsed[r][v] = colUsed[c][v] = false;
            choices++;
        }
        return false;
    }

    private boolean checkPartial(Cage c) {
        List<Integer> vals = new ArrayList<>();
        for (int[] cell : c.cells) {
            int x = board[cell[0]][cell[1]];
            if (x > 0) vals.add(x);
        }
        int assigned = vals.size();
        switch (c.op) {
            case '=':
                return vals.get(0) == c.target;
            case '+': {
                int sum = vals.stream().mapToInt(i->i).sum();
                if (sum > c.target) return false;
                if (assigned == c.cells.size()) return sum == c.target;
                return true;
            }
            case '*': {
                int prod = vals.stream().reduce(1, (a,b)->a*b);
                if (prod > c.target || c.target % prod != 0) return false;
                if (assigned == c.cells.size()) return prod == c.target;
                return true;
            }
            case '-':
                if (assigned < 2) return true;
                int d1 = vals.get(0), d2 = vals.get(1);
                return Math.abs(d1 - d2) == c.target;
            case '/':
                if (assigned < 2) return true;
                int a = vals.get(0), b = vals.get(1);
                int mx = Math.max(a,b), mn = Math.min(a,b);
                return mn != 0 && mx % mn == 0 && (mx/mn) == c.target;
            default:
                return false;
        }
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                sb.append(board[r][c] == 0 ? groups[r][c] : board[r][c]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public int choices() {
        return choices;
    }

    public static void main(String[] args) throws Exception {
        Mathdoku md = new Mathdoku();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        if (!md.loadPuzzle(br)) {
            System.err.println("Error: could not load puzzle.");
            return;
        }
        if (!md.readyToSolve()) {
            System.err.println("Error: puzzle not ready to solve.");
            return;
        }
        if (md.solve()) {
            // solved!
            System.out.print(md.print());
            System.err.println("Choices (backtracks): " + md.choices());
        } else {
            System.err.println("No solution found.");
        }
    }
}
