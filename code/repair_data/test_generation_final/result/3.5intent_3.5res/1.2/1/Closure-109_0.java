private Node parseContextTypeExpression(JsDocToken token) {
    Node typeNameNode = parseTypeName(token);
    if (typeNameNode != null && typeNameNode.getType() == Node.CONSTRUCTOR) {
        typeNameNode.setType(Node.CONSTRUCTOR);
    }
    return typeNameNode;
}