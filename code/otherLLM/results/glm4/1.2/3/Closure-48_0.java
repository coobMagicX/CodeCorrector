Node functionNode = ...; // A node representing a function.
String functionName = ...; // The name of the function.
JSDocInfo jsDocInfo = ...; // JSDoc information about the function.
Node lvalueNode = ...; // The left-hand side node (the variable being assigned).

FunctionType functionType = createFunctionTypeFromNodes(
    functionNode, functionName, jsDocInfo, lvalueNode);