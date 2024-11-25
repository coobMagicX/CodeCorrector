Node processFunctionNode(FunctionNode functionNode) {
  Name name = functionNode.getFunctionName();
  Boolean isUnnamedFunction = (name == null);
  if (isUnnamedFunction) {
    name = new Name();
    name.setIdentifier("");
  }
  
  Node node = newNode(Token.FUNCTION);
  Node newName = transform(name);
  newName.setLineno(functionNode.getLineno());
  newName.setCharno(position2charno(newName.getStart()));

  node.addChildToBack(newName);

  Node lp = newNode(Token.LP);
  lp.setLineno(functionNode.getLineno());
  int lparenCharno = position2charno(functionNode.getLp() + functionNode.getAbsolutePosition());
  lp.setCharno(lparenCharno);

  for (AstNode param : functionNode.getParams()) {
    Node transformedParam = transform(param);
    transformedParam.setLineno(param.getLineno());
    transformedParam.setCharno(position2charno(transformedParam.getStart()));
    lp.addChildToBack(transformedParam);
  }

  node.addChildToBack(lp);

  Node bodyNode = transform(functionNode.getBody());
  parseDirectives(bodyNode);
  bodyNode.setLineno(functionNode.getLineno());
  bodyNode.setCharno(position2charno(bodyNode.getStart()));
  node.addChildToBack(bodyNode);
  
  return node;
}