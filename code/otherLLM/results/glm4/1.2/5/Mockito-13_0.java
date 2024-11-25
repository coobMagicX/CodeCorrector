public Object handle(Invocation invocation) throws Throwable {
    if (invocationContainerImpl.hasAnswersForStubbing()) {
        // stubbing voids with stubVoid() or doAnswer() style
        InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);
        invocationContainerImpl.setMethodForStubbing(invocationMatcher);
        return null;
    }
    
    VerificationMode verificationMode = mockingProgress.pullVerificationMode();

    // Bind the matcher before checking the state to ensure correct matcher is used.
    InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);

    mockingProgress.validateState();

    //if verificationMode is not null then someone is doing verify()        
    if (verificationMode != null) {
        //We need to check if verification was started on the correct mock 
        // - see VerifyingWithAnExtraCallToADifferentMockTest (bug 138)
        if (verificationMode instanceof MockAwareVerificationMode && ((MockAwareVerificationMode) verificationMode).getMock() == invocation.getMock()) {                
            VerificationDataImpl data = new VerificationDataImpl(invocationContainerImpl, invocationMatcher);            
            verificationMode.verify(data);
            return null;
            // this means there is an invocation on a different mock. Re-adding verification mode 
            // - see VerifyingWithAnExtraCallToADifferentMockTest (bug 138)
        }
    }
    
    // Set the invocation for potential stubbing after checking if it's for correct mock.
    invocationContainerImpl.setInvocationForPotentialStubbing(invocationMatcher);
    OngoingStubbingImpl<T> ongoingStubbing = new OngoingStubbingImpl<T>(invocationContainerImpl);
    mockingProgress.reportOngoingStubbing(ongoingStubbing);

    StubbedInvocationMatcher stubbedInvocation = invocationContainerImpl.findAnswerFor(invocation);

    if (stubbedInvocation != null) {
        stubbedInvocation.captureArgumentsFrom(invocation);
        return stubbedInvocation.answer(invocation);
    } else {
        Object ret = mockSettings.getDefaultAnswer().answer(invocation);

        // Redo setting invocation for potential stubbing in case of partial
        // mocks / spies to ensure the correct method is stubbed.
        invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher);
        return ret;
    }
}