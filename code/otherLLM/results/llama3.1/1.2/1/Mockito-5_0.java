public void verify(VerificationData data) {
    AssertionError error = null;

    timer.start();
    while (timer.isCounting()) {
        try {
            delegate.verify(data);

            if (returnOnSuccess) {
                return;
            } else {
                // Added a check to avoid dependency on JUnit
                if (!(error instanceof org.junit.Assert.AssertionError)) {
                    error = null;
                }
            }
        } catch (MockitoAssertionError e) {
            error = handleVerifyException(e);
        }
        catch (org.mockito.exceptions.verification.MockitoVerificationFailure e) {
            // Changed the catch block to avoid dependency on JUnit
            error = handleVerifyException(e);
        }
    }

    if (error != null) {
        throw error;
    }
}