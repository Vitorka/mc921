import org.antlr.v4.runtime.tree.pattern.ParseTreePatternMatcher;

import java.util.*;

public class Semantic extends GrammarBaseVisitor<String> {

    //Hashmap with all of local variables in a func
    Map<String, String> exprFunc = new LinkedHashMap<String, String>();

    //Number of local variables used in a func
    int localID = 1;

    //List with all params of a func
    List<String> funcParams = new ArrayList<String>();

    //List with all local variables
    List<String> localVariables = new ArrayList<String>();

    //Hashmap with all the global variables and its initializer functions
    Map<String, String> globalVariables = new LinkedHashMap<String, String>();

    @Override public String visitRootComponent(GrammarParser.RootComponentContext ctx) {
        visit(ctx.component());

        System.out.println("define void @" + "initializer" + "() {");
        Iterator funcs = globalVariables.entrySet().iterator();
        while (funcs.hasNext()) {
            Map.Entry element = (Map.Entry)funcs.next();
            System.out.println("    " + "call void @" + element.getValue() + "()");
        }
        System.out.println("    " + "ret void");
        System.out.println("}");
        return null;
    }

    @Override public String visitComponentVar(GrammarParser.ComponentVarContext ctx) {
        visit(ctx.var());
        return null;
    }

    @Override public String visitComponentComponent(GrammarParser.ComponentComponentContext ctx) {
        visit(ctx.component(0));
        visit(ctx.component(1));
        return null;
    }

    @Override public String visitComponentFunc(GrammarParser.ComponentFuncContext ctx) {
        visit(ctx.func());
        return null;
    }

    @Override public String visitVar(GrammarParser.VarContext ctx) {
        String lastVariable = visit(ctx.expr());

        globalVariables.put(ctx.ID().toString(), ctx.ID().toString() + "_initializer");
        System.out.println("@" + ctx.ID().toString() + " = global i32 0;");
        System.out.println("define void @" + ctx.ID().toString() + "_initializer" + "() {");
        TreeMap<String, String> hashSorted = new TreeMap<>();
        hashSorted.putAll(exprFunc);
        Iterator funcs = hashSorted.entrySet().iterator();
        while (funcs.hasNext()) {
            Map.Entry element = (Map.Entry)funcs.next();
            System.out.println("    " + element.getKey() + " = " + element.getValue());
        }

        //Expr is a number
        if(exprFunc.isEmpty()) {
            System.out.println("    " + "store i32 " + lastVariable + ", i32* @" + ctx.ID().toString());
        } else {
            System.out.println("    " + "store i32 %" + (localID - 1) + ", i32* @" + ctx.ID().toString());
        }
        System.out.println("    " + "ret void");
        System.out.println("}");
        exprFunc = new LinkedHashMap<String, String>();
        localVariables = new ArrayList<String>();
        localID = 1;

        return null;
    }

    @Override public String visitFunc(GrammarParser.FuncContext ctx) {

        //Get all the params of the function, put them in a list of local variables and then transform them in a string
        visit(ctx.params());
        localVariables = new ArrayList<String>(funcParams);
        String params = "";
        for (String i : funcParams) {
            params += "i32 " + i + ", ";
        }
        params = params.substring(0, params.length() - 2);

        //Visit the expression of the function
        String lastVariable = visit(ctx.expr());

        //Print all operations inside a function
        TreeMap<String, String> hashSorted = new TreeMap<>();
        hashSorted.putAll(exprFunc);
        System.out.println("define i32 @" + ctx.ID().toString() + "(" + params + ") {");
        Iterator funcs = hashSorted.entrySet().iterator();
        while (funcs.hasNext()) {
            Map.Entry element = (Map.Entry)funcs.next();
            System.out.println("    " + element.getKey() + " = " + element.getValue());
        }

        //If there is only one element in expression
        if(exprFunc.isEmpty()) {
            System.out.println("    " + "ret i32 " + lastVariable);
        } else {
            System.out.println("    " + "ret i32 %" + (localID - 1));
        }
        System.out.println("}");
        exprFunc = new HashMap<String, String>();
        funcParams = new ArrayList<String>();
        localID = 1;

        return null;
    }

