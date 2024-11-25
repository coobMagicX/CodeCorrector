public Object handle(Invocation invocation) throws Throwable {
    if (invocationContainerImpl.hasAnswersForStubbing()) {
        // stubbing voids with stubVoid() or doAnswer() style
        InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);
        invocationContainerImpl.setMethodForStubbing(invocationMatcher);
        return null;
    }
    
    VerificationMode verificationMode = mockingProgress.pullVerificationMode();

    if (verificationMode != null && verificationMode instanceof MockAwareVerificationMode) {
        MockAwareVerificationMode mockAwareVerificationMode = (MockAwareVerificationMode) verificationMode;
        if (mockAwareVerificationMode.getMock() == invocation.getMock()) {
            InvocationMatcher currentMatcher = matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);
            VerificationDataImpl data = new VerificationDataImpl(invocationContainerImpl, currentMatcher);            
            verificationMode.verify(data);
            // After verify, we need to clear the verification mode if it's not meant to be reusable
            mockingProgress.clearVerificationMode();
        } else {
            // Re-add the verification mode for the correct mock if there was a mistake in the previous invocation
            mockingProgress.pushVerificationMode(verificationMode);
        }
    }

    InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(),
                invocation);

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
        invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher);
        return ret;
    }
}