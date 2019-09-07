import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class MyParser {
    public static void main(String[] args) throws Exception {
        // create a CharStream that reads from standard input
        CharStream input = CharStreams.fromStream(System.in);
        // create a lexer that feeds off of input CharStream
        SummerLexer lexer = new SummerLexer(input);
        // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // create a parser that feeds off the tokens buffer
        SummerParser parser = new SummerParser(tokens);
        ParseTree tree = parser.root(); // begin parsing at prog rule
        AddVisitor vis = new AddVisitor();
        vis.visit(tree);
    }
}
