        public Object clone() throws CloneNotSupportedException {
            Object clone = createCopy(0, getItemCount());
            return clone;
        }