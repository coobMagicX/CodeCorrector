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
      if (node.getString().equals("this")) {
        scope.inferSlotType("this", typeRegistry.getThisType());
      } else {
        scope.inferQualifiedSlot(node, qualifiedName, origType, type);
      }
      break;

    case Token.THIS:
      scope.inferSlotType("this", typeRegistry.getThisType());
      break;

    default:
      throw new IllegalArgumentException("Node cannot be refined. \n" +
          node.toStringTree());
  }
}