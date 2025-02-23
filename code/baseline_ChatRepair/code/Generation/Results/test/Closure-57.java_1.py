private static String extractClassNameIfGoog(Node node, Node parent, String functionName) {
    String className = null;
    if (NodeUtil.isExprCall(parent)) {
        Node callee = node.getFirstChild();
        if (callee != null && callee.getType() == Token.GETPROP) {
            String qualifiedName = callee.getQualifiedName();
            if (functionName.equals(qualifiedName)) {
                Node target = callee.getNext();
                if (target != null && isClassNameTargetFunction(qualifiedName)) {
                    className = target.getString();
                }
            }
        }
    }
    return className;
}

private static boolean isClassNameTargetFunction(String qualifiedName) {
    return "goog.provide".equals(qualifiedName) || "goog.module".equals(qualifiedName);
}
