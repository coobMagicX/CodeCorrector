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
        } catch (MockitoAssertionError e) {
            error = handleVerifyException(e);
        }
        // Removed the JUnit specific catch block to ensure Mockito does not depend on JUnit
    }

    if (error != null) {
        throw error;
    }
}

private AssertionError handleVerifyException(AssertionError e) {
    if (canRecoverFromFailure(delegate)) {
        sleep(pollingPeriodMillis);
        return e;
    } else {
        throw e;
    }
}