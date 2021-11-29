public String roleRange() {
    DCIteratorBinding it = (DCIteratorBinding) BindingContext.getCurrent()
                                                             .getCurrentBindingsEntry()
                                                             .get("UserroleView3Iterator");
    RowSetIterator rsIter = it.getRowSetIterator();
    Row rowObj = rsIter.getCurrentRow();
    Object acc = rowObj.getAttribute("Useraccount");
    String aid = acc.toString();
    // Excecute from AmImpl
    OperationBinding ob = executeOperation("roleDate");
    ob.getParamsMap().put("a", aid);
    ob.execute();

    return null;

}