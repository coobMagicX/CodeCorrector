public void verify(VerificationData data) {
    AssertionError error = null;

    timer.start();
    while (timer.isCounting()) {
        try {
            delegate.verify(data);

            if (returnOnSuccess) {
                return;
            }
        } catch (MockitoAssertionError e) {
            error = handleVerifyException(e);
        }
        catch (org.mockito.exceptions.verification.junit.ArgumentsAreDifferent e) {
            error = handleVerifyException(e);
        }
    }

    // Handle the case when delegate.verify(data); returns without throwing an exception
    if (error == null && !delegate.isVerified()) {
        error = new AssertionError("Verification failed");
    }

    if (error != null) {
        throw error;
    }
}