    @Override public String visitExprSum(GrammarParser.ExprSumContext ctx) {
        String expr = visit(ctx.expr());
        String prior = visit(ctx.prior());

        //Expr and prior are local variables
        if(localVariables.contains(expr) && localVariables.contains(prior)) {
            exprFunc.put("%" + localID, "add i32 " + expr + ", " + prior);
            localVariables.add("%" + localID);
        }
        //Expr is a local variable or number and prior is a global variable
        else if ((localVariables.contains(expr) || expr.matches("[0-9]+")) && globalVariables.containsKey(prior.substring(1))) {
            exprFunc.put("%" + localID, "load i32, i32* @" + prior.substring(1));
            localVariables.add("%" + localID++);
            exprFunc.put("%" + localID, "add i32 " + expr + ", " + "%" + (localID - 1));
            localVariables.add("%" + localID);
        }
        //Expr is a global variable and prior is a local variable or number
        else if ((localVariables.contains(prior) || prior.matches("[0-9]+")) && globalVariables.containsKey(expr.substring(1))) {
            exprFunc.put("%" + localID, "load i32, i32* @" + expr.substring(1));
            localVariables.add("%" + localID++);
            exprFunc.put("%" + localID, "add i32 " + "%" + (localID - 1) + ", " + prior);
            localVariables.add("%" + localID);
        }
        //Expr and Prior are global variables
        else if (globalVariables.containsKey(expr.substring(1)) && globalVariables.containsKey(prior.substring(1))) {
            exprFunc.put("%" + localID, "load i32, i32* @" + expr.substring(1));
            localVariables.add("%" + localID++);
            exprFunc.put("%" + localID, "load i32, i32* @" + prior.substring(1));
            localVariables.add("%" + localID++);
            exprFunc.put("%" + localID, "add i32 " + "%" + (localID - 2) + ", " + "%" + (localID - 1));
            localVariables.add("%" + localID);
        }
        //Expr is a function
        else if(Character.compare(expr.charAt(0), '@') == 0) {
            exprFunc.put("%" + localID, "call i32 " + expr);
            localVariables.add("%" + localID++);
            exprFunc.put("%" + localID, "add i32 " + "%" + (localID - 1) + ", " + "%" + prior);
            localVariables.add("%" + localID);
        }
        //Prior is a function
        else if(Character.compare(prior.charAt(0), '@') == 0) {
            exprFunc.put("%" + localID, "call i32 " + prior);
            localVariables.add("%" + localID++);
            exprFunc.put("%" + localID, "add i32 " + "%" + expr + ", " + "%" + (localID - 1));
            localVariables.add("%" + localID);
        }
        //Expr and prior are numbers
        else {
            exprFunc.put("%" + localID, "add i32 " + expr + ", " + prior);
            localVariables.add("%" + localID);
        }
        return "%" + localID++;
    }

    @Override public String visitExprMinus(GrammarParser.ExprMinusContext ctx) {
        String expr = visit(ctx.expr());
        String prior = visit(ctx.prior());

        //Expr and prior are local variables
        if(localVariables.contains(expr) && localVariables.contains(prior)) {
            exprFunc.put("%" + localID, "sub i32 " + expr + ", " + prior);
            localVariables.add("%" + localID);
        }
        //Expr is a local variable or number and prior is a global variable
        else if ((localVariables.contains(expr) || expr.matches("[0-9]+")) && globalVariables.containsKey(prior.substring(1))) {
            exprFunc.put("%" + localID, "load i32, i32* @" + prior.substring(1));
            localVariables.add("%" + localID++);
            exprFunc.put("%" + localID, "sub i32 " + expr + ", " + "%" + (localID - 1));
            localVariables.add("%" + localID);
        }
        //Expr is a global variable and prior is a local variable or number
        else if ((localVariables.contains(prior) || prior.matches("[0-9]+")) && globalVariables.containsKey(expr.substring(1))) {
            exprFunc.put("%" + localID, "load i32, i32* @" + expr.substring(1));
            localVariables.add("%" + localID++);
            exprFunc.put("%" + localID, "sub i32 " + "%" + (localID - 1) + ", " + prior);
            localVariables.add("%" + localID);
        }
        //Expr and Prior are global variables
        else if (globalVariables.containsKey(expr.substring(1)) && globalVariables.containsKey(prior.substring(1))) {
            exprFunc.put("%" + localID, "load i32, i32* @" + expr.substring(1));
            localVariables.add("%" + localID++);
            exprFunc.put("%" + localID, "load i32, i32* @" + prior.substring(1));
            localVariables.add("%" + localID++);
            exprFunc.put("%" + localID, "sub i32 " + "%" + (localID - 2) + ", " + "%" + (localID - 1));
            localVariables.add("%" + localID);
        }
        //Expr is a function
        else if(Character.compare(expr.charAt(0), '@') == 0) {
            exprFunc.put("%" + localID, "call i32 " + expr);
            localVariables.add("%" + localID++);
            exprFunc.put("%" + localID, "sub i32 " + "%" + (localID - 1) + ", " + "%" + prior);
            localVariables.add("%" + localID);
        }
        //Prior is a function
        else if(Character.compare(prior.charAt(0), '@') == 0) {
            exprFunc.put("%" + localID, "call i32 " + prior);
            localVariables.add("%" + localID++);
            exprFunc.put("%" + localID, "sub i32 " + expr + ", " + "%" + (localID - 1));
            localVariables.add("%" + localID);
        }
        //Expr and prior are numbers
        else {
            exprFunc.put("%" + localID, "sub i32 " + expr + ", " + prior);
            localVariables.add("%" + localID);
        }
        return "%" + localID++;
    }

