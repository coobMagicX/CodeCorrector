Node processFunctionNode(FunctionNode functionNode) {
  Node node = newNode(Token.FUNCTION);

  Name name = functionNode.getFunctionName();
  if (name != null) {
    Node newName = transform(name);
    newName.setLineno(name.getLineno());
    int nameColumn = functionNode.getAbsolutePosition() + functionNode.getLp(); // You may need to adjust this calculation.
    newName.setCharno(position2charno(nameColumn));
    node.addChildToBack(newName);
  }

  Node lp = newNode(Token.LP);
  if (name != null) {
    lp.setLineno(name.getLineno());
  } else {
    lp.setLineno(functionNode.getLineno());
  }
  int lparenCharno = functionNode.getLp() + functionNode.getAbsolutePosition();
  lp.setCharno(position2charno(lparenCharno));

  for (AstNode param : functionNode.getParams()) {
    Node paramNode = transform(param);
    paramNode.setLineno(param.getLineno());
    node.addChildToBack(paramNode);
  }
  node.addChildToBack(lp);

  Node bodyNode = transform(functionNode.getBody());
  parseDirectives(bodyNode);
  bodyNode.setLineno(functionNode.getBody().getLineno());
  node.addChildToBack(bodyNode);

  return node;
}

int position2charno(int position) {
  // Conversion logic here, ensure it is correctly implemented
  return position;
}
