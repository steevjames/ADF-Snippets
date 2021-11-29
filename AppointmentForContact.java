package Beans;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

import oracle.adf.model.BindingContext;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

public class AppointmentsForContact {
    public AppointmentsForContact() {
    }

    public BindingContainer getBindingsCont() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public OperationBinding executeOperation(String operation) {
        OperationBinding createParam = getBindingsCont().getOperationBinding(operation);
        return createParam;
    }

    public String createEntry() {
        System.out.println("\n\nInside method");
        DCIteratorBinding it = (DCIteratorBinding) BindingContext.getCurrent()
                                                                 .getCurrentBindingsEntry()
                                                                 .get("AppointmentsView2Iterator");
        RowSetIterator rsIter = it.getRowSetIterator();
        Row rowObj = rsIter.getCurrentRow();
        String contactId = rowObj.getAttribute("Contactid").toString();
        String appointmentId = rowObj.getAttribute("Appointmentid").toString();

        OperationBinding ob = executeOperation("setInviteeData");
        ob.getParamsMap().put("contactId", contactId);
        ob.getParamsMap().put("appointmentId", appointmentId);
        ob.execute();
        HandleNavigation("commit");
        System.out.print("\n\n\nDone...\n\n");
        return null;
    }

    public static void HandleNavigation(String outcome) {
        FacesContext context = FacesContext.getCurrentInstance();
        NavigationHandler nh = context.getApplication().getNavigationHandler();
        nh.handleNavigation(context, "", outcome);
    }
}
