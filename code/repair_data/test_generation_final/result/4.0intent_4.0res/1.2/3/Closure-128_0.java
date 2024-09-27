void addList(Node firstInList, boolean isArrayOrFunctionArgument, Context lhsContext) {
    for (Node n = firstInList; n != null; n = n.getNext()) {
        boolean isFirst = n == firstInList;
        if (isFirst) {
            addExpr(n, isArrayOrFunctionArgument ? 1 : 0, lhsContext);
        } else {
            cc.listSeparator();
            addExpr(n, isArrayOrFunctionArgument ? 1 : 0, Context.OTHER);
        }
    }
}