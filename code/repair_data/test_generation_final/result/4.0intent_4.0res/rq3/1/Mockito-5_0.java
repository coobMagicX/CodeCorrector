public void verify(VerificationData data) {
    AssertionError error = null;

    timer.start();
    while (timer.isCounting()) {
        try {
            delegate.verify(data);

            if (returnOnSuccess) {
                return;
            } else {
                error = null;
            }
        } catch (AssertionError e) {
            error = handleVerifyException(e);
        }
    }

    if (error != null) {
        throw error;
    }
}

private AssertionError handleVerifyException(AssertionError e) {
    // Implementation to handle the exception
    return e;
}