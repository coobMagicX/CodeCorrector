protected void declareNameInScope(FlowScope scope, Node node, JSType type) {
  switch (node.getType()) {
    case Token.NAME:
      String name = node.getString();
      if ("this".equals(name)) {
        // Skip "this" references to avoid modeling it as a valid scope.
        break;
      }
      scope.inferSlotType(node.getString(), type);
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