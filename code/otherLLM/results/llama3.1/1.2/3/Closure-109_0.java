private Node parseContextTypeExpression(JsDocToken token) {
    // Check if the token is an unknown type
    if (token.getValue().startsWith("?")) {
        // If it's an unknown type, create a new node to represent it
        return new UnknownNode(token);
    }

    // Distinguish between constructors and nominal constructors
    if (token.getValue().startsWith("function ")) {
        // If it's a constructor, parse its parameters
        Node constructorParams = parseConstructorParams(token);
        return new ConstructorNode(constructorParams);
    } else if (token.getValue().startsWith("class ")) {
        // If it's a nominal constructor, parse its class definition
        Node classDefinition = parseClassDefinition(token);
        return new NominalConstructorNode(classDefinition);
    }

    // If none of the above conditions are met, just parse the type name
    return parseTypeName(token);
}