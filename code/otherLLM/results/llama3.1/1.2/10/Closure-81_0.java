Node processFunctionNode(FunctionNode functionNode) {
  Name name = functionNode.getFunctionName();
  Boolean isUnnamedFunction = false;
  if (name == null) {
    name = new Name();
    name.setIdentifier("");
    isUnnamedFunction = true;
  }
  Node node = newNode(Token.FUNCTION);
  Node newName = transform(name);
  if (isUnnamedFunction) {
    // Old Rhino tagged the empty name node with the line number of the
    // declaration.
    newName.setLineno(functionNode.getLineno());
    int lpColumn = functionNode.getAbsolutePosition() +
        functionNode.getLp();
    int lpLine = getLineNumberBeforeToken(functionNode, Token.FUNCTION);
    if (lpLine != -1) {
      newName.setCharno(position2charno(lpColumn));
      lp.setLineno(lpLine);
    }
  }

  node.addChildToBack(newName);
  Node lp = newNode(Token.LP);
  // The left paren's complicated because it's not represented by an
  // AstNode, so there's nothing that has the actual line number that it
  // appeared on.  We know the paren has to appear on the same line as the
  // function name (or else a semicolon will be inserted.)  If there's no
  // function name, assume the paren was on the same line as the function.
  // TODO(bowdidge): Mark line number of paren correctly.
  Name fnName = functionNode.getFunctionName();
  if (fnName != null) {
    lp.setLineno(fnName.getLineno());
  } else {
    int lpLine = getLineNumberBeforeToken(functionNode, Token.FUNCTION);
    if (lpLine != -1) {
      lp.setLineno(lpLine);
    }
  }
  int lparenCharno = functionNode.getLp() +
      functionNode.getAbsolutePosition();

  lp.setCharno(position2charno(lparenCharno));
  for (AstNode param : functionNode.getParams()) {
    lp.addChildToBack(transform(param));
  }
  node.addChildToBack(lp);

  Node bodyNode = transform(functionNode.getBody());
  parseDirectives(bodyNode);
  node.addChildToBack(bodyNode);
 return node;
}

// new method to get line number before a specific token
int getLineNumberBeforeToken(FunctionNode functionNode, Token token) {
  int position = functionNode.getAbsolutePosition() + functionNode.getPosition(token);
  int lineNumber = getLineNumber(position);
  if (lineNumber != -1 && isOnSameLine(lineNumber, functionNode)) {
    return lineNumber;
  }
  return -1;
}

// new method to check if a line number corresponds to the same line as another node
boolean isOnSameLine(int lineNumber, FunctionNode functionNode) {
  int functionStart = functionNode.getAbsolutePosition();
  int functionEnd = functionStart + functionNode.getLength();
  return lineNumber >= functionStart && lineNumber <= functionEnd;
}