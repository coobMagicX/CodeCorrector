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
        } catch (MockitoAssertionError | org.mockito.exceptions.verification.junit.ArgumentsAreDifferent e) {
            error = handleVerifyException(e);
        } catch (AssertionError e) {
            error = handleVerifyException(e);
        }
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