public Object clone() throws CloneNotSupportedException {
    TimeSeries s2 = createCopy(0, getItemCount() - 1);
    s2.setIndependent(true); // Add this line to make the cloned object independent
    return s2;
}