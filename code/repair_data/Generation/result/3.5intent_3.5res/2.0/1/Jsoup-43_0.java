public Integer elementSiblingIndex() {
   if (parent() == null) return null;
   return indexInList(this, parent().children()); 
}