    @Override public String visitExprPrior(GrammarParser.ExprPriorContext ctx) {
        String prior = visit(ctx.prior());

        //If prior is a local variable or a number
        if(localVariables.contains(prior) || prior.matches("[0-9]+")) {
            return prior;
        }
        //If prior is a global variable
        else if(globalVariables.containsKey(prior.substring(1))) {
            exprFunc.put("%" + localID, "load i32, i32* @" + prior.substring(1));
            return "%" + localID++;
        }
        //If prior is a function
        else {
            exprFunc.put("%" + localID, "call i32 " + prior);
            return "%" + localID++;
        }
    }

    @Override public String visitPriorDiv(GrammarParser.PriorDivContext ctx) {
        String prior = visit(ctx.prior());
        String terminal = visit(ctx.terminal());

        //Prior and terminal are local variables
        if(localVariables.contains(prior) && localVariables.contains(terminal)) {
            exprFunc.put("%" + localID, "sdiv i32 " + prior + ", " + terminal);
            localVariables.add("%" + localID);
        }
        //Prior is a local variable or number and terminal is a global variable
        else if ((localVariables.contains(prior) || prior.matches("[0-9]+")) && globalVariables.containsKey(terminal.substring(1))) {
            exprFunc.put("%" + localID, "load i32, i32* @" + terminal.substring(1));
            localVariables.add("%" + localID++);
            exprFunc.put("%" + localID, "sdiv i32 " + prior + ", " + "%" + (localID - 1));
            localVariables.add("%" + localID);
        }
        //Prior is a global variable and terminal is a local variable or number
        else if ((localVariables.contains(terminal) || terminal.matches("[0-9]+")) && globalVariables.containsKey(prior.substring(1))) {
            exprFunc.put("%" + localID, "load i32, i32* @" + prior.substring(1));
            localVariables.add("%" + localID++);
            exprFunc.put("%" + localID, "sdiv i32 " + "%" + (localID - 1) + ", " + terminal);
            localVariables.add("%" + localID);
        }
        //Prior and terminal are global variables
        else if (globalVariables.containsKey(prior.substring(1)) && globalVariables.containsKey(terminal.substring(1))) {
            exprFunc.put("%" + localID, "load i32, i32* @" + prior.substring(1));
            localVariables.add("%" + localID++);
            exprFunc.put("%" + localID, "load i32, i32* @" + terminal.substring(1));
            localVariables.add("%" + localID++);
            exprFunc.put("%" + localID, "sdiv i32 " + "%" + (localID - 2) + ", " + "%" + (localID - 1));
            localVariables.add("%" + localID);
        }
        //Prior is a function
        else if(Character.compare(prior.charAt(0), '@') == 0) {
            exprFunc.put("%" + localID, "call i32 " + prior);
            localVariables.add("%" + localID);
        }
        //Terminal is a function
        else if(Character.compare(terminal.charAt(0), '@') == 0) {
            exprFunc.put("%" + localID, "call i32 " + terminal);
            localVariables.add("%" + localID);
        }
        //Prior and terminal are numbers
        else {
            exprFunc.put("%" + localID, "sdiv i32 " + prior + ", " + terminal);
            localVariables.add("%" + localID);
        }
        return "%" + localID++;
    }

