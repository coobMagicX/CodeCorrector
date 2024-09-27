private void replaceAssignmentExpression(Var v, Reference ref,
                                         Map<String, String> varmap) {
    // Compute all of the assignments necessary
    List<Node> nodes = new ArrayList<>();
    Node val = ref.getAssignedValue();
    blacklistVarReferencesInTree(val, v.scope);
    Preconditions.checkState(val.getType() == Token.OBJECTLIT);
    Set<String> all = new LinkedHashSet<>(varmap.keySet());
    for (Node key = val.getFirstChild(); key != null; key = key.getNext()) {
        String var = key.getString();
        Node value = key.getFirstChild().detach();
        // TODO(user): Copy type information.
        nodes.add(new Node(Token.ASSIGN, Node.newString(Token.NAME, varmap.get(var)), value));
        all.remove(var);
    }

    // TODO(user): Better source information.
    for (String var : all) {
        nodes.add(new Node(Token.ASSIGN, Node.newString(Token.NAME, varmap.get(var)), NodeUtil.newUndefinedNode(null)));
    }

    Node replacement;

    // All assignments evaluate to true, so make sure that the expr statement evaluates to true in case it matters.
    nodes.add(new Node(Token.TRUE));

    // Join these using COMMA. A COMMA node must have 2 children, so we create a tree.
    Collections.reverse(nodes);
    replacement = new Node(Token.COMMA);
    Node cur = replacement;
    for (int i = 0; i < nodes.size() - 1; i++) {
        cur.addChildToFront(nodes.get(i));
        Node nextComma = new Node(Token.COMMA);
        cur.addChildToFront(nextComma);
        cur = nextComma;
    }
    cur.addChildToFront(nodes.get(nodes.size() - 1));

    Node replace = ref.getParent();
    replacement.copyInformationFromForTree(replace);

    if (replace.getType() == Token.VAR) {
        replace.getParent().replaceChild(replace, NodeUtil.newExpr(replacement));
    } else {
        replace.getParent().replaceChild(replace, replacement);
    }
}