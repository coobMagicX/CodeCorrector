public void verify(VerificationData data) {
    AssertionError error = null;

    timer.start();
    while (timer.isCounting()) {
        try {
            delegate.verify(data);

            if (returnOnSuccess) {
                return;
            }
        } catch (Error e) {
            error = handleVerifyException(e);
        } catch (RuntimeException e) {
            error = handleVerifyException(e);
        }
    }

    if (error != null) {
        throw error;
    }
}

private AssertionError handleVerifyException(Throwable e) {
    // Process the exception and return an AssertionError to be rethrown
    // This method should not depend on JUnit either, and should handle conversion
    // of exceptions in a generalized manner.
    return new AssertionError(e.getMessage(), e);
}
