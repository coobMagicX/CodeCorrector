protected void declareNameInScope(FlowScope scope, Node node, JSType type) {
  switch (node.getType()) {
    case Token.NAME:
      scope.inferSlotType(node.getString(), type);
      break;

    case Token.THIS: // Updated the case to handle "this" references
      if (scope.getThisType() != null) { // Check for a valid "this" object in scope
        type = scope.getThisType(); // Refine type based on "this"
      } else {
        throw new IllegalArgumentException("No 'this' object in scope. \n" +
            node.toStringTree());
      }
      break;

    case Token.GETPROP:
      String qualifiedName = node.getQualifiedName();
      Preconditions.checkNotNull(qualifiedName);

      JSType origType = node.getJSType();
      origType = origType == null ? getNativeType(UNKNOWN_TYPE) : origType;
      scope.inferQualifiedSlot(node, qualifiedName, origType, type);
      break;

    default:
      throw new IllegalArgumentException("Node cannot be refined. \n" +
          node.toStringTree());
  }
}