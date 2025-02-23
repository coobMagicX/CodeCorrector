private void replaceAssignmentExpression(Var v, Reference ref, Map<String, String> varmap) {
    List<Node> nodes = new ArrayList<Node>();
    Node val = ref.getAssignedValue();
    blacklistVarReferencesInTree(val, v.scope);
    
    Preconditions.checkState(val.isObjectLit(), "Value must be an object literal");

    // Assign the relevant variables based on the object literal
    for (Node key = val.getFirstChild(); key != null; key = key.getNext()) {
        String var = key.getString();
        Node value = key.getFirstChild();
        if (value != null) {
            value.detach();
        }
        nodes.add(new Node(Token.ASSIGN, Node.newString(Token.NAME, varmap.get(var)), value));
    }

    // For vars not directly assigned, default to undefined
    for (String var : varmap.keySet()) {
        if (!val.hasChildrenWithName(var)) {
            nodes.add(new Node(Token.ASSIGN, Node.newString(Token.NAME, varmap.get(var)), NodeUtil.newUndefinedNode(null)));
        }
    }

    // Keep the last evaluated value as true
    nodes.add(new Node(Token.TRUE));

    // Building the comma expression tree
    Node replacement = nodes.get(0);
    for (int i = 1; i < nodes.size(); i++) {
        replacement = new Node(Token.COMMA, replacement, nodes.get(i));
    }

    replacement.copyInformationFromForTree(ref.getParent());

    // Replace in the AST
    Node replace = ref.getParent();
    if (replace.getType() == Token.VAR) {
        replace.getParent().replaceChild(replace, new Node(Token.EXPR_RESULT, replacement));
    } else {
        replace.getParent().replaceChild(replace, replacement);
    }
}
