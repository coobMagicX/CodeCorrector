OptimizationFramework optim = new OptimizationFramework();
Node node = // load a JavaScript function from somewhere

Node optimizedNode = optim.applyReducer(ReturnConstantReducer.class, node);