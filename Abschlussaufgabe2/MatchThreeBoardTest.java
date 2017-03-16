package edu.kit.informatik.matchthree.tests;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.rules.ExpectedException;
import org.junit.runners.model.Statement;

import edu.kit.informatik.matchthree.*;
import edu.kit.informatik.matchthree.framework.*;
import edu.kit.informatik.matchthree.framework.exceptions.BoardDimensionException;
import edu.kit.informatik.matchthree.framework.exceptions.IllegalTokenException;
import edu.kit.informatik.matchthree.framework.exceptions.NoFillingStrategyException;
import edu.kit.informatik.matchthree.framework.exceptions.TokenStringParseException;
import edu.kit.informatik.matchthree.framework.interfaces.*;

public class MatchThreeBoardTest {

    @Test
    public void setGetRemoveTest() {
        TestHelper.expect(() -> new MatchThreeBoard(Token.set("ABC"), 1, 3), BoardDimensionException.class);
        TestHelper.expect(() -> new MatchThreeBoard(Token.set("A"), 5, 5), IllegalArgumentException.class);
        Board board = new MatchThreeBoard(Token.set("ABC"), 3, 3);

        assertFalse(board.containsPosition(new Position(0, -1)));
        assertFalse(board.containsPosition(new Position(3, 1)));
        assertTrue(board.containsPosition(new Position(2, 1)));
        assertTrue(board.containsPosition(new Position(0, 2)));

        TestHelper.expect(() -> board.getTokenAt(new Position(-1, 0)), BoardDimensionException.class);
        TestHelper.expect(() -> board.getTokenAt(new Position(0, -1)), BoardDimensionException.class);
        TestHelper.expect(() -> board.getTokenAt(new Position(3, 0)), BoardDimensionException.class);
        TestHelper.expect(() -> board.getTokenAt(new Position(0, 3)), BoardDimensionException.class);

        assertNull(board.getTokenAt(new Position(2, 2)));
        assertNull(board.getTokenAt(new Position(0, 0)));

        TestHelper.expect(() -> board.setTokenAt(new Position(-1, 0), new Token("A")), BoardDimensionException.class);
        TestHelper.expect(() -> board.setTokenAt(new Position(3, 0), new Token("A")), BoardDimensionException.class);
        TestHelper.expect(() -> board.setTokenAt(new Position(1, 1), new Token("D")), IllegalTokenException.class);

        board.setTokenAt(new Position(0, 0), new Token("A"));
        board.setTokenAt(new Position(1, 1), new Token("A"));
        board.setTokenAt(new Position(2, 0), new Token("B"));
        board.setTokenAt(new Position(0, 2), new Token("C"));

        assertEquals(new Token("B"), board.getTokenAt(new Position(2, 0)));
        assertEquals(new Token("A"), board.getTokenAt(new Position(1, 1)));

        board.setTokenAt(new Position(1, 1), null);
        assertNull(board.getTokenAt(new Position(1, 1)));

        board.setTokenAt(new Position(1, 1), new Token("B"));
        TestHelper.expect(() -> board.removeTokensAt(TestHelper.positionSet(-1, 0, 2, 0, 0, 2)),
                BoardDimensionException.class);
        TestHelper.expect(() -> board.removeTokensAt(TestHelper.positionSet(0, 0, 2, 0, 0, 3)),
                BoardDimensionException.class);
        assertEquals(new Token("B"), board.getTokenAt(new Position(2, 0)));
        assertEquals(new Token("C"), board.getTokenAt(new Position(0, 2)));

        board.removeTokensAt(TestHelper.positionSet(0, 0, 2, 0, 0, 2));
        assertNull(board.getTokenAt(new Position(0, 2)));
        assertNull(board.getTokenAt(new Position(0, 0)));
        assertEquals(new Token("B"), board.getTokenAt(new Position(1, 1)));

        Set<Token> tokens = Token.set("AB");
        Board board2 = new MatchThreeBoard(tokens, 2, 2);
        tokens.removeAll(Token.set("AB"));
        assertEquals("Tokens are mutable!", board2.getAllValidTokens(), Token.set("AB"));
        board2.setTokenAt(new Position(0, 0), new Token("A"));
    }

