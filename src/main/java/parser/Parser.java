package parser;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import codegenerator.CodeGeneratorFacade;
import errorhandler.ErrorHandler;
import scanner.lexicalAnalyzer;
import scanner.token.Token;


public class Parser {
  private List<Rule> rules;
  private Stack<Integer> parsStack;
  private ParseTable parseTable;
  private lexicalAnalyzer lexicalAnalyzer;
  private CodeGeneratorFacade cgf;

  public Parser() {
    parsStack = new Stack<Integer>();
    parsStack.push(0);
    try {
      parseTable = new ParseTable(Files.readAllLines(Paths.get("src/main/resources/parseTable")).get(0));
    } catch (Exception e) {
      e.printStackTrace();
    }
    rules = new ArrayList<Rule>();
    try {
      for (String stringRule : Files.readAllLines(Paths.get("src/main/resources/Rules"))) {
        rules.add(new Rule(stringRule));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    cgf = new CodeGeneratorFacade();
  }

  public void startParse(java.util.Scanner sc) {
    lexicalAnalyzer = new lexicalAnalyzer(sc);
    Token lookAhead = lexicalAnalyzer.getNextToken();
    boolean finish = false;
    Action currentAction;
    while (!finish) {
      try {
        currentAction = parseTable.getActionTable(parsStack.peek(), lookAhead);

        switch (currentAction.getAction()) {
          case shift:
            lookAhead = doShiftAction(parsStack, lexicalAnalyzer, currentAction);
            break;
          case reduce:
            doReduceAction(currentAction, lookAhead);
            break;
          case accept:
            finish = true;
            break;
        }

      } catch (Exception ignored) {

        ignored.printStackTrace();
      }


    }
    if (!ErrorHandler.isHasError())
      cgf.printMemory();


  }

  private Token doShiftAction(Stack<Integer> parsStack, lexicalAnalyzer lexicalAnalyzer, Action currentAction){
    parsStack.push(currentAction.getNumber());
    return lexicalAnalyzer.getNextToken();
  }

  private void doReduceAction(Action currentAction, Token lookAhead){
    Rule rule = rules.get(currentAction.getNumber());
    for (int i = 0; i < rule.getRHS().size(); i++) {
      parsStack.pop();
    }
    parsStack.push(parseTable.getGotoTable(parsStack.peek(), rule.getLHS()));
    try {
      cgf.semanticFunction(rule, lookAhead);
    } catch (Exception e) {
    }
  }


}
