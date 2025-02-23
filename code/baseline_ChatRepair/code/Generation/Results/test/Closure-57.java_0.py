private static String extractClassNameIfGoog(Node node, Node parent, String functionName) {
    String className = null;
    if (NodeUtil.isExprCall(parent)) {
        Node callee = node.getFirstChild();
        if (callee != null && callee.getType() == Token.GETPROP) {
            String qualifiedName = callee.getQualifiedName();
            if (functionName.equals(qualifiedName)) {
                Node firstArg = callee.getNext();
                if (firstArg != null && isClassNameFunction(functionName)) {
                    className = firstArg.getString();
                }
            }
        }
    }
    return className;
}

private static boolean isClassNameFunction(String functionName) {
    // Add other function names here if needed
    return "goog.provide".equals(functionName) || "goog.module".equals(functionName);
}
