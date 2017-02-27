package edu.kit.informatik.matchthree.tests;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import edu.kit.informatik.matchthree.MatchThreeBoard;
import edu.kit.informatik.matchthree.MaximumDeltaMatcher;
import edu.kit.informatik.matchthree.framework.Delta;
import edu.kit.informatik.matchthree.framework.Position;
import edu.kit.informatik.matchthree.framework.Token;
import edu.kit.informatik.matchthree.framework.exceptions.BoardDimensionException;
import edu.kit.informatik.matchthree.framework.exceptions.MatcherInitializationException;
import edu.kit.informatik.matchthree.framework.interfaces.Board;
import edu.kit.informatik.matchthree.tests.TestHelper;

public class MaximumDeltaMatcherTest {

    @Test
    public void basicTest() {
        TestHelper.expect(() -> new MaximumDeltaMatcher(TestHelper.deltaSet()), MatcherInitializationException.class);
        TestHelper.expect(() -> new MaximumDeltaMatcher(TestHelper.setOf(null, null)),
                MatcherInitializationException.class);
        TestHelper.expect(() -> new MaximumDeltaMatcher(TestHelper.deltaSet(0, 0)),
                MatcherInitializationException.class);
        TestHelper.expect(() -> new MaximumDeltaMatcher(TestHelper.setOf(null, new Delta(1, 0))),
                MatcherInitializationException.class);
        TestHelper.expect(() -> new MaximumDeltaMatcher(TestHelper.deltaSet(0, 0, 0, 1)),
                MatcherInitializationException.class);

        MaximumDeltaMatcher m = new MaximumDeltaMatcher(TestHelper.deltaSet(1, 0));
        Board board = new MatchThreeBoard(Token.set("OX"), "XX;OX");
        TestHelper.expect(() -> m.match(board, new Position(2, 0)), BoardDimensionException.class);
        TestHelper.expect(() -> m.matchAll(board, TestHelper.setOf(new Position(0, 2))), BoardDimensionException.class);
        assertEquals(TestHelper.setOf(TestHelper.positionSet(0, 0, 1, 0)), m.match(board, new Position(0, 0)));

        Set<Delta> deltas = TestHelper.deltaSet(0, 1);
        MaximumDeltaMatcher m2 = new MaximumDeltaMatcher(deltas);
        Board board2 = new MatchThreeBoard(Token.set("OX"), "X O;OOO; X ");
        assertEquals(TestHelper.setOf(TestHelper.positionSet(2, 1, 2, 0)), m2.match(board2, new Position(2, 1)));
        assertEquals(new HashSet<Set<Position>>(), m2.match(board2, new Position(1, 0)));

        deltas.clear();
        assertEquals("Deltas are mutable!", TestHelper.setOf(TestHelper.positionSet(2, 1, 2, 0)),
                m2.match(board2, new Position(2, 0)));
    }

    @Test
    public void combinationTest() {
        MaximumDeltaMatcher m = new MaximumDeltaMatcher(TestHelper.deltaSet(1, 0, 0, -1));
        Board board = new MatchThreeBoard(Token.set("OX"), "XXO;X O;XOO");
        assertEquals(TestHelper.setOf(TestHelper.positionSet(0, 0, 1, 0, 0, 1, 0, 2)),
                m.match(board, new Position(1, 0)));
        assertEquals(
                TestHelper.setOf(TestHelper.positionSet(0, 0, 1, 0, 0, 1, 0, 2),
                        TestHelper.positionSet(2, 0, 2, 1, 1, 2, 2, 2)),
                m.matchAll(board, TestHelper.positionSet(0, 2, 2, 0)));
        assertEquals("Expected reduction to 2 matches by set.",
                TestHelper.setOf(TestHelper.positionSet(0, 0, 1, 0, 0, 1, 0, 2),
                        TestHelper.positionSet(2, 0, 2, 1, 1, 2, 2, 2)),
                m.matchAll(board, TestHelper.positionSet(0, 2, 0, 1, 0, 0, 2, 0, 2, 1, 2, 2)));

        m = new MaximumDeltaMatcher(TestHelper.deltaSet(1, -1, -1, -1));
        board = new MatchThreeBoard(Token.set("OX"), "XOX;OXO;OOX");
        assertEquals("Expected reduction to 3 matches by set.",
                TestHelper.setOf(TestHelper.positionSet(0, 0, 2, 0, 1, 1, 2, 2),
                        TestHelper.positionSet(1, 0, 0, 1, 2, 1, 1, 2), TestHelper.positionSet(0, 2)),
                m.matchAll(board, TestHelper.positionSet(0, 2, 0, 1, 0, 0, 2, 0, 2, 1, 2, 2)));

        m = new MaximumDeltaMatcher(TestHelper.deltaSet(2, 1));
        board = new MatchThreeBoard(Token.set("OX"), "XOX O; XXOO;XOOX ");
        assertEquals("Expected reduction to 2 matches by set.",
                TestHelper.setOf(TestHelper.positionSet(0, 0, 2, 1), TestHelper.positionSet(1, 0, 3, 1)),
                m.matchAll(board, TestHelper.positionSet(0, 0, 2, 1, 4, 2, 3, 1)));
    }
}