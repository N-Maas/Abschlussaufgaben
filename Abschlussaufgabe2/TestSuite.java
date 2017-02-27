package edu.kit.informatik.matchthree.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({ MatchThreeBoardConstructorTest.class, MatchThreeBoardTest.class,
        MoveFactoryImplementationTest.class, MaximumDeltaMatcherTest.class, MatchThreeGameTest.class })

public class TestSuite {
}