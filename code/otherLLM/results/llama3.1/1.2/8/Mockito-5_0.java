public void verify(VerificationData data) {
    AssertionError error = null;

    timer.start();
    while (timer.isCounting()) {
        try {
            // Use the overloaded delegate.verify(data) method that does not load JUnit classes
            delegate.verifyMockito(data); // Assuming this method is available

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