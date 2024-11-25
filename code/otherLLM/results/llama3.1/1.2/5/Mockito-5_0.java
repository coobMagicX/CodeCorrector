public void verify(VerificationData data) {
    AssertionError error = null;

    timer.start();
    while (timer.isCounting()) {
        try {
            delegate.verify(data);

            if (returnOnSuccess) {
                return;
            } else {
                // Removed the line that was resetting 'error' to null
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