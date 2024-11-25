private boolean areNodesEqualForInlining(Node lhs, Node rhs) {
    // implementation here...
}

// ...

if (name1.hasChildren() &&
        maybeName2.isName() &&
        name1.getString().equals(maybeName2.getString())) {

    Node thenExpr = name1.removeChildren();
    Node elseExpr = elseAssign.getLastChild().detachFromParent();

    cond.detachFromParent();

    // Extracted method to create the hook node
    Node hookNode = createHookNode(cond, thenExpr, elseExpr);

    var.detachFromParent();

    name1.addChildrenToBack(hookNode);
    parent.replaceChild(n, var);
    reportCodeChange();
    return var;
}

private Node createHookNode(Node cond, Node thenExpr, Node elseExpr) {
    // implementation here...
}