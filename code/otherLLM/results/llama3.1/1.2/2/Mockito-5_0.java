public void verify(VerificationData data) {
    AssertionError error = null;

    timer.start();
    while (timer.isCounting()) {
        try {
            delegate.verify(data);

            if (returnOnSuccess) {
                return;
            } else {
                // Check for unwanted dependencies and prevent MockitoAssertionError from being caught
                if (error == null && data instanceof org.mockito.Mockito) {
                    error = new AssertionError("Mockito's API depends on JUnit classes");
                }
            }
        } catch (org.mockito.exceptions.verification.junit.ArgumentsAreDifferent e) {
            error = handleVerifyException(e);
        }
    }

    if (error != null) {
        throw error;
    }
}