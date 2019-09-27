import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Sintax extends GrammarBaseVisitor<Integer> {

    //Hashmap that will store the ids for vars and funcs
    HashMap<String, String> ids = new HashMap<String, String>();

    //Hashmap that will store the params for funcs
    HashMap<String, List<String>> funcParams = new HashMap<String, List<String>>();

    //List for local params used in funcs
    List<String> params = null;

    //Variable that will store the numbers of params used in a function call
    int numParams = 0;

    @Override
    public Integer visitStartBegin(GrammarParser.StartBeginContext ctx) {
        visit(ctx.begin());
        return 0;
    }

    @Override public Integer visitBeginFunc(GrammarParser.BeginFuncContext ctx) {
        //Visit a func
        visit(ctx.func());
        return 0;
    }

    @Override public Integer visitBeginBegin(GrammarParser.BeginBeginContext ctx) {
        visit(ctx.begin(0));
        visit(ctx.begin(1));
        return 0;
    }

    @Override public Integer visitBeginVar(GrammarParser.BeginVarContext ctx) {
        //Visit the var
        visit(ctx.var());
        return 0;
    }

    @Override public Integer visitVar(GrammarParser.VarContext ctx) {

        //Verify if this id is already in the vars and funcs ids hashmap
        if(!ids.containsKey(ctx.ID().toString())) {
            ids.put(ctx.ID().toString(), "var");
        } else {
            System.out.println("Symbol already declared: " + ctx.ID().toString());
        }

        visit(ctx.expr());
        return 0;
    }

    @Override public Integer visitFunc(GrammarParser.FuncContext ctx) {

        //Verify if this id is already in the vars and funcs ids hashmap
        if(!ids.containsKey(ctx.ID().toString())) {
            ids.put(ctx.ID().toString(), "func");
        } else {
            System.out.println("Symbol already declared: " + ctx.ID().toString());
        }

        params = new ArrayList<String>();
        visit(ctx.params());
        visit(ctx.expr());
        funcParams.put(ctx.ID().toString(), params);
        params = null;
        return 0;
    }

    @Override public Integer visitExprParen(GrammarParser.ExprParenContext ctx) {
        visit(ctx.expr());
        return 0;
    }

    @Override public Integer visitExprNum(GrammarParser.ExprNumContext ctx) {
        return 0;
    }

    @Override public Integer visitExprMult(GrammarParser.ExprMultContext ctx) {
        visit(ctx.expr(0));
        visit(ctx.expr(1));
        return 0;
    }

    @Override public Integer visitExprSum(GrammarParser.ExprSumContext ctx) {
        visit(ctx.expr(0));
        visit(ctx.expr(1));
        return 0;
    }

    @Override public Integer visitExprMinus(GrammarParser.ExprMinusContext ctx) {
        visit(ctx.expr(0));
        visit(ctx.expr(1));
        return 0;
    }

    @Override public Integer visitExprFunc(GrammarParser.ExprFuncContext ctx) {

        //Verify if the id is already declared, and if yes, if this id is a func
        if(ids.containsKey(ctx.ID().toString())) {
            if(!ids.get(ctx.ID().toString()).equals("func")) {
                System.out.println("Bad used symbol: " + ctx.ID().toString());
                return 0;
            }
        } else {
            System.out.println("Symbol undeclared: " + ctx.ID().toString());
            return 0;
        }

        numParams = 0;
        visit(ctx.exprparams());

        //If the number of params called was different of the number of params that the function expect, show an error
        //message
        if(funcParams.get(ctx.ID().toString()).size() != numParams) {
            System.out.println("Bad argument count: " + ctx.ID().toString());
        }
        return 0;
    }

    @Override public Integer visitExprID(GrammarParser.ExprIDContext ctx) {

        //Verify if the id is inside a list of params (local variables for funcs)
        if(params != null) {
            if(params.contains(ctx.ID().toString())) {
               return 0;
            }
        }
        //Verify if the id used was declared before, if yes, verify if this is a var, not a function
        if(ids.containsKey(ctx.ID().toString())) {
            if(!ids.get(ctx.ID().toString()).equals("var")) {
                System.out.println("Bad used symbol: " + ctx.ID().toString());
            }
        } else {
            System.out.println("Symbol undeclared: " + ctx.ID().toString());
        }
        return 0;
    }

    @Override public Integer visitExprDiv(GrammarParser.ExprDivContext ctx) {
        visit(ctx.expr(0));
        visit(ctx.expr(1));
        return 0;
    }

    @Override public Integer visitParamsNUM(GrammarParser.ParamsNUMContext ctx) { return 0; }

    @Override public Integer visitParamsID(GrammarParser.ParamsIDContext ctx) {
        params.add(ctx.ID().toString());
        return 0;
    }

    @Override public Integer visitVariousParams(GrammarParser.VariousParamsContext ctx) {
        visit(ctx.params(0));
        visit(ctx.params(1));
        return 0;
    }

    @Override public Integer visitExprParamsNUM(GrammarParser.ExprParamsNUMContext ctx) {
        numParams++;
        return 0;
    }

    @Override public Integer visitExprParamsID(GrammarParser.ExprParamsIDContext ctx) {

        //Verify if the id is inside a list of params (local variables for funcs)
        if(params != null) {
            if(params.contains(ctx.ID().toString())) {
                numParams++;
                return 0;
            }
        }
        //Verify if the id used was declared before, if yes, verify if this is a var, not a function
        if(ids.containsKey(ctx.ID().toString())) {
            if(!ids.get(ctx.ID().toString()).equals("var")) {
                System.out.println("Bad used symbol: " + ctx.ID().toString());
            }
        } else {
            System.out.println("Symbol undeclared: " + ctx.ID().toString());
        }
        numParams++;
        return 0;
    }

    @Override public Integer visitVariousExprParams(GrammarParser.VariousExprParamsContext ctx) {
        visit(ctx.exprparams(0));
        visit(ctx.exprparams(1));
        return 0;
    }

}
