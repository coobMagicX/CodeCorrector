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
        catch (org.mockito.exceptions.verification.junit.ArgumentsAreDifferent e) {
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
        // Create a new AssertionError with a custom message
        AssertionError error = new AssertionError("Verification failed: " + e.getMessage());

        // Copy the stack trace from the original AssertionError
        error.setStackTrace(e.getStackTrace());

        throw error;
    }
}