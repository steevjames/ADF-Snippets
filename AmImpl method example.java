    public void setFileData(String name, String path, String contTyp) {
        ViewObject fileVo = this.getFilesView4();
        Row newRow = fileVo.createRow();
        newRow.setAttribute("Filename", name);
        newRow.setAttribute("Filepath", path);
        newRow.setAttribute("Filetype", contTyp);
        fileVo.insertRow(newRow);
        this.getDBTransaction().commit();
        fileVo.executeQuery();
    }


    public void refreshPage() {
        FacesContext fc = FacesContext.getCurrentInstance();
        String refreshpage = fc.getViewRoot().getViewId();
        ViewHandler ViewH = fc.getApplication().getViewHandler();
        UIViewRoot UIV = ViewH.createView(fc, refreshpage);
        UIV.setViewId(refreshpage);
        fc.setViewRoot(UIV);
    }


    public void estimatedPrice(String pidStr) {

        Object pid = pidStr;
        RowSet rs = (RowSet) getProductsView1();
        rs.reset();

        while (rs.hasNext()) {
            System.out.println("Iinside while loop");
            ProductsViewRowImpl r = (ProductsViewRowImpl) rs.next();
            Object prodid = r.getAttribute("Productid");
            String prodidstr = prodid.toString();

            if (prodidstr.equals(pidStr)) {

                Object p = r.getAttribute("Price");
                double pr = Double.parseDouble(p.toString());
                DCIteratorBinding it = (DCIteratorBinding) BindingContext.getCurrent()
                                                                         .getCurrentBindingsEntry()
                                                                         .get("RevenueitemView2Iterator");
                RowSetIterator rsIter = it.getRowSetIterator();
                Row rowObj = rsIter.getCurrentRow();
                Object c = rowObj.getAttribute("Currency");
                if (c != null) {
                    String cStr = c.toString();


                    if (cStr != null) {
                        if (!(cStr.equals("INR"))) {
                            if (cStr.equals("USD")) {
                                pr *= 75;
                            } else if (cStr.equals("EUR")) {
                                pr *= 85;
                            }
                            p = (Double) pr;
                        }
                    }
                }
                System.out.println(pr);
                rowObj.setAttribute("Estimatedprice", p);
                rs.closeRowSet();
                break;
            }


        }

    }

    public void totalRevenueOppAM(String oidStr) {
        System.out.println("Inside impl  " + oidStr);

        //
        RowSet rs = (RowSet) getRevenueitemView1();
        rs.reset();
        System.out.println(rs.hasNext());
        double total = 0;
        while (rs.hasNext()) {
            System.out.println("Inside while");
            Row r = rs.next();
            Object loopoid = r.getAttribute("Opportunityid");
            String loopoidStr = loopoid.toString();
            if (oidStr.equals(loopoidStr)) {
                System.out.println("inside if");

                Object looprev = r.getAttribute("Revenue");
                if (looprev != null) {
                    double looprevint = Double.parseDouble(looprev.toString());
                    Object curr = r.getAttribute("Currency");
                    if (curr != null) {
                        String currStr = curr.toString();
                        System.out.println(curr);
                        System.out.println("Unconverted loop rev int" + looprevint);

                        if (!(curr.equals("INR"))) {
                            if (curr.equals("USD")) {
                                looprevint *= 75;
                            } else if (curr.equals("EUR")) {
                                looprevint *= 85;
                            }
                        }
                    }
                    System.out.println("Converted loop rev int" + looprevint);

                    total += looprevint;
                }
            }
        }
        rs.closeRowSet();
        DCIteratorBinding it = (DCIteratorBinding) BindingContext.getCurrent()
                                                                 .getCurrentBindingsEntry()
                                                                 .get("OpportunityView1Iterator");
        RowSetIterator rsIter = it.getRowSetIterator();
        Row rowObj = rsIter.getCurrentRow();
        Object c = rowObj.getAttribute("Currency");
        if (c != null) {

            String cstr = c.toString();
            if (!(cstr.equals("INR"))) {

                if (cstr.equals("USD")) {
                    total /= 75;
                } else if (cstr.equals("EUR")) {
                    total /= 85;
                }
            }
        }
        total=Math.ceil(total);
        rowObj.setAttribute("Totalrevenue", total);
        
        System.out.println(total);
        double best = 1.1 * total;
        double worst = 0.9 * total;
        Object bestrev = (Double) best;
        Object worstrev = (Double) worst;
        rowObj.setAttribute("Bestcaserevenue", bestrev);
        rowObj.setAttribute("Worstcaserevenue", worstrev);
    }


        
    public void roleDate(String a) {

        System.out.println("Inside roleDatete" + a);
        RowSet rs = (RowSet) getUserroleView1();
        rs.reset();
        System.out.println(rs.hasNext());
        String looprolestr = null;
        String loopaccstr = null;

        boolean flag = true;

        DCIteratorBinding it = (DCIteratorBinding) BindingContext.getCurrent()
                                                                 .getCurrentBindingsEntry()
                                                                 .get("UserroleView3Iterator");
        RowSetIterator rsIter = it.getRowSetIterator();
        Row rowObj = rsIter.getCurrentRow();
        Object role = rowObj.getAttribute("Roleid");
        String rolestr = role.toString();

        Object u = rowObj.getAttribute("Userroleid");
        String ustr = u.toString();
        Object start = null;
        Object end = null;

        while (rs.hasNext()) {
            System.out.println("Inside while");
            Row rw = rs.next();
            Object looproleid = rw.getAttribute("Userroleid");
            String loopidstr = looproleid.toString();
            Object loopacc = rw.getAttribute("Useraccount");
            loopaccstr = loopacc.toString();
            Object looprole = rw.getAttribute("Roleid");
            looprolestr = looprole.toString();
            System.out.println(loopidstr);
            System.out.println(ustr);
            System.out.println(rolestr);
            System.out.println(looprolestr);
            if (a.equals(loopaccstr))
            
                if (!(loopidstr.equals(ustr))) {
                    start = rw.getAttribute("Startdate");
                    end = rw.getAttribute("Enddate");
                    if (rolestr.equals(looprolestr)) {
                        System.out.println("Inside if");
                        flag = false;
                        break;
                    }
                }
        }
        Object sdate = rowObj.getAttribute("Startdate");
        Object edate = rowObj.getAttribute("Enddate");
        java.sql.Timestamp s = (java.sql.Timestamp) sdate;
        java.sql.Timestamp e = (java.sql.Timestamp) edate;
        java.sql.Timestamp starts = (java.sql.Timestamp) start;
        java.sql.Timestamp ends = (java.sql.Timestamp) end;
        System.out.println(starts);
        System.out.println(s);
        System.out.println(e);
        if(end==null) {
            if(s.after(starts))
            {
            FacesContext context = FacesContext.getCurrentInstance();
                            context.addMessage(null, new FacesMessage("Duplicate role found"));
            }
            else if(s.before(starts)&&e.before(starts)){
                NavigationHandler nvHndlr = FacesContext.getCurrentInstance()
                                                        .

                                                        getApplication()
                                                        .

                                                        getNavigationHandler();

                nvHndlr.handleNavigation(FacesContext.getCurrentInstance(), null, "save");
                System.out.println("Valid");
                rs.closeRowSet();
            }
        }
        else
        {
        if(flag){
            NavigationHandler nvHndlr = FacesContext.getCurrentInstance()
                                                    .

                                                    getApplication()
                                                    .

                                                    getNavigationHandler();

            nvHndlr.handleNavigation(FacesContext.getCurrentInstance(), null, "save");
            rs.closeRowSet();
        }
    
        if (!flag) {
          
            System.out.println(start.getClass().getName());

            if (end != null && start != null) {
                System.out.println(start.toString());
                System.out.println(end.toString());
            }
         
        
            if (edate != null && sdate != null) {
                System.out.println(sdate.toString());
                System.out.println(edate.toString());
            }
            
           

            if (s.after(starts) && s.before(ends)) {
                System.out.println("Invalid");
                FacesContext context = FacesContext.getCurrentInstance();
                                context.addMessage(null, new FacesMessage("Duplicate role found"));
//                NavigationHandler nvHndlr = FacesContext.getCurrentInstance()
//                                                        .
//
//                                                        getApplication()
//                                                        .
//
//                                                        getNavigationHandler();
//
//                nvHndlr.handleNavigation(FacesContext.getCurrentInstance(), null, "invalidRange");
                rs.closeRowSet();
            }

            else if (e.after(starts) && e.before(ends)) {
                System.out.println("Invalid");
                FacesContext context = FacesContext.getCurrentInstance();
                                context.addMessage(null, new FacesMessage("Duplicate role found"));
                
//                NavigationHandler nvHndlr = FacesContext.getCurrentInstance()
//                                                        .
//
//                                                        getApplication()
//                                                        .
//
//                                                        getNavigationHandler();
//
//                nvHndlr.handleNavigation(FacesContext.getCurrentInstance(), null, "invalidRange");
                rs.closeRowSet();
            } else if (s.before(starts) && e.after(ends)) {
                System.out.println("Invalid");
                FacesContext context = FacesContext.getCurrentInstance();
                                context.addMessage(null, new FacesMessage("Duplicate role found"));
//                NavigationHandler nvHndlr = FacesContext.getCurrentInstance()
//                                                        .
//
//                                                        getApplication()
//                                                        .
//
//                                                        getNavigationHandler();
//
//                nvHndlr.handleNavigation(FacesContext.getCurrentInstance(), null, "invalidRange");
                rs.closeRowSet();
            } else {
                NavigationHandler nvHndlr = FacesContext.getCurrentInstance()
                                                        .

                                                        getApplication()
                                                        .

                                                        getNavigationHandler();

                nvHndlr.handleNavigation(FacesContext.getCurrentInstance(), null, "save");
                System.out.println("Valid");
                rs.closeRowSet();
            }

        }
        }
        

    }
