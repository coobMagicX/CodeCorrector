protected void declareNameInScope(FlowScope scope, Node node, JSType type) {
    switch (node.getType()) {
        case Token.NAME:
            scope.inferSlotType(node.getString(), type);
            break;

        case Token.GETPROP:
            String qualifiedName = node.getQualifiedName();
            Preconditions.checkNotNull(qualifiedName, "QualifiedName cannot be null");

            JSType origType = node.getJSType();
            origType = origType == null ? getNativeType(JSTypeNative.UNKNOWN_TYPE) : origType;
            scope.inferQualifiedSlot(node, qualifiedName, origType, type);
            break;

        default:
            throw new IllegalArgumentException("Node type cannot be refined: " + node.toStringTree());
    }
}