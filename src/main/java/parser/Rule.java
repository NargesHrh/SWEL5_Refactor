package parser;

import scanner.token.Token;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by mohammad hosein on 6/25/2015.
 */
public class Rule {
    private NonTerminal LHS;
    private List<GrammarSymbol> RHS;
    private int semanticAction;
    public Rule(String stringRule) {
        stringRule = stringIndex(stringRule);
        String[] splited = stringRule.split("->");
        LHS = NonTerminal.valueOf(splited[0]);
        RHS = new ArrayList<GrammarSymbol>();
        stringSplit(splited);
    }

    private void stringSplit(String[] splited){
        if (splited.length > 1) {
            String[] RHSs = splited[1].split(" ");
            for (String s : RHSs){
                try {
                    getRHS().add(new GrammarSymbol(NonTerminal.valueOf(s)));
                } catch (Exception e) {
                    getRHS().add(new GrammarSymbol(new Token(Token.getTyepFormString(s), s)));
                }
            }
        }
    }

    private String stringIndex(String stringRule){
        int index = stringRule.indexOf("#");
        if (index != -1) {
            try {
                semanticAction = Integer.parseInt(stringRule.substring(index + 1));
            }catch (NumberFormatException ex){
                semanticAction = 0;
            }
            return stringRule.substring(0, index);
        } else {
            semanticAction = 0;
            return stringRule;
        }
    }

    public NonTerminal getLHS() {
        return LHS;
    }

    public int getSemanticAction() {
        return semanticAction;
    }

    public List<GrammarSymbol> getRHS() {
        return RHS;
    }
}

class GrammarSymbol{
    private boolean isTerminal;
    private NonTerminal nonTerminal;
    private Token terminal;

    public GrammarSymbol(NonTerminal nonTerminal)
    {
        this.nonTerminal = nonTerminal;
        isTerminal = false;
    }
    public GrammarSymbol(Token terminal)
    {
        this.terminal = terminal;
        isTerminal = true;
    }

    public NonTerminal getNonTerminal() {
        return nonTerminal;
    }

    public Token getTerminal() {
        return terminal;
    }
}