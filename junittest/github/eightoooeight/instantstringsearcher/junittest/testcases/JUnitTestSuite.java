package github.eightoooeight.instantstringsearcher.junittest.testcases;

import github.eightoooeight.instantstringsearcher.junittest.testcases.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    RawDataClassesTester.class,
    TrieNodeClassesTester.class,
    InstantStringSearcherTester.class,
    ImprovedSyncStringSearcherSyncTester.class
})

public class JUnitTestSuite {   
}
