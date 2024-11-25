private Node parseContextTypeExpression(JsDocToken token) {
    if (token.getType().equals("function")) {
        // Check for 'new' keyword followed by a type name
        String value = token.getValue();
        int index = value.indexOf("(");
        if (index != -1 && value.startsWith("function (new:", index)) {
            // Extract the type name from the function signature
            int endIndex = value.indexOf(")", index);
            return parseTypeName(token, value.substring(index + 9, endIndex));
        } else {
            // Handle constructor functions without 'new' keyword
            if (value.toLowerCase().startsWith("function ")) {
                return parseTypeName(token, "constructor");
            }
        }
    }
    return parseTypeName(token);
}