    @Test
    public void stringTest() {
        TestHelper.expect(() -> new MatchThreeBoard(Token.set("ABC"), "ABC;AB"), TokenStringParseException.class);
        TestHelper.expect(() -> new MatchThreeBoard(Token.set("ABC"), "ABC;ABD"), TokenStringParseException.class);
        TestHelper.expect(() -> new MatchThreeBoard(Token.set("ABC"), "ABC;A;D"), TokenStringParseException.class);
        TestHelper.expect(() -> new MatchThreeBoard(Token.set("ABC"), "ABC;ABC;"), TokenStringParseException.class);
        TestHelper.expect(() -> new MatchThreeBoard(Token.set("ABC"), "ABC;ABD "), TokenStringParseException.class);
        TestHelper.expect(() -> new MatchThreeBoard(Token.set("ABC"), " ABC;ABD"), TokenStringParseException.class);
        Board board = new MatchThreeBoard(Token.set("ABC"), "ABC;BCA;AAA");
        assertEquals("ABC;BCA;AAA", board.toTokenString());
        assertEquals(new Token("C"), board.getTokenAt(new Position(2, 0)));
        assertEquals(new Token("A"), board.getTokenAt(new Position(0, 2)));
        assertEquals(new Token("B"), board.getTokenAt(new Position(0, 1)));
        board.setTokenAt(new Position(1, 1), null);
        board.setTokenAt(new Position(2, 0), new Token("B"));
        board.setTokenAt(new Position(0, 2), new Token("C"));
        assertEquals("ABB;B A;CAA", board.toTokenString());

        board = new MatchThreeBoard(Token.set("ABC"), " CA;B B;AC ");
        assertEquals(" CA;B B;AC ", board.toTokenString());
        assertNull(board.getTokenAt(new Position(0, 0)));
        assertNull(board.getTokenAt(new Position(1, 1)));
        assertEquals(board.toTokenString(),
                new MatchThreeBoard(board.getAllValidTokens(), board.toTokenString()).toTokenString());
        board.removeTokensAt(TestHelper.positionSet(2, 0, 0, 2));
        assertEquals(" C ;B B; C ", board.toTokenString());
    }

    @Test
    public void tokenMovingTest() {
        Board board = new MatchThreeBoard(Token.set("ABC"), "AA ;BBB; CC");
        TestHelper.expect(() -> board.setFillingStrategy(null), NullPointerException.class);
        TestHelper.expect(board::fillWithTokens, NoFillingStrategyException.class);
        TestHelper.expect(() -> board.swapTokens(new Position(-1, 0), new Position(1, 1)),
                BoardDimensionException.class);

        assertEquals("AA ;BBB; CC", board.toTokenString());
        board.swapTokens(new Position(1, 0), new Position(1, 1));
        board.swapTokens(new Position(2, 0), new Position(2, 2));
        assertEquals("ABC;BAB; C ", board.toTokenString());
        board.swapTokens(new Position(1, 0), new Position(1, 1));
        board.swapTokens(new Position(1, 1), new Position(0, 0));
        assertEquals("BAC;BAB; C ", board.toTokenString());

        assertEquals(TestHelper.positionSet(0, 0, 0, 1, 0, 2, 2, 0, 2, 1, 2, 2), board.moveTokensToBottom());
        assertEquals(" A ;BAC;BCB", board.toTokenString());
        assertTrue(board.moveTokensToBottom().isEmpty());

        Board b2 = new MatchThreeBoard(Token.set("AB"), " A;AA;BB;B ");
        assertEquals(TestHelper.positionSet(1, 0, 1, 1, 1, 2, 1, 3), b2.moveTokensToBottom());
        assertEquals("  ;AA;BA;BB", b2.toTokenString());

        Board b3 = new MatchThreeBoard(Token.set("AB"), "A B; BB; AA;B  ;  B;A  ");
        b3.moveTokensToBottom();

        // UNKLAR

        // int[] pos = new int[34];
        // int x = 2;
        // int y = 0;
        // for (int i = 0; i < pos.length; i += 2) {
        // pos[i] = x;
        // pos[i + 1] = y;
        // x--;
        // if (x < 0) {
        // x = 2;
        // y++;
        // }
        // }
        // assertEquals(b3.moveTokensToBottom(), TestHelper.positionSet(pos).remove(new Position(1,
        // 0)));
        assertEquals("   ;   ;  B;A B;BBA;AAB", b3.toTokenString());
        b3.setFillingStrategy(new DeterministicStrategy(TestHelper.tokenIterators("BAA", "AABB", "AB")));
        b3.fillWithTokens();
        assertEquals("ABB;ABA;BAB;AAB;BBA;AAB", b3.toTokenString());
    }
}
