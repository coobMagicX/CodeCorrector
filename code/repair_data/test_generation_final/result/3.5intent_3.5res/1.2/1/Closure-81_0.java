Node processFunctionNode(FunctionNode functionNode) {
  Name name = functionNode.getFunctionName();
  if (name == null || name.getIdentifier().isEmpty()) {
    throw new RuntimeException("Error: Unnamed function encountered");
  }
  Node node = newNode(Token.FUNCTION);
  Node newName = transform(name);
  newName.setLineno(functionNode.getLineno());
  int lpColumn = functionNode.getAbsolutePosition() + functionNode.getLp();
  newName.setCharno(position2charno(lpColumn));

  node.addChildToBack(newName);
  Node lp = newNode(Token.LP);
  lp.setLineno(newName.getLineno());
  int lparenCharno = functionNode.getLp() + functionNode.getAbsolutePosition();
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