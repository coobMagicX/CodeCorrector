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
        } catch (AssertionError e) {
            error = handleVerifyException(e);
            if (error != null && !canRecoverFromFailure(delegate)) {
                throw error;
            }
        }
    }

    if (error != null) {
        throw error;
    }
}