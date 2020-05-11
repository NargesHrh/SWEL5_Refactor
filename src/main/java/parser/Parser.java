package parser;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import codegenerator.CodeGeneratorFacade;
import errorHandler.ErrorHandler;
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
        //Log.print("");

        switch (currentAction.getAction()) {
          case shift:
            parsStack.push(currentAction.getNumber());
            lookAhead = lexicalAnalyzer.getNextToken();

            break;
          case reduce:
            Rule rule = rules.get(currentAction.getNumber());
            for (int i = 0; i < rule.getRHS().size(); i++) {
              parsStack.pop();
            }


            parsStack.push(parseTable.getGotoTable(parsStack.peek(), rule.getLHS()));
//                        Log.print("");
            try {
              cgf.semanticFunction(rule, lookAhead);
            } catch (Exception e) {
            }
            break;
          case accept:
            finish = true;
            break;
        }

      } catch (Exception ignored) {

        ignored.printStackTrace();
//                boolean find = false;
//                for (NonTerminal t : NonTerminal.values()) {
//                    if (parseTable.getGotoTable(parsStack.peek(), t) != -1) {
//                        find = true;
//                        parsStack.push(parseTable.getGotoTable(parsStack.peek(), t));
//                        StringBuilder tokenFollow = new StringBuilder();
//                        tokenFollow.append(String.format("|(?<%s>%s)", t.name(), t.pattern));
//                        Matcher matcher = Pattern.compile(tokenFollow.substring(1)).matcher(lookAhead.toString());
//                        while (!matcher.find()) {
//                            lookAhead = lexicalAnalyzer.getNextToken();
//                        }
//                    }
//                }
//                if (!find)
//                    parsStack.pop();
      }


    }
    if (!ErrorHandler.isHasError())
      cgf.printMemory();


  }


}
