public void verify(VerificationData data) {
    AssertionError error = null;

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

    if (error != null) {
        throw error;
    }
}