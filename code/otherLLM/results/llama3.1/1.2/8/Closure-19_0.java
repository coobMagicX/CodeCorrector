protected void declareNameInScope(FlowScope scope, Node node, JSType type) {
  switch (node.getType()) {
    case Token.NAME:
      scope.inferSlotType(node.getString(), type);
      break;

    case Token.GETPROP:
      String qualifiedName = node.getQualifiedName();
      Preconditions.checkNotNull(qualifiedName);

      if ("this".equals(qualifiedName)) { // Add support for "this" references
        JSType origType = getNativeType(OBJECT_TYPE);
        scope.inferQualifiedSlot(node, qualifiedName, origType, type);
      } else {
        JSType origType = node.getJSType();
        origType = origType == null ? getNativeType(UNKNOWN_TYPE) : origType;
        scope.inferQualifiedSlot(node, qualifiedName, origType, type);
      }
      break;

    default:
      throw new IllegalArgumentException("Node cannot be refined. \n" +
          node.toStringTree());
  }
}