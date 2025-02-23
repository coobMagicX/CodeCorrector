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
      Node firstOperand = val.getFirstChild();
      Node secondOperand = firstOperand.getNext();
      return isValidDefineValue(firstOperand, defines) &&
             isValidDefineValue(secondOperand, defines);

    
    case Token.BITAND:
    case Token.BITOR:
    case Token.BITXOR:
      Node firstBitwiseOperand = val.getFirstChild();
      Node secondBitwiseOperand = firstBitwiseOperand.getNext();
      return isValidDefineValue(firstBitwiseOperand, defines) &&
             isValidDefineValue(secondBitwiseOperand, defines);

    
    case Token.NOT:
    case Token.NEG:
    case