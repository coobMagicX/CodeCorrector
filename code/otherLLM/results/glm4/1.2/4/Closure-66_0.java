// Visit each node in the AST and ensure they have a valid type
for (Node n : astRoot) {
  visitNode(n);
}