package Beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.faces.application.NavigationHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.view.rich.component.rich.data.RichColumn;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.util.ResetUtils;
import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;
import oracle.adf.model.BindingContext;

import oracle.adf.model.bean.DCDataRow;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichPopup;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.server.ViewRowImpl;
import org.apache.myfaces.trinidad.model.UploadedFile;

public class FileBean {
    private UploadedFile myFile;

    public FileBean() {
    }

    public void setMyFile(UploadedFile myFile) {
        this.myFile = myFile;
    }

    public UploadedFile getMyFile() {
        return myFile;
    }

    private String uploadFile(UploadedFile file) {
        System.out.println("inside upload");
        UploadedFile myfile = file;
        String path = null;
        if (myfile == null) {
        } else {
            // All uploaded files will be stored in below path
            path = "C:\\FileSys\\" + myfile.getFilename();
            InputStream inputStream = null;
            try {
                FileOutputStream out = new FileOutputStream(path);
                inputStream = myfile.getInputStream();
                byte[] buffer = new byte[8192];
                int bytesRead = 0;
                while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
                out.close();
            } catch (Exception ex) {
                // handle exception
                ex.printStackTrace();
            } finally {
                try {
                    // inputStream.close();
                    System.out.println("Inside try");
                } catch (Exception e) {
                }
            }
        }
        // Returns the path where file is stored
        return path;
    }

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
        UploadedFile fileVal = getMyFile();
        String path = uploadFile(fileVal);
        OperationBinding ob = executeOperation("setFileData");

        ob.getParamsMap().put("name", fileVal.getFilename());
        ob.getParamsMap().put("path", path);
        ob.getParamsMap().put("contTyp", fileVal.getContentType());
        ob.execute();
        // String str= getMyFile().getFilename();
        return null;
    }

    public void downloadFileListener(FacesContext facesContext, OutputStream outputStream) throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        String filepath = fc.getApplication().evaluateExpressionGet(fc, "#{row.filepath}", String.class);
        // Read file from particular path, path bind is binding of table field that
        // contains path
        File filed = new File(filepath);
        FileInputStream fis;
        byte[] b;
        try {
            fis = new FileInputStream(filed);
            int n;
            while ((n = fis.available()) > 0) {
                b = new byte[n];
                int result = fis.read(b);
                System.out.println(result);
                outputStream.write(b, 0, b.length);
                if (result == -1)
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputStream.flush();
    }

    public String deleteButton() {

        DCIteratorBinding it = (DCIteratorBinding) BindingContext.getCurrent().getCurrentBindingsEntry()
                .get("FilesView4Iterator");
        RowSetIterator rsIter = it.getRowSetIterator();
        Row rowObj = rsIter.getCurrentRow();

        String filepath = rowObj.getAttribute("Filepath").toString();
        System.out.println(filepath);
        File f = new File(filepath);
        if (f.delete()) {
            System.out.println("Done");
        } else {
            System.out.println("Unsuccesful");
            return null;
        }
        try {

            NavigationHandler nvHndlr = FacesContext.getCurrentInstance().
                    getApplication().
                    getNavigationHandler();
            nvHndlr.handleNavigation(FacesContext.getCurrentInstance(), null, "delFile");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void closePopup(ActionEvent actionEvent) {
        UIComponent tmpComponent;
        tmpComponent = actionEvent.getComponent().getParent();
        while (!(tmpComponent instanceof RichPopup)) {
            tmpComponent = tmpComponent.getParent();
        }
        RichPopup popup = (RichPopup) tmpComponent;
        popup.hide();
    }
}
