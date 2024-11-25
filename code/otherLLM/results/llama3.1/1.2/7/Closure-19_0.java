protected void declareNameInScope(FlowScope scope, Node node, JSType type) {
  switch (node.getType()) {
    case Token.NAME:
      if (!node.getString().equals("this")) { // Add this check to prevent incorrect inference of "this" references
        scope.inferSlotType(node.getString(), type);
      } else {
        // Handle "this" references separately, as they are not currently modeled in the CFG.
        // For now, just ignore them and let other methods handle them if necessary.
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