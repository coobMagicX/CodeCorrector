private static String extractClassNameIfGoog(Node node, Node parent, String functionName) {
    if(NodeUtil.isExprCall(parent)) {
        Node callee = node.getFirstChild();
        if (callee != null && callee.isGetProp()) {
            if ("goog.require".equals(functionName) || "goog.provide".equals(functionName)) {
                // For goog.require and goog.provide, we do not expect a class name
                return null;
            }
            Node getPropNode = callee.getFirstChild();
            if (getPropNode != null && getPropNode.getNext() != null) {
                Node classNameNode = getPropNode.getNext();
                if (classNameNode.isString()) {
                    return classNameNode.getString();
                }
            }
        }
    }
    return null;
}
