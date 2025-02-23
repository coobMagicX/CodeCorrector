private static String extractClassNameIfGoog(Node node, Node parent, String functionName) {
    String className = null;
    if (NodeUtil.isExprCall(parent)) {
        Node callee = node.getFirstChild();
        if (callee != null && callee.getType() == Token.GETPROP) {
            String qualifiedName = callee.getQualifiedName();
            if (functionName.equals("goog.require") || functionName.equals("goog.provide")) {
                // If the function is goog.require or goog.provide, we should not extract a class name
                return null;
            } else if (functionName.equals(qualifiedName)) {
                Node target = callee.getNext();
                if (target != null) {
                    className = target.getString();
                }
            }
        }
    }
    return className;
}
