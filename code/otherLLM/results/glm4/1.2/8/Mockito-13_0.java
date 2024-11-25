public Object handle(Invocation invocation) throws Throwable {
    if (invocationContainerImpl.hasAnswersForStubbing()) {
        // stubbing voids with stubVoid() or doAnswer() style
        InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);
        invocationContainerImpl.setMethodForStubbing(invocationMatcher);
        return null;
    }
    
    VerificationMode verificationMode = mockingProgress.pullVerificationMode();
    
    if (verificationMode != null) {
        // We need to check if verification was started on the correct mock
        // - see VerifyingWithAnExtraCallToADifferentMockTest (bug 138)
        if (verificationMode instanceof MockAwareVerificationMode && ((MockAwareVerificationMode) verificationMode).getMock() == invocation.getMock()) {                
            InvocationMatcher currentMatcher = matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);
            VerificationDataImpl data = new VerificationDataImpl(invocationContainerImpl, currentMatcher);            
            verificationMode.verify(data);
        } else {
            // This means there is an invocation on a different mock. Re-adding the verification mode.
            mockingProgress.addVerificationMode(verificationMode);
        }
    }

    InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);

    mockingProgress.validateState();

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
        // mocks / spies.
        // Without it, the real method inside 'when' might have delegated
        // to other self method and overwrite the intended stubbed method
        // with a different one. The reset is required to avoid runtime exception that validates return type with stubbed method signature.
        invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher);
        return ret;
    }
}