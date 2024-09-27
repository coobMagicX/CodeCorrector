private boolean isQualifiedNameInferred(
    String qName, Node n, JSDocInfo info,
    Node rhsValue, JSType valueType) {
  if (valueType == null) {
    return true;  // Assume inference when no type information is available.
  }

  // Prototypes of constructors and interfaces are always declared.
  if (qName != null && qName.endsWith(".prototype")) {
    // Check for type mismatches for .prototype assignments
    boolean isPrototypeTypeCorrect = valueType.isObject() || valueType.isFunctionType();
    return !isPrototypeTypeCorrect;
  }

  boolean inferred = true;
  if (info != null) {
    inferred = !(info.hasType()
        || info.hasEnumParameterType()
        || (isConstantSymbol(info, n) && valueType != null
            && !valueType.isUnknownType())
        || FunctionTypeBuilder.isFunctionTypeDeclaration(info));
  }

  if (inferred && rhsValue != null && rhsValue.isFunction()) {
    if (info != null) {
      return false;  // Type explicitly declared in JSDoc
    } else if (!scope.isDeclared(qName, false) &&
        n.isUnscopedQualifiedName()) {
      // Check if this is in a conditional block.
      // Functions assigned in conditional blocks are inferred.
      for (Node current = n.getParent();
           !(current.isScript() || current.isFunction());
           current = current.getParent()) {
        if (NodeUtil.isControlStructure(current)) {
          return true;  // Inference confirmed in conditional block.
        }
      }

      // Check if this is assigned in an inner scope.
      // Functions assigned in inner scopes are inferred.
      AstFunctionContents contents =
          getFunctionAnalysisResults(scope.getRootNode());
      if (contents == null ||
          !contents.getEscapedQualifiedNames().contains(qName)) {
        return false;  // Not in inner scope, not inferred.
      }
    }
  }
  return inferred;
}