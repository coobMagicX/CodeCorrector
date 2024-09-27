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

        case Token.THIS: // Handling 'this' references as per the fix strategy
            // Assuming 'this' refers to the type of current context, we need to model it accordingly
            scope.inferSlotType("this", type);
            break;

        default:
            throw new IllegalArgumentException("Node cannot be refined. \n" +
                node.toStringTree());
    }
}