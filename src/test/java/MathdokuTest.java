import org.example.Mathdoku;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class MathdokuTest {

    @Test
    public void test4x4Puzzle() throws Exception {
        String puzzle = """
                aabb
                cdee
                cfeg
                ffhg

                a 3 -
                b 1 -
                c 2 /
                d 2 =
                e 5 +
                f 9 *
                g 2 /
                h 4 =
                """;

        Mathdoku md = new Mathdoku();
        BufferedReader reader = new BufferedReader(new StringReader(puzzle));

        assertTrue(md.loadPuzzle(reader), "Puzzle failed to load");
        assertTrue(md.readyToSolve(), "Puzzle not ready to solve");
        assertTrue(md.solve(), "Solver failed");

        String expected =
                "1423\n" +
                        "4231\n" +
                        "2314\n" +
                        "3142\n";
        assertEquals(expected, md.print(), "Puzzle solution mismatch");
        System.out.println(md.print());
    }

    @Test
     public void testCount4x4Backtrack() throws Exception {
        // The standard 4×4 example: should require some backtracking
        String puzzle = """
                aabb
                cdee
                cfeg
                ffhg

                a 3 -
                b 1 -
                c 2 /
                d 2 =
                e 5 +
                f 9 *
                g 2 /
                h 4 =
                """;
        Mathdoku md = new Mathdoku();
        BufferedReader reader = new BufferedReader(new StringReader(puzzle));

        assertTrue(md.loadPuzzle(reader), "Failed to load 4×4 puzzle");
        assertTrue(md.readyToSolve(), "4×4 puzzle not ready to solve");
        assertTrue(md.solve(), "Solver failed on 4×4 puzzle");
        System.out.println(md.choices());
        assertTrue(md.choices() > 0, "4×4 puzzle should have at least one backtrack");
    }

    @Test
    public void test4x4Puzzle2Solves() throws Exception {
        String puzzle4x4 = """
        abcc
        ebcd
        effd
        ggfh

        a 1 =
        b 5 +
        c 48 *
        d 1 -
        e 5 +
        f 7 +
        g 3 -
        h 3 =
        """;

        Mathdoku md = new Mathdoku();
        BufferedReader reader = new BufferedReader(new StringReader(puzzle4x4));

        assertTrue(md.loadPuzzle(reader), "Puzzle failed to load");
        assertTrue(md.readyToSolve(), "Puzzle not ready to solve");
        assertTrue(md.solve(), "Solver failed");

        String expected =
                "1234\n" +
                        "2341\n" +
                        "3412\n" +
                        "4123\n";
        assertEquals(expected, md.print(), "Puzzle solution mismatch");
        System.out.println(md.print()   );
    }

    @Test
    public void test4x4Puzzle2Backtracks() throws Exception {
        String puzzle4x4 = """
        abcc
        ebcd
        effd
        ggfh

        a 1 =
        b 5 +
        c 48 *
        d 1 -
        e 5 +
        f 7 +
        g 3 -
        h 3 =
        """;

        Mathdoku md = new Mathdoku();
        BufferedReader reader = new BufferedReader(new StringReader(puzzle4x4));

        assertTrue(md.loadPuzzle(reader), "Failed to load puzzle");
        assertTrue(md.readyToSolve(), "Puzzle not ready to solve");
        assertTrue(md.solve(), "Solver failed");
        System.out.println(md.choices());
        assertTrue(md.choices() > 0, "4×4 puzzle should have at least one backtrack");
    }

    @Test
    public void test4x4Puzzle3Solves() throws Exception {
        String puzzle4x4 = """
        aefb
        gefh
        giih
        cjjd

        a 1 =
        b 3 =
        c 4 =
        d 1 =
        e 2 -
        f 4 /
        g 1 -
        h 2 /
        i 2 -
        j 1 -
        """;

        Mathdoku md = new Mathdoku();
        BufferedReader reader = new BufferedReader(new StringReader(puzzle4x4));

        assertTrue(md.loadPuzzle(reader), "Puzzle failed to load");
        assertTrue(md.readyToSolve(),  "Puzzle not ready to solve");
        assertTrue(md.solve(),         "Solver failed");

        String expected =
                "1243\n" +
                        "3412\n" +
                        "2134\n" +
                        "4321\n";
        assertEquals(expected, md.print(), "Puzzle solution mismatch");
        System.out.println(md.print());
    }

    @Test
    public void test4x4Puzzle3Backtracks() throws Exception {
        String puzzle4x4 = """
        aefb
        gefh
        giih
        cjjd

        a 1 =
        b 3 =
        c 4 =
        d 1 =
        e 2 -
        f 4 /
        g 1 -
        h 2 /
        i 2 -
        j 1 -
        """;
        Mathdoku md = new Mathdoku();
        BufferedReader reader = new BufferedReader(new StringReader(puzzle4x4));

        assertTrue(md.loadPuzzle(reader));
        assertTrue(md.readyToSolve());
        assertTrue(md.solve());
        // This one will require at least a few backtracks
        assertTrue(md.choices() > 0, "Expected some backtracking, got " + md.choices());
        System.out.println(md.choices());
    }


}

