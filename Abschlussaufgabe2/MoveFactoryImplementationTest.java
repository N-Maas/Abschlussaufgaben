package edu.kit.informatik.matchthree.tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.informatik.matchthree.MatchThreeBoard;
import edu.kit.informatik.matchthree.MoveFactoryImplementation;
import edu.kit.informatik.matchthree.framework.Position;
import edu.kit.informatik.matchthree.framework.Token;
import edu.kit.informatik.matchthree.framework.exceptions.BoardDimensionException;
import edu.kit.informatik.matchthree.framework.exceptions.TokenStringParseException;
import edu.kit.informatik.matchthree.framework.interfaces.Board;
import edu.kit.informatik.matchthree.framework.interfaces.Move;
import edu.kit.informatik.matchthree.framework.interfaces.MoveFactory;
import edu.kit.informatik.matchthree.tests.TestHelper;

public class MoveFactoryImplementationTest {
    private final MoveFactory factory = new MoveFactoryImplementation();

    @Test
    public void applyingTest() {
        TestHelper.expect(() -> this.factory.flipDown(new Position(-1, 0)), BoardDimensionException.class);
        TestHelper.expect(() -> this.factory.rotateSquareClockwise((new Position(0, -1))),
                BoardDimensionException.class);
        TestHelper.expect(() -> this.factory.rotateRowRight(-1), BoardDimensionException.class);
        TestHelper.expect(() -> this.factory.rotateColumnDown(-1), BoardDimensionException.class);

        Move flipDown = this.factory.flipDown(new Position(1, 1));
        Move flipRight = this.factory.flipRight(new Position(2, 0));
        Move square = this.factory.rotateSquareClockwise(new Position(0, 2));
        Move row = this.factory.rotateRowRight(2);
        Move column = this.factory.rotateColumnDown(3);

        Board board = new MatchThreeBoard(Token.set("123"), "13 1;2313");
        assertFalse(flipDown.canBeApplied(board));
        TestHelper.expect(() -> flipDown.apply(board), BoardDimensionException.class);
        assertTrue(flipRight.canBeApplied(board));
        flipRight.apply(board);
        assertEquals("131 ;2313", board.toTokenString());
        assertFalse(square.canBeApplied(board));
        TestHelper.expect(() -> square.apply(board), BoardDimensionException.class);
        assertFalse(row.canBeApplied(board));
        TestHelper.expect(() -> row.apply(board), BoardDimensionException.class);
        assertTrue(column.canBeApplied(board));
        column.apply(board);
        assertEquals("1313;231 ", board.toTokenString());

        Board board2 = new MatchThreeBoard(Token.set("123"), "13;21; 2;32");
        assertTrue(flipDown.canBeApplied(board2));
        flipDown.apply(board2);
        assertEquals("13;22; 1;32", board2.toTokenString());
        assertFalse(flipRight.canBeApplied(board2));
        TestHelper.expect(() -> flipRight.apply(board2), BoardDimensionException.class);
        assertTrue(square.canBeApplied(board2));
        square.apply(board2);
        assertEquals("13;22;3 ;21", board2.toTokenString());
        assertTrue(row.canBeApplied(board2));
        row.apply(board2);
        assertEquals("13;22; 3;21", board2.toTokenString());
        assertFalse(column.canBeApplied(board2));
        TestHelper.expect(() -> column.apply(board2), BoardDimensionException.class);
    }

    @Test
    public void reverseAndAffectedPositionsTest() {
        Move flipDown = this.factory.flipDown(new Position(1, 2));
        Move flipRight = this.factory.flipRight(new Position(0, 1));
        Move square1 = this.factory.rotateSquareClockwise(new Position(0, 1));
        Move square2 = this.factory.rotateSquareClockwise(new Position(2, 2));
        Move row = this.factory.rotateRowRight(2);
        Move column = this.factory.rotateColumnDown(3);

        Board board = new MatchThreeBoard(Token.set("123"), "1322;3221;2131;2312");

        flipDown.apply(board);
        flipDown.reverse().apply(board);
        assertEquals(TestHelper.positionSet(1, 2, 1, 3), flipDown.getAffectedPositions(board));
        assertEquals(TestHelper.positionSet(1, 2, 1, 3), flipDown.reverse().getAffectedPositions(board));
        assertEquals("1322;3221;2131;2312", board.toTokenString());

        flipRight.apply(board);
        flipRight.reverse().apply(board);
        flipRight.reverse().reverse().apply(board);
        assertEquals(TestHelper.positionSet(0, 1, 1, 1), flipRight.getAffectedPositions(board));
        assertEquals(TestHelper.positionSet(0, 1, 1, 1), flipRight.reverse().reverse().getAffectedPositions(board));
        assertEquals("1322;2321;2131;2312", board.toTokenString());

        square1.apply(board);
        square1.reverse().apply(board);
        assertEquals(TestHelper.positionSet(0, 1, 1, 1, 0, 2, 1, 2), square1.getAffectedPositions(board));
        assertEquals(TestHelper.positionSet(0, 1, 1, 1, 0, 2, 1, 2), square1.reverse().getAffectedPositions(board));
        assertEquals("1322;2321;2131;2312", board.toTokenString());

        square2.reverse().apply(board);
        square2.reverse().reverse().apply(board);
        square2.reverse().reverse().reverse().apply(board);
        assertEquals(TestHelper.positionSet(2, 2, 3, 2, 2, 3, 3, 3),
                square2.reverse().reverse().getAffectedPositions(board));
        assertEquals(TestHelper.positionSet(2, 2, 3, 2, 2, 3, 3, 3),
                square2.reverse().reverse().reverse().getAffectedPositions(board));
        assertEquals("1322;2321;2112;2331", board.toTokenString());

        row.reverse().apply(board);
        row.reverse().reverse().apply(board);
        assertEquals(TestHelper.positionSet(0, 2, 1, 2, 2, 2, 3, 2), row.getAffectedPositions(board));
        assertEquals(TestHelper.positionSet(0, 2, 1, 2, 2, 2, 3, 2), row.reverse().getAffectedPositions(board));
        assertEquals("1322;2321;2112;2331", board.toTokenString());

        column.apply(board);
        column.reverse().reverse().apply(board);
        column.reverse().reverse().reverse().reverse().reverse().apply(board);
        assertEquals(TestHelper.positionSet(3, 0, 3, 1, 3, 2, 3, 3),
                column.reverse().reverse().getAffectedPositions(board));
        assertEquals(TestHelper.positionSet(3, 0, 3, 1, 3, 2, 3, 3),
                column.reverse().reverse().reverse().getAffectedPositions(board));
        assertEquals("1321;2322;2111;2332", board.toTokenString());
    }
}