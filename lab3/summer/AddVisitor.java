
public class AddVisitor extends SummerBaseVisitor<Integer> {
    @Override
    public Integer visitRoot(SummerParser.RootContext ctx)
    {
        Integer val = visit(ctx.expr(0)) + visit(ctx.expr(1));
        System.out.println(ctx.ADD().getText() + val);
        return 0;
    }
    @Override
    public Integer visitExprSum(SummerParser.ExprSumContext ctx)
    {
        return visit(ctx.expr(0)) + visit(ctx.expr(1));
    }
    @Override
    public Integer visitExprNum(SummerParser.ExprNumContext ctx)
    {
        return Integer.valueOf(ctx.NUM().getText());
    }
}
