import org.antlr.v4.runtime.tree.pattern.ParseTreePatternMatcher;

import java.util.*;

public class Semantic extends GrammarBaseVisitor<String> {

    //Hashmap with all of local variables in a func
    Map<String, String> exprFunc = new HashMap<String, String>();

    //Number of local variables used in a func
    int localVariables = 1;

    //Hashmap with all params of a func
    List<String> funcParams = new ArrayList<String>();;

    @Override public String visitRootComponent(GrammarParser.RootComponentContext ctx) {
        visit(ctx.component());
        return null;
    }

    @Override public String visitComponentVar(GrammarParser.ComponentVarContext ctx) {
        return null;
    }

    @Override public String visitComponentComponent(GrammarParser.ComponentComponentContext ctx) {
        visit(ctx.component(0));
//        visit(ctx.component(1));
        return null;
    }

    @Override public String visitComponentFunc(GrammarParser.ComponentFuncContext ctx) {
        visit(ctx.func());
        return null;
    }

    @Override public String visitVar(GrammarParser.VarContext ctx) {
        return null;
    }

    @Override public String visitFunc(GrammarParser.FuncContext ctx) {

        //Get all the params of the function and then transform them in a string
        visit(ctx.params());
        String params = funcParams.toString().substring(1, funcParams.toString().length() - 1);

        //Visit the expression of the function
        visit(ctx.expr());

        //Print all operations inside a function
        System.out.println("define i32 @" + ctx.ID().toString() + "(i32 "+ params + ") {");
        Iterator funcs = exprFunc.entrySet().iterator();
        while (funcs.hasNext()) {
            Map.Entry element = (Map.Entry)funcs.next();
            System.out.println("    " + element.getKey() + " = " + element.getValue());
        }
        System.out.println("    " + "ret i32 %" + (localVariables - 1));
        System.out.println("}");
        exprFunc = new HashMap<String, String>();
        localVariables = 1;

        return null;
    }

    @Override public String visitExprSum(GrammarParser.ExprSumContext ctx) {
        String expr = visit(ctx.expr());
        String prior = visit(ctx.prior());
        exprFunc.put("%" + localVariables, "add i32 " + expr + ", " + prior);

        return "%" + localVariables++;
    }

    @Override public String visitExprMinus(GrammarParser.ExprMinusContext ctx) { return null; }

    @Override public String visitExprPrior(GrammarParser.ExprPriorContext ctx) {
        return visit(ctx.prior());
    }

    @Override public String visitPriorDiv(GrammarParser.PriorDivContext ctx) {
        String prior = visit(ctx.prior());
        String terminal = visit(ctx.terminal());
        exprFunc.put("%" + localVariables, "sdiv i32 " + prior + ", " + terminal);

        return "%" + localVariables++;
    }

    @Override public String visitPriorMult(GrammarParser.PriorMultContext ctx) {
        String prior = visit(ctx.prior());
        String terminal = visit(ctx.terminal());
        exprFunc.put("%" + localVariables, "mul i32 " + prior + ", " + terminal);

        return "%" + localVariables++;
    }

    @Override public String visitPriorTerminal(GrammarParser.PriorTerminalContext ctx) {
        return visit(ctx.terminal());
    }

    @Override public String visitTerminalFunc(GrammarParser.TerminalFuncContext ctx) { return null; }

    @Override public String visitTerminalNum(GrammarParser.TerminalNumContext ctx) {
        return ctx.NUM().toString();
    }

    @Override public String visitTerminalID(GrammarParser.TerminalIDContext ctx) {
        return "%" + ctx.ID().toString();
    }

    @Override public String visitTerminalParen(GrammarParser.TerminalParenContext ctx) {
        return visit(ctx.expr());
    }

    @Override public String visitParamsNUM(GrammarParser.ParamsNUMContext ctx) { return null; }

    @Override public String visitParamsID(GrammarParser.ParamsIDContext ctx) {
        funcParams.add("%" + ctx.ID().toString());
        return null;
    }

    @Override public String visitVariousParams(GrammarParser.VariousParamsContext ctx) {
        visit(ctx.params(0));
        visit(ctx.params(1));
        return null;
    }

    @Override public String visitExprParamsNUM(GrammarParser.ExprParamsNUMContext ctx) { return null; }

    @Override public String visitExprParamsID(GrammarParser.ExprParamsIDContext ctx) { return null; }

    @Override public String visitVariousExprParams(GrammarParser.VariousExprParamsContext ctx) { return null; }

}
