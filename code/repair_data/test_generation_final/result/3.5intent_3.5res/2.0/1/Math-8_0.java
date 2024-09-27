public T sample() {
    int index = random.nextInt(singletons.size());
    return singletons.get(index);
}