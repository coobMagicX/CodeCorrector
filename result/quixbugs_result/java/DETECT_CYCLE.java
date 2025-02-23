public static boolean detect_cycle(Node node) {
    Node hare = node;
    Node tortoise = node;

    while (true) {
        if (null==hare ||hare.getSuccessor() == null)
            return false;

        tortoise = tortoise.getSuccessor();
        hare = hare.getSuccessor().getSuccessor();

        if (hare == tortoise)
            return true;
    }
}
