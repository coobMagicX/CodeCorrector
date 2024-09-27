public Object clone() throws CloneNotSupportedException {
    Object clone = createCopy(0, getItemCount() - 1);
    return clone;
}

public TimeSeries createCopy(int startIndex, int endIndex) {
    TimeSeries copy = new TimeSeries();
    for (int i = startIndex; i <= endIndex; i++) {
        copy.add(data.get(i));
    }
    return copy;
}

@Override
public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }

    if (!(obj instanceof TimeSeries)) {
        return false;
    }

    TimeSeries other = (TimeSeries) obj;

    if (this.getItemCount() != other.getItemCount()) {
        return false;
    }

    for (int i = 0; i < getItemCount(); i++) {
        if (!this.getData(i).equals(other.getData(i))) {
            return false;
        }
    }

    return true;
}