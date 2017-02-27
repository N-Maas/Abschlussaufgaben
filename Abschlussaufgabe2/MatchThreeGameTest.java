package edu.kit.informatik.matchthree.tests;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import edu.kit.informatik.matchthree.MatchThreeBoard;
import edu.kit.informatik.matchthree.MatchThreeGame;
import edu.kit.informatik.matchthree.MaximumDeltaMatcher;
import edu.kit.informatik.matchthree.MoveFactoryImplementation;
import edu.kit.informatik.matchthree.framework.DeterministicStrategy;
import edu.kit.informatik.matchthree.framework.Position;
import edu.kit.informatik.matchthree.framework.Token;
import edu.kit.informatik.matchthree.framework.interfaces.Board;
import edu.kit.informatik.matchthree.framework.interfaces.Game;
import edu.kit.informatik.matchthree.framework.interfaces.MoveFactory;

public class MatchThreeGameTest {

    // @Test
    // public void reductionTest() {
    // assertEquals(
    // TestHelper.setOf(TestHelper.positionSet(1, 0), TestHelper.positionSet(1, 1, 2, 0),
    // TestHelper.positionSet(3, 3)),
    // MatchThreeGame.reduceMatches(
    // TestHelper.setOf(TestHelper.positionSet(1, 0), TestHelper.positionSet(1, 1, 2, 0),
    // TestHelper.positionSet(3, 3), TestHelper.positionSet(3, 3))));
    // assertEquals(TestHelper.setOf(TestHelper.positionSet(1, 0, 1, 1, 2, 2)),
    // MatchThreeGame.reduceMatches(TestHelper.setOf(TestHelper.positionSet(1, 0),
    // TestHelper.positionSet(1, 0, 1, 1), TestHelper.positionSet(1, 0, 1, 1),
    // TestHelper.positionSet(1, 1, 1, 0), TestHelper.positionSet(1, 1, 1, 0, 2, 2))));
    // assertEquals(
    // TestHelper.setOf(TestHelper.positionSet(1, 0, 1, 1, 1, 2), TestHelper.positionSet(0, 1, 1, 1,
    // 2, 1),
    // TestHelper.positionSet(1, 0, 0, 1)),
    // MatchThreeGame.reduceMatches(
    // TestHelper.setOf(TestHelper.positionSet(1, 0, 1, 1, 1, 2), TestHelper.positionSet(1, 1),
    // TestHelper.positionSet(0, 1, 1, 1, 2, 1), TestHelper.positionSet(1, 0, 1, 2),
    // TestHelper.positionSet(1, 0, 0, 1), TestHelper.positionSet(1, 0, 0, 1),
    // TestHelper.positionSet(2, 1, 1, 1), TestHelper.positionSet(2, 1))));
    // assertEquals(new TreeSet<Set<Position>>(), MatchThreeGame.reduceMatches(new
    // HashSet<Set<Position>>()));
    // }

    @Test
    public void gameTestA() {
        Board board = new MatchThreeBoard(Token.set("OX#"), "#XO;OOO;#X#");
        board.setFillingStrategy(new DeterministicStrategy(TestHelper.tokenIterators("OOX#O", "#OOX#", "O#XO")));
        Game game = new MatchThreeGame(board, new MaximumDeltaMatcher(TestHelper.deltaSet(1, 0, 0, 1)));
        MoveFactory factory = new MoveFactoryImplementation();

        game.initializeBoardAndStart();
        assertEquals(5, game.getScore());
        assertEquals("O##;#XO;#X#", board.toTokenString());

        game.acceptMove(factory.rotateRowRight(0).reverse());
        assertEquals(34, game.getScore());
        assertEquals("O#O;#XX;OO#", board.toTokenString());

        game.acceptMove(factory.flipDown(new Position(1, 1)));
        assertEquals(34, game.getScore());
        assertEquals("O#O;#OX;OX#", board.toTokenString());
    }

    @Test
    public void gameTestB() {
        Board board = new MatchThreeBoard(Token.set("ABC"), "CAAB;ABCC;ACBA");
        board.setFillingStrategy(
                new DeterministicStrategy(TestHelper.tokenIterators("BCA", "CBABABCB", "BBAACACA", "ABACCB")));
        Game game = new MatchThreeGame(board, new MaximumDeltaMatcher(TestHelper.deltaSet(-1, 0)));
        MoveFactory factory = new MoveFactoryImplementation();

        game.initializeBoardAndStart();
        assertEquals(0, game.getScore());
        assertEquals("CAAB;ABCC;ACBA", board.toTokenString());

        game.acceptMove(factory.rotateColumnDown(0));
        assertEquals(3, game.getScore());
        assertEquals("BCBB;CBCC;ACBA", board.toTokenString());

        game.acceptMove(factory.rotateSquareClockwise(new Position(0, 0)));
        assertEquals(30, game.getScore());
        assertEquals("CACA;CBAB;ACBA", board.toTokenString());

        game.acceptMove(factory.rotateColumnDown(2).reverse());
        assertEquals(52, game.getScore());
        assertEquals("ABAB;CBAC;ACCA", board.toTokenString());
    }
}