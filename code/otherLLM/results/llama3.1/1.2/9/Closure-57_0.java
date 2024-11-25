private static String extractClassNameIfGoog(Node node, Node parent,
String functionName){
  String className = null;
  
  // Input validation: Check if functionName is not empty
  if (!functionName.isEmpty()) {
    if (NodeUtil.isExprCall(parent)) {
      Node callee = node.getFirstChild();
      if (callee != null && callee.getType() == Token.GETPROP) {
        String qualifiedName = callee.getQualifiedName();
        
        // Input validation: Check if functionName matches the qualified name
        if (functionName.equals(qualifiedName)) {
          Node target = callee.getNext();
          if (target != null) {
            className = target.getString();
          }
        }
      }
    }
  } else {
    // Handle error: Return null or a default value when functionName is empty
    return null;
  }
  
  return className;
}