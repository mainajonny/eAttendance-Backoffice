package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import models.MDept;
import models.MProgs;
import models.MUnits;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;
import play.libs.Json;
import play.mvc.*;
import org.apache.commons.io.FileUtils;

import views.html.Programmes.*;

import java.util.Map;
/**
 * Created by Meech on 9/6/2018.
 */
public class CProgs extends Controller {

    public static Result renderProgs(){
        return ok(AddProg.render("Add Programmes", MDept.deptoptions()));
    }

    public static Result renderViewProgs(){
        return ok(ViewProg.render("View Programmes"));
    }

    public static Result addProgs(){

        DynamicForm requestform = Form.form().bindFromRequest();
        String ProgName = requestform.get("ProgName");
        String ProgDept = requestform.get("ProgDept");
        String ProgLevel = requestform.get("ProgLevel");

        MProgs progs = new MProgs();
        progs.ProgName=ProgName;
        progs.ProgDept=ProgDept;
        progs.ProgLevel =ProgLevel;
        progs.isActive="1";
        progs.save();
        return redirect(routes.CProgs.renderViewProgs());
    }

    public static Result renderEditProg(Long id) {

        MProgs mProgs= MProgs.findProgById(id);
        return ok(EditProg.render("Edit Programme", mProgs, MDept.deptoptions()));
    }

    public static Result EditProgramme(Long id){
        MProgs mProgs=new MProgs();
        mProgs.eid=id;
        mProgs.isActive="1";
        mProgs.ProgName=Form.form().bindFromRequest().get("ProgName");
        mProgs.ProgDept=Form.form().bindFromRequest().get("ProgDept");
        mProgs.ProgLevel=Form.form().bindFromRequest().get("ProgLevel");
        Ebean.update(mProgs);

        return redirect(routes.CProgs.renderViewProgs());
    }

    public static  Result deleteProg(Long id){

        MProgs mProgs=MProgs.findProgById(id);
        mProgs.delete();

        return  redirect(routes.CProgs.renderViewProgs());
    }

    public static Result progslist() {

        Map<String, String[]> params = request().queryString();

        Integer iTotalRecords = MProgs.findProgrammes.findRowCount();
        String filter = params.get("sSearch")[0];
        Integer pageSize = Integer.valueOf(params.get("iDisplayLength")[0]);
        Integer page = Integer.valueOf(params.get("iDisplayStart")[0]) / pageSize;

        /**
         * Get sorting order and column
         */
        String sortBy = "ProgName";
        String order = params.get("sSortDir_0")[0];

        switch (Integer.valueOf(params.get("iSortCol_0")[0])) {
            case 0:
                sortBy = "ProgName";
                break;
            case 1:
                sortBy = "eid";
                break;
            case 2:
                sortBy = "ProgDept";
                break;
            case 3:
                sortBy = "ProgLevel";
                break;

        }

        /**
         * Get page to show from database
         * It is important to set setFetchAhead to false, since it doesn't benefit a stateless application at all.
         */
        Page<MProgs> areaPage = MProgs.findProgrammes.where(
                Expr.or(
                        Expr.ilike("ProgName", "%" + filter + "%"),
                        Expr.or(
                                Expr.ilike("eid", "%" + filter + "%"),
                                Expr.ilike("ProgDept", "%" + filter + "%")
                        )
                )
        )
                .orderBy(sortBy + " " + order + ", eid " + order)
                .findPagingList(pageSize).setFetchAhead(false)
                .getPage(page);

        Integer iTotalDisplayRecords = areaPage.getTotalRowCount();


        ObjectNode result = Json.newObject();

        result.put("sEcho", Integer.valueOf(params.get("sEcho")[0]));
        result.put("iTotalRecords", iTotalRecords);
        result.put("iTotalDisplayRecords", iTotalDisplayRecords);

        ArrayNode anc = result.putArray("aaData");

        for (MProgs cc : areaPage.getList()) {
            ObjectNode row = Json.newObject();
            //    System.out.println("in data table fetch: " + cc.RoomName);
            row.put("ProgName", cc.ProgName);
            row.put("ProgDept", cc.ProgDept);
            row.put("ProgLevel", cc.ProgLevel);
            row.put("eid", cc.eid);
            anc.add(row);
        }

        return ok(result);
    }

}
