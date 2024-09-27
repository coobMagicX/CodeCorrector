private static String extractClassNameIfGoog(Node node, Node parent, String functionName) {
    String className = null;
    if (NodeUtil.isExprCall(parent)) {
        Node callee = parent.getFirstChild();  // parent's first child should be the callee for an expression call
        if (callee != null && callee.getType() == Token.GETPROP) {
            String qualifiedName = callee.getQualifiedName();
            if (functionName.equals(qualifiedName)) {
                Node target = callee.getFirstChild();  // The target should be the first child of the callee
                if (target != null) {
                    className = target.getString();
                }
            }
        }
    }
    return className;
}