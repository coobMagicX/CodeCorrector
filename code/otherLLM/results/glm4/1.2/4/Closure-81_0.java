private Node newNode(int type, Node child1, Node child2, Node child3) {
  return new Node(type, child1, child2, child3).clonePropsFrom(templateNode);
}

Node processFunctionNode(FunctionNode functionNode) {
  Name name = functionNode.getFunctionName();
  Boolean isUnnamedFunction = false;
  if (name == null) {
    name = newNode(Token.NAME).setIdentifier("");
    isUnnamedFunction = true;
  }
  
  Node node = newNode(Token.FUNCTION);
  Node newName = transform(name);
  
  node.addChildToBack(newName);

  Node lp = newNode(Token.LP, null, null, null); // Initialize lp with no children
  node.addChildToBack(lp);

  if (isUnnamedFunction) {
    newName.setLineno(functionNode.getLineno());
    int lpColumn = functionNode.getAbsolutePosition() + functionNode.getLp();
    newName.setCharno(position2charno(lpColumn));
    lp.setLineno(newName.getLineno()); // Set line number for the left parenthesis
    lp.setCharno(position2charno(lpColumn)); // Use the same charno as the name node
  } else {
    newName.setLineno(functionNode.getLineno());
    if (name.getIdentifier().isEmpty()) { // If named function but identifier is empty, set line number for the left parenthesis
      lp.setLineno(newName.getLineno());
    }
  }

  for (AstNode param : functionNode.getParams()) {
    lp.addChildToBack(transform(param));
  }

  Node bodyNode = transform(functionNode.getBody());
  parseDirectives(bodyNode);
  node.addChildToBack(bodyNode);

  return node;
}