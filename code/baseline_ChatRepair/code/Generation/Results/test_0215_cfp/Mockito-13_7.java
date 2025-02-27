public Object handle(Invocation invocation) throws Throwable {
    if (invocationContainerImpl.hasAnswersForStubbing()) {
        
        InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(
                        mockingProgress.getArgumentMatcherStorage(), invocation);
        invocationContainerImpl.setMethodForStubbing(invocationMatcher);
        return null;
    }

    VerificationMode verificationMode = mockingProgress.pullVerificationMode();
    InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(
                    mockingProgress.getArgumentMatcherStorage(), invocation);
    mockingProgress.validateState();

    if (verificationMode != null) {
        
        if (verificationMode instanceof MockAwareVerificationMode) {
            if (((MockAwareVerificationMode) verificationMode).getMock() != invocation.getMock()) {
                
                mockingProgress.verificationStarted(verificationMode);
                return null;  
            }
            
            VerificationDataImpl data = new VerificationDataImpl(invocationContainerImpl, invocationMatcher);
            verificationMode.verify(data);
            return null;  
        }
    }

    
    invocationContainerImpl.setInvocationForPotentialStubbing(invocationMatcher);
    OngoingStubbingImpl<T> ongoingStubbing = new OngoingStubbingImpl<T>(invocationContainerImpl);
    mockingProgress.reportOngoingStubbing(ongoingStubbing);

    StubbedInvocationMatcher stubbedInvocation = invocationContainerImpl.findAnswerFor(invocation);
    if (stubbedInvocation != null) {
        stubbedInvocation.captureArgumentsFrom(invocation);
        return stubbedInvocation.answer(invocation);
    } else {
        Object ret = mockSettings.getDefaultAnswer().answer(invocation);
        
        
        invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher);
        return ret;
    }
}
