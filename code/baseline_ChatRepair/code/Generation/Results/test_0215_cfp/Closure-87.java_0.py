private boolean isFoldableExprBlock(Node n) {
    if (n.getType() == Token.BLOCK && n.hasOneChild()) {
        Node maybeExpr = n.getFirstChild();
        Node parent = n.getParent();

        if (NodeUtil.isExpressionNode(maybeExpr)) {
            // Check if the parent of the block is not an IF node, 
            // ensuring that folding does not affect construct behavior
            // that expects a block (like IF, WHILE, FOR).
            if (parent != null &&
                (parent.isIf() || parent.isWhile() ||
                 parent.isFor() || parent.isSwitch())) {
                return false;
            }
            return true;
        }
    } 
    return false;
}
