public Object handle(Invocation invocation) throws Throwable {
    if (invocationContainerImpl.hasAnswersForStubbing()) {
        // stubbing voids with stubVoid() or doAnswer() style
        InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);
        invocationContainerImpl.setMethodForStubbing(invocationMatcher);
        return null;
    }
    VerificationMode verificationMode = mockingProgress.pullVerificationMode();

    if (verificationMode == null) {
        // No verification mode, proceed with stubbing or default answer
        InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);
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

    // At this point, verification mode is not null, proceed with verification

    if (verificationMode instanceof MockAwareVerificationMode && ((MockAwareVerificationMode) verificationMode).getMock() == invocation.getMock()) {
        // Verification started on the correct mock
        InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);
        mockingProgress.validateState();
        VerificationDataImpl data = new VerificationDataImpl(invocationContainerImpl, invocationMatcher);
        verificationMode.verify(data);
    } else {
        // This means there is an invocation on a different mock or incorrect mock. Re-adding verification mode
        if (verificationMode instanceof MockAwareVerificationMode) {
            ((MockAwareVerificationMode) verificationMode).addMock(invocation.getMock());
        }
        mockingProgress.validateState();
        // Verification will be handled later, not in this handle method.
    }

    // If no stubbing or default answer was found, and verification wasn't successful, return null
    if (stubbedInvocation == null && ret == null) {
        return null;
    } else {
        return ret;
    }
}