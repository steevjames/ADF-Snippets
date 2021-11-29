package Beans;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;

import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

public class Contact {
    public Contact() {
    }
    public BindingContainer getBindingsCont() {
             return BindingContext.getCurrent().getCurrentBindingsEntry();
         }

         public OperationBinding executeOperation(String operation) {
             OperationBinding createParam = getBindingsCont().getOperationBinding(operation);
             return createParam;
         }

    public void primaryCheck(ValueChangeEvent valueChangeEvent) {
        Object val= valueChangeEvent.getNewValue();
                System.out.println("Inside bean"+val.toString());
                if(val.toString().equals("true")) {
                    System.out.println("Inside if");
                    DCIteratorBinding it = (DCIteratorBinding)BindingContext.getCurrent().getCurrentBindingsEntry().get("CustomercontactsView2Iterator");   
                    RowSetIterator rsIter = it .getRowSetIterator();
                    Row rowObj = rsIter .getCurrentRow();
                    Object cid=rowObj.getAttribute("Contactid");
                    Object aid=rowObj.getAttribute("Customeraccountid");
                    System.out.println(cid.toString());
                    OperationBinding ob = executeOperation("editPrimary");
                    ob.getParamsMap().put("cidStr", cid.toString());
                    //ob.getParamsMap().put("aidStr", aid.toString());
                    ob.execute();
    }
    }

    public void wrapper(ValueChangeEvent valueChangeEvent) {
        primaryCheck(valueChangeEvent);
//        DCBindingContainer bindings = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
//        OperationBinding operationBinding = bindings.getOperationBinding("Commit");
//        operationBinding.execute();
        
    }
}
