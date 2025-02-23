public boolean hasSameMethod(Invocation candidate) {        
    Method m1 = invocation.getMethod();
    Method m2 = candidate.getMethod();
    
    // Check if they are the same method on potentially different class loaders
    if (!m1.getName().equals(m2.getName()) ||
        !m1.getDeclaringClass().equals(m2.getDeclaringClass())) {
        return false;
    }
    
    // Furthermore, checks if their parameter types are identical
    Class<?>[] params1 = m1.getParameterTypes();
    Class<?>[] params2 = m2.getParameterTypes();
    
    if (params1.length != params2.length) {
        return false;
    }
    
    for (int i = 0; i <