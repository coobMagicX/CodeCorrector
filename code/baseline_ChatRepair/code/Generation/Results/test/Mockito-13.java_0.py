public Object handle(Invocation invocation) throws Throwable {
    if (invocationContainerImpl.hasAnswersForStubbing()) {
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
        // Check if the mock under verification is the mock invoked
        if (verificationMode instanceof MockAwareVerificationMode) {
            Object mockUnderVerification = ((MockAwareVerificationMode) verificationMode).getMock();
            if (mockUnderVerification == invocation.getMock()) {
                // Correct mock, proceed with verification
                VerificationDataImpl data = new VerificationDataImpl(invocationContainerImpl, invocationMatcher);
                verificationMode.verify(data);
                return null;  // Normal exit for verification scenario
            } else {
                // Verification is ongoing for a different mock.
                // We need to re-add verification mode (exit from the handler).
                mockingProgress.verificationStarted(verificationMode);
            }
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

        // Redo setting invocation for potential stubbing in case of partial
        // mocks / spies. It avoids overriding the intended stubbed method in certain cases.
        invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher);
        return ret;
    }
}
