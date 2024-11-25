protected void declareNameInScope(FlowScope scope, Node node, JSType type) {
  switch (node.getType()) {
    case Token.NAME:
      scope.inferSlotType(node.getString(), type);
      break;

    case Token.GETPROP:
      String qualifiedName = node.getQualifiedName();
      Preconditions.checkNotNull(qualifiedName);

      JSType origType = node.getJSType();
      origType = origType == null ? getNativeType(UNKNOWN_TYPE) : origType;
      scope.inferQualifiedSlot(node, qualifiedName, origType, type);
      break;

    case Token.THIS: // Assuming Token.THIS is the token type for "this" references
      // Handle "this" reference appropriately (e.g., infer as type 'this')
      JSType thisType = getThisType(); // This is a hypothetical method to represent the type of 'this'
      scope.inferSlotType(node.getString(), thisType);
      break;

    default:
      // For all other cases that are not "Token.NAME" or "Token.GETPROP",
      // ensure that we do not throw an exception for "this" references.
      if (node instanceof ThisExpression) {
        JSType thisType = getThisType(); // This is a hypothetical method to represent the type of 'this'
        scope.inferSlotType(node.getString(), thisType);
      } else {
        throw new IllegalArgumentException("Node cannot be refined. \n" +
            node.toStringTree());
      }
  }
}