package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import models.*;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;
import play.libs.Json;
import play.mvc.*;
import org.apache.commons.io.FileUtils;

import views.html.Assign_Unit.*;

import java.util.Map;


/**
 * Created by Meech on 9/19/2018.
 */
public class CAssignUnit extends Controller {

    public static Result renderAssignedUnits(){
        return ok(AssignUnit.render("Assign Units",  MDept.deptoptions(), MProgs.options(), MUnits.unitoptions(), MLecs.lecoptions()));
    }

    public static Result renderViewAssignedUnits(){
        return ok(ViewAssignedUnits.render("View assigned units"));
    }

    public static Result assignUnits(){

        DynamicForm requestform = Form.form().bindFromRequest();
        String AssignProg = requestform.get("AssignProg");
        String AssignUnit = requestform.get("AssignUnit");
        String AssignLec = requestform.get("AssignLec");
        String AssignDept = MProgs.findDeptByCourse(AssignProg).ProgDept;

        long LecId=Long.valueOf(AssignLec);

        String AssignLecName=MLecs.findLecById(LecId).LecName;
        String AssignLecEmail=MLecs.findLecById(LecId).LecEmail;

        /*System.out.print("LecName:  "+AssignLecName);
        System.out.print("LecEmail:  "+AssignLecEmail);*/

        MAssignUnit assignunits = new MAssignUnit();
        assignunits.AssignProg=AssignProg;
        assignunits.AssignDept=AssignDept;
        assignunits.AssignUnit=AssignUnit;
        assignunits.AssignLec =AssignLec;
        assignunits.AssignLecName =AssignLecName;
        assignunits.AssignLecEmail =AssignLecEmail;
        assignunits.isActive="1";
        assignunits.save();
        return renderViewAssignedUnits();
    }

    public static Result assignedunitslist() {

        Map<String, String[]> params = request().queryString();

        Integer iTotalRecords = MAssignUnit.findAssignedUnits.findRowCount();
        String filter = params.get("sSearch")[0];
        Integer pageSize = Integer.valueOf(params.get("iDisplayLength")[0]);
        Integer page = Integer.valueOf(params.get("iDisplayStart")[0]) / pageSize;

        /**
         * Get sorting order and column
         */
        String sortBy = "AssignUnit";
        String order = params.get("sSortDir_0")[0];

        switch (Integer.valueOf(params.get("iSortCol_0")[0])) {
            case 0:
                sortBy = "AssignUnit";
                break;
            case 1:
                sortBy = "zid";
                break;
            case 2:
                sortBy = "AssignDept";
                break;
            case 3:
                sortBy = "AssignProg";
                break;
            case 4:
                sortBy = "AssignLec";
                break;
            case 5:
                sortBy = "AssignLecName";
                break;
            case 6:
                sortBy = "AssignLecEmail";
                break;

        }

        /**
         * Get page to show from database
         * It is important to set setFetchAhead to false, since it doesn't benefit a stateless application at all.
         */
        Page<MAssignUnit> areaPage = MAssignUnit.findAssignedUnits.where(
                Expr.or(
                        Expr.ilike("AssignUnit", "%" + filter + "%"),
                        Expr.or(
                                Expr.ilike("zid", "%" + filter + "%"),
                                Expr.ilike("AssignLec", "%" + filter + "%")
                        )
                )
        )
                .orderBy(sortBy + " " + order + ", zid " + order)
                .findPagingList(pageSize).setFetchAhead(false)
                .getPage(page);

        Integer iTotalDisplayRecords = areaPage.getTotalRowCount();


        ObjectNode result = Json.newObject();

        result.put("sEcho", Integer.valueOf(params.get("sEcho")[0]));
        result.put("iTotalRecords", iTotalRecords);
        result.put("iTotalDisplayRecords", iTotalDisplayRecords);

        ArrayNode anc = result.putArray("aaData");

        for (MAssignUnit cc : areaPage.getList()) {
            ObjectNode row = Json.newObject();
            //    System.out.println("in data table fetch: " + cc.RoomName);
            row.put("AssignProg", cc.AssignProg);
            row.put("AssignDept", cc.AssignDept);
            row.put("AssignUnit", cc.AssignUnit);
            row.put("AssignLec", cc.AssignLec);
            row.put("AssignLecName", cc.AssignLecName);
            row.put("AssignLecEmail", cc.AssignLecEmail);
            row.put("zid", cc.zid);
            anc.add(row);
        }

        return ok(result);
    }
}
