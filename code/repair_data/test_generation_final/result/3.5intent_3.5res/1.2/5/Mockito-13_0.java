public Object handle(Invocation invocation) throws Throwable {
    if (invocationContainerImpl.hasAnswersForStubbing()) {
        // stubbing voids with stubVoid() or doAnswer() style
        InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(mockingProgress
                        .getArgumentMatcherStorage(), invocation);
        invocationContainerImpl.setMethodForStubbing(invocationMatcher);
        return null;
    }
    VerificationMode verificationMode = mockingProgress.pullVerificationMode();

    InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(),
                    invocation);

    mockingProgress.validateState();

    //if verificationMode is not null then someone is doing verify()        
    if (verificationMode != null) {
        //We need to check if verification was started on the correct mock 
        // - see VerifyingWithAnExtraCallToADifferentMockTest (bug 138)
        if (verificationMode instanceof MockAwareVerificationMode && ((MockAwareVerificationMode) verificationMode).getMock() == invocation.getMock()) {
            // Reset the state of interactions on mockTwo
            mockingProgress.reset();

            VerificationDataImpl data = new VerificationDataImpl(invocationContainerImpl, invocationMatcher);
            verificationMode.verify(data);
            return null;
        } else {
            // Verification was not performed on the correct mock, throw NeverWantedButInvoked exception
            throw new NeverWantedButInvoked(invocation.getMock(), invocationMatcher.getInvocation());
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

        // redo setting invocation for potential stubbing in case of partial
        // mocks / spies.
        // Without it, the real method inside 'when' might have delegated
        // to other self method and overwrite the intended stubbed method
        // with a different one. The reset is required to avoid runtime exception that validates return type with stubbed method signature.
        invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher);
        return ret;
    }
}