protected void declareNameInScope(FlowScope scope, Node node, JSType type) {
  switch (node.getType()) {
    case Token.NAME:
      if ("this".equals(node.getString())) {
        throw new IllegalArgumentException("Cannot infer the type of 'this' in this context.");
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