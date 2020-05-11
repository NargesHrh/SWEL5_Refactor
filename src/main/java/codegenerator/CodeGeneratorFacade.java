package codegenerator;

import parser.Rule;
import scanner.token.Token;

public class CodeGeneratorFacade {

    CodeGenerator cg;

    public CodeGeneratorFacade() {
        cg = new CodeGenerator();
    }

    public void printMemory(){
        cg.printMemory();
    }

    public void semanticFunction(Rule rule, Token next) {
        cg.semanticFunction(rule.getSemanticAction(), next);
    }
}
