package github.eightoooeight.instantstringsearcher.junittest;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import github.eightoooeight.instantstringsearcher.junittest.testcases.*;

public class TestRunner {
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(JUnitTestSuite.class);

      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString() + "\n");
      }
		
      System.out.println(result.wasSuccessful() ? "The testes were all successful." : "The testes failed");
   }
}  	
