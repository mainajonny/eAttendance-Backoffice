package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import models.MDept;
import models.MFaculty;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;
import play.libs.Json;
import play.mvc.*;
import org.apache.commons.io.FileUtils;

import views.html.Departments.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * Created by Meech on 9/6/2018.
 */
public class CDept extends Controller{

    public static Result renderDepts(){
        return ok(AddDept.render("Add Departments", MFaculty.options()));
    }

    public static Result renderViewDepts(){
        return ok(ViewDept.render("View Departments"));
    }

    public static Result addDept(){

        DynamicForm requestform = Form.form().bindFromRequest();
        String DeptName = requestform.get("DeptName");
        String DeptHead = requestform.get("DeptHead");
        String DeptFaculty = requestform.get("DeptFaculty");

        MDept depts = new MDept();
        depts.DeptName=DeptName;
        depts.DeptHead=DeptHead;
        depts.DeptFaculty=DeptFaculty;
        depts.isActive="1";
        depts.save();
        return renderViewDepts();
    }

    public static Result deptslist() {

        Map<String, String[]> params = request().queryString();

        Integer iTotalRecords = MDept.findDepartments.findRowCount();
        String filter = params.get("sSearch")[0];
        Integer pageSize = Integer.valueOf(params.get("iDisplayLength")[0]);
        Integer page = Integer.valueOf(params.get("iDisplayStart")[0]) / pageSize;

        /**
         * Get sorting order and column
         */
        String sortBy = "DeptName";
        String order = params.get("sSortDir_0")[0];

        switch (Integer.valueOf(params.get("iSortCol_0")[0])) {
            case 0:
                sortBy = "DeptName";
                break;
            case 1:
                sortBy = "bid";
                break;
            case 2:
                sortBy = "DeptHead";
                break;
            case 3:
                sortBy = "DeptFaculty";
                break;

        }

        /**
         * Get page to show from database
         * It is important to set setFetchAhead to false, since it doesn't benefit a stateless application at all.
         */
        Page<MDept> areaPage = MDept.findDepartments.where(
                Expr.or(
                        Expr.ilike("DeptName", "%" + filter + "%"),
                        Expr.or(
                                Expr.ilike("bid", "%" + filter + "%"),
                                Expr.ilike("DeptFaculty", "%" + filter + "%")
                        )
                )
        )
                .orderBy(sortBy + " " + order + ", bid " + order)
                .findPagingList(pageSize).setFetchAhead(false)
                .getPage(page);

        Integer iTotalDisplayRecords = areaPage.getTotalRowCount();


        ObjectNode result = Json.newObject();

        result.put("sEcho", Integer.valueOf(params.get("sEcho")[0]));
        result.put("iTotalRecords", iTotalRecords);
        result.put("iTotalDisplayRecords", iTotalDisplayRecords);

        ArrayNode anc = result.putArray("aaData");

        for (MDept cc : areaPage.getList()) {
            ObjectNode row = Json.newObject();
            //    System.out.println("in data table fetch: " + cc.RoomName);
            row.put("DeptName", cc.DeptName);
            row.put("DeptHead", cc.DeptHead);
            row.put("DeptFaculty", cc.DeptFaculty);
            row.put("bid", cc.bid);
            anc.add(row);
        }

        return ok(result);
    }
}
