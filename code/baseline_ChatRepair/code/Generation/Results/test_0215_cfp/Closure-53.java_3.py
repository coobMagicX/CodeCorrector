private void replaceAssignmentExpression(Var v, Reference ref, Map<String, String> varmap) {
    List<Node> nodes = new ArrayList<>();
    Node val = ref.getAssignedValue();
    blacklistVarReferencesInTree(val, v.scope);
    
    Preconditions.checkState(val.isObjectLit(), "Value must be an object literal");

    // Collecting all necessary assignments
    for (Node key = val.getFirstChild(); key != null; key = key.getNext()) {
        String var = key.getString();
        Node value = key.getFirstChild().detach();
        nodes.add(new Node(Token.ASSIGN, Node.newString(Token.NAME, varmap.getOrDefault(var, var)), value));
    }

    // Adding assignments for remaining var keys with undefined values
    varmap.keySet().stream()
        .filter(var -> !val.hasChildrenWithName(var)) // Filter out already processed keys
        .forEach(var -> nodes.add(new Node(Token.ASSIGN, Node.newString(Token.NAME, varmap.get(var)), NodeUtil.newUndefinedNode(null))));

    // Making sure the expression evaluates to an expected value like 'true' 
    nodes.add(Node.newNode(Token.TRUE));

    // Create COMMA expressions
    if (nodes.size() > 1) {
        Node commaExpr = new Node(Token.COMMA);
        Node current = commaExpr;
        
        for (int i = 0; i < nodes.size() - 1; i++) {
            Node nextComma = new Node(Token.COMMA);
            current.addChildToFront(nodes.get(i));
            current.addChildToBack(nextComma);
            current = nextComma;
        }
        current.addChildToFront(nodes.get(nodes.size() - 1));

        nodes.clear();
        nodes.add(commaExpr);
    }

    Node replacement = nodes.get(0);
    replacement.copyInformationFromForTree(ref.getParent());

    // Replace the old tree node with the new one
    Node parent = ref.getParent();
    if (parent != null && parent.getType() == Token.VAR) {
        parent.getParent().replaceChild(parent, new Node(Token.EXPR_RESULT, replacement));
    } else if (parent != null) {
        parent.replaceChild(ref.getNode(), replacement);
    }
}
