case Token.HOOK: {
    Preconditions.checkState(childCount == 3);
    int p = NodeUtil.precedence(type);
    Context rhsContext = Context.OTHER;
    addExpr(first, p + 1, context);
    cc.addOp("?", true);
    addExpr(first.getNext(), 1, rhsContext);
    cc.addOp(":", true);
    addExpr(last, 1, rhsContext);
    break;
}