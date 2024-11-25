public Object handle(Invocation invocation) throws Throwable {
    if (invocationContainerImpl.hasAnswersForStubbing()) {
        // stubbing voids with stubVoid() or doAnswer() style
        InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);
        invocationContainerImpl.setMethodForStubbing(invocationMatcher);
        return null;
    }
    VerificationMode verificationMode = mockingProgress.pullVerificationMode();

    if (verificationMode != null) {
        // Check if the verification mode is MockAware and if it's associated with the correct mock
        if (verificationMode instanceof MockAwareVerificationMode && ((MockAwareVerificationMode) verificationMode).getMock() == invocation.getMock()) {                
            VerificationDataImpl data = new VerificationDataImpl(invocationContainerImpl, invocationMatcher);
            verificationMode.verify(data); // Perform the verification on the correct mock
            return null;
        } else {
            // Reset the verification mode if it is not associated with the correct mock to avoid cross-interference
            verificationMode = null;
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
        // mocks / spies to avoid any confusion with the correct stubbed method.
        invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher);
        return ret;
    }
}