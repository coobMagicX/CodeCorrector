static boolean isValidDefineValue(Node val, Set<String> defines) {
  switch (val.getType()) {
    case Token.STRING:
    case Token.NUMBER:
    case Token.TRUE:
    case Token.FALSE:
      return true;

    
    case Token.ADD:
    case Token.SUB:
    case Token.MUL:
    case Token.DIV:
    case Token.MOD:
      
      Node leftOperand = val.getFirstChild();
      Node rightOperand = leftOperand.getNext();
      return isValidDefineValue(leftOperand, defines) && isValidDefineValue(rightOperand, defines);

    
    case Token.BITAND:
    case Token.BITOR:
    case Token.BITXOR:
      
      leftOperand = val.getFirstChild();
      rightOperand = leftOperand.getNext();
      return isValidDefineValue(leftOperand, defines) && isValidDefineValue(rightOperand, defines