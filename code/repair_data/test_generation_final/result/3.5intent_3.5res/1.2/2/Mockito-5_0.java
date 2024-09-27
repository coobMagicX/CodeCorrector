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
        } catch (org.mockito.exceptions.verification.junit.ArgumentsAreDifferent e) {
            error = handleVerifyException(e);
        } catch (Exception e) {
            // Handle any other exceptions here
            error = handleVerifyException(new AssertionError("Exception during verification"));
        }
    }

    if (error != null) {
        throw error;
    }
}