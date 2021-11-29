
import oracle.binding.BindingContainer;   
   
   /***** Generic Method to Get BindingContainer **/
    public BindingContainer getBindingsCont() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }
    /**
     * 
     * Generic Method to execute operation
     * 
     */
    public OperationBinding executeOperation(String operation) {
        OperationBinding createParam = getBindingsCont().getOperationBinding(operation);
        return createParam;
    }

    public String buttonAction() {
        // Add event code here...
        OperationBinding ob = executeOperation("methodName");

        ob.getParamsMap().put("parmeter1", fileVal.getFilename());
        ob.getParamsMap().put("parameter2", path);
        ob.getParamsMap().put("parameter3", fileVal.getContentType());
        ob.execute();
        return null;
    }