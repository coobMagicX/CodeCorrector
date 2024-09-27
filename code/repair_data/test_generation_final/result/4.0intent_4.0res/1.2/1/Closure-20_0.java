private Node tryFoldSimpleFunctionCall(Node n) {
    Preconditions.checkState(n.isCall());
    Node callTarget = n.getFirstChild();
    if (callTarget != null && callTarget.isName() &&
        callTarget.getString().equals("String")) {
        // We need to ensure there is exactly one argument to fold String(a) to '' + a
        if (callTarget.getNext() != null && callTarget.getNext().getNext() == null) {
            Node value = callTarget.getNext();
            Node addition = IR.add(
                IR.string("").srcref(callTarget),
                value.detachFromParent());
            n.getParent().replaceChild(n, addition);
            reportCodeChange();
            return addition;
        }
    }
    return n;
}