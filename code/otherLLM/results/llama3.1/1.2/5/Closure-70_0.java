private void declareArguments(Node functionNode) {
  Node astParameters = functionNode.getFirstChild().getNext();
  Node body = astParameters.getNext();
  FunctionType functionType = (FunctionType) functionNode.getJSType();
  
  if (functionType != null) {
    Node jsDocParameters = functionType.getParametersNode();
    
    if (jsDocParameters != null) {
      Node jsDocParameter = jsDocParameters.getFirstChild();
      
      for (Node astParameter : astParameters.children()) {
        String astParamName = astParameter.getFirstChild().getText();
        
        while(jsDocParameter != null){
          String jsDocParamName = jsDocParameter.getJSType().getText();
          
          if(astParamName.equals(jsDocParamName)){
            defineSlot(astParameter, functionNode,
                jsDocParameter.getJSType(), true);
            
            break;
          }
          else if(jsDocParameter.getNext() != null 
                  && jsDocParameter.getNext().getJSType().getText()
                     .equals(jsDocParamName)){
            // ignore duplicate variable declaration
            break;
          } else {
            defineSlot(astParameter, functionNode, null, true);
            
            break;
          }
        }
        
        jsDocParameter = jsDocParameter.getNext();
      }
    }
  }
} // end declareArguments