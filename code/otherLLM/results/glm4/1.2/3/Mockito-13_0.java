public Object handle(Invocation invocation) throws Throwable {
    if (invocationContainerImpl.hasAnswersForStubbing()) {
        // stubbing voids with stubVoid() or doAnswer() style
        InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);
        invocationContainerImpl.setMethodForStubbing(invocationMatcher);
        return null;
    }
    VerificationMode verificationMode = mockingProgress.pullVerificationMode();

    InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(),
                    invocation);

    mockingProgress.validateState();

    //if verificationMode is not null then someone is doing verify()        
    if (verificationMode != null) {
        // We need to check if the invocation was made on the correct mock
        boolean isOnCorrectMock = (verificationMode instanceof MockAwareVerificationMode && ((MockAwareVerificationMode) verificationMode).getMock() == invocation.getMock());
        if (!isOnCorrectMock) {
            // This means there is an invocation on a different mock. Re-adding the verification mode.
            // It's important to note that we should only re-add it if we are sure that this is not the correct
            // mock for verification, as adding it without checking would lead to incorrect verifications.
            return null; // Return early to avoid further processing and to re-add the verification mode outside of this method.
        }
        
        VerificationDataImpl data = new VerificationDataImpl(invocationContainerImpl, invocationMatcher);            
        verificationMode.verify(data);
        return null;
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

        // Redo setting invocation for potential stubbing in case of partial mocks / spies.
        invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher);
        return ret;
    }
}