    @Override public String visitPriorMult(GrammarParser.PriorMultContext ctx) {
        String prior = visit(ctx.prior());
        String terminal = visit(ctx.terminal());

        //Prior and terminal are local variables
        if(localVariables.contains(prior) && localVariables.contains(terminal)) {
            exprFunc.put("%" + localID, "mul i32 " + prior + ", " + terminal);
            localVariables.add("%" + localID);
        }
        //Prior is a local variable or number and terminal is a global variable
        else if ((localVariables.contains(prior) || prior.matches("[0-9]+")) && globalVariables.containsKey(terminal.substring(1))) {
            exprFunc.put("%" + localID, "load i32, i32* @" + terminal.substring(1));
            localVariables.add("%" + localID++);
            exprFunc.put("%" + localID, "mul i32 " + prior + ", " + "%" + (localID - 1));
            localVariables.add("%" + localID);
        }
        //Prior is a global variable and terminal is a local variable or number
        else if ((localVariables.contains(terminal) || terminal.matches("[0-9]+")) && globalVariables.containsKey(prior.substring(1))) {
            exprFunc.put("%" + localID, "load i32, i32* @" + prior.substring(1));
            localVariables.add("%" + localID++);
            exprFunc.put("%" + localID, "mul i32 " + "%" + (localID - 1) + ", " + terminal);
            localVariables.add("%" + localID);
        }
        //Prior and terminal are global variables
        else if (globalVariables.containsKey(prior.substring(1)) && globalVariables.containsKey(terminal.substring(1))) {
            exprFunc.put("%" + localID, "load i32, i32* @" + prior.substring(1));
            localVariables.add("%" + localID++);
            exprFunc.put("%" + localID, "load i32, i32* @" + terminal.substring(1));
            localVariables.add("%" + localID++);
            exprFunc.put("%" + localID, "mul i32 " + "%" + (localID - 2) + ", " + "%" + (localID - 1));
            localVariables.add("%" + localID);
        }
        //Prior is a function
        else if(Character.compare(prior.charAt(0), '@') == 0) {
            exprFunc.put("%" + localID, "call i32 " + prior);
            localVariables.add("%" + localID);
        }
        //Terminal is a function
        else if(Character.compare(terminal.charAt(0), '@') == 0) {
            exprFunc.put("%" + localID, "call i32 " + terminal);
            localVariables.add("%" + localID);
        }
        //Prior and terminal are numbers
        else {
            exprFunc.put("%" + localID, "mul i32 " + prior + ", " + terminal);
            localVariables.add("%" + localID);
        }
        return "%" + localID++;
    }

    @Override public String visitPriorTerminal(GrammarParser.PriorTerminalContext ctx) {

        return visit(ctx.terminal());
    }

    @Override public String visitTerminalFunc(GrammarParser.TerminalFuncContext ctx) {
        String params = visit(ctx.exprparams());
        return "@" + ctx.ID().toString() + "(" + params + ")";
    }

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

    @Override public String visitExprParamsNUM(GrammarParser.ExprParamsNUMContext ctx) {
        return "i32 " + ctx.NUM().toString();
    }

    @Override public String visitExprParamsID(GrammarParser.ExprParamsIDContext ctx) {

        //If the ID is a local variable
        if(localVariables.contains("%" + ctx.ID().toString())) {
            return "i32 %" + ctx.ID().toString();
        }
        //If the ID is a global variable
        else {
            exprFunc.put("%" + localID, "load i32, i32* @" + ctx.ID().toString());
            return "i32 %" + localID++;
        }
    }

    @Override public String visitVariousExprParams(GrammarParser.VariousExprParamsContext ctx) {
        String param1 = visit(ctx.exprparams(0));
        String param2 = visit(ctx.exprparams(1));
        return param1 + ", " + param2;
    }

}
