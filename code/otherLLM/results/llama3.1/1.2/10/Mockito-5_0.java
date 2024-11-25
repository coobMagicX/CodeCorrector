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
            if (!canRecoverFromFailure(delegate.getVerificationMode())) {
                throw handleVerifyException(e);
            } else {
                sleep(100); // Introduce a small delay to allow for recovery
            }
        }
        catch (org.mockito.exceptions.verification.junit.ArgumentsAreDifferent e) {
            error = handleVerifyException(e);
        }
    }

    if (error != null) {
        throw error;
    }
}