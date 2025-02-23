public void verify(VerificationData data) {
    AssertionError error = null;
    timer.start();

    while (timer.isCounting()) {
        try {
            delegate.verify(data);
            if (returnOnSuccess) {
                return;
            }
        } catch (AssertionError e) { // Directly catching AssertionError, avoid any library specifics
            error = handleVerifyException(e); // Ensure handleVerifyException can cope with AssertionError
        }
    }

    if (error != null) {
        throw error;
    }
}
