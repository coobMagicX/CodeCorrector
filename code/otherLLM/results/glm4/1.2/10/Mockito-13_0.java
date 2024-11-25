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
        // We need to check if the verification was started on the correct mock
        if (verificationMode instanceof MockAwareVerificationMode && ((MockAwareVerificationMode) verificationMode).getMock() == invocation.getMock()) {                
            VerificationDataImpl data = new VerificationDataImpl(invocationContainerImpl, invocationMatcher);
            verificationMode.verify(data);
            return null;
        } else {
            // This means there is an invocation on a different mock. We need to add the verification mode back.
            mockingProgress.pushVerificationMode(verificationMode); // Add this line to re-add the verification mode
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

        // Redo setting invocation for potential stubbing in case of partial mocks / spies
        invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher);
        return ret;
    }
}