package edu.kit.informatik.matchthree.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.kit.informatik.matchthree.framework.Delta;
import edu.kit.informatik.matchthree.framework.Position;
import edu.kit.informatik.matchthree.framework.Token;
import edu.kit.informatik.matchthree.framework.exceptions.BoardDimensionException;

public class TestHelper {
    private TestHelper() {
        throw new UnsupportedOperationException("Utility class constructor.");
    }

    public static Set<Position> positionSet(int... pos) {
        if ((pos.length & 1) != 0) {
            throw new IllegalArgumentException("Just number required [for (x, y) pairs].");
        }
        Set<Position> result = new HashSet<>();
        for (int i = 0; i < pos.length - 1; i += 2) {
            result.add(new Position(pos[i], pos[i + 1]));
        }
        return result;
    }

    public static Set<Delta> deltaSet(int... deltas) {
        return positionSet(deltas).stream().map(p -> new Delta(p.x, p.y)).collect(HashSet::new, Set::add, Set::addAll);
    }

    public static <T> Set<T> setOf(T... set) {
        Set<T> result = new HashSet<>();
        for (T el : set) {
            result.add(el);
        }
        return result;
    }

    public static void expect(Runnable code, Class<? extends Throwable> exception) {
        try {
            code.run();
            fail(exception + " expected.");
        } catch (Throwable e) {
            assertTrue("Expected: " + e.getClass() + ", is: " + exception, exception.isInstance(e));
        }
    }

    public static Iterator<Token>[] tokenIterators(String... tokens) {
        Iterator<Token>[] iterators = new Iterator[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            int buffer = i;
            iterators[i] = new Iterator<Token>() {
                private int counter = 0;

                @Override
                public boolean hasNext() {
                    return this.counter < tokens[buffer].length();
                }

                @Override
                public Token next() {
                    Token result = new Token(tokens[buffer].charAt(this.counter));
                    this.counter++;
                    return result;
                }

            };
        }
        return iterators;
    }
}
