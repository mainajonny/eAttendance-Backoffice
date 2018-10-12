package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
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

import views.html.Faculty.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * Created by Meech on 9/6/2018.
 */
public class CFaculty extends Controller {

    public static Result renderFaculty(){
        return ok(AddFaculty.render("Add Faculty"));
    }

    public static Result renderViewFaculty(){
        return ok(ViewFaculty.render("View Faculty"));
    }

    public static Result addFaculty(){

        DynamicForm requestform = Form.form().bindFromRequest();
        String FName = requestform.get("FName");
        String FInt = requestform.get("FInt");
        String FMajor = requestform.get("FMajor");

        MFaculty faculty = new MFaculty();
        faculty.FName=FName;
        faculty.FInt=FInt;
        faculty.FMajor=FMajor;
        faculty.isActive="1";
        faculty.save();
        return renderViewFaculty();
    }

    public static Result facultylist() {

        Map<String, String[]> params = request().queryString();

        Integer iTotalRecords = MFaculty.findFaculty.findRowCount();
        String filter = params.get("sSearch")[0];
        Integer pageSize = Integer.valueOf(params.get("iDisplayLength")[0]);
        Integer page = Integer.valueOf(params.get("iDisplayStart")[0]) / pageSize;

        /**
         * Get sorting order and column
         */
        String sortBy = "FName";
        String order = params.get("sSortDir_0")[0];

        switch (Integer.valueOf(params.get("iSortCol_0")[0])) {
            case 0:
                sortBy = "FName";
                break;
            case 1:
                sortBy = "did";
                break;
            case 2:
                sortBy = "FInt";
                break;
            case 3:
                sortBy = "FMajor";
                break;

        }

        /**
         * Get page to show from database
         * It is important to set setFetchAhead to false, since it doesn't benefit a stateless application at all.
         */
        Page<MFaculty> areaPage = MFaculty.findFaculty.where(
                Expr.or(
                        Expr.ilike("FName", "%" + filter + "%"),
                        Expr.or(
                                Expr.ilike("did", "%" + filter + "%"),
                                Expr.ilike("FInt", "%" + filter + "%")
                        )
                )
        )
                .orderBy(sortBy + " " + order + ", did " + order)
                .findPagingList(pageSize).setFetchAhead(false)
                .getPage(page);

        Integer iTotalDisplayRecords = areaPage.getTotalRowCount();


        ObjectNode result = Json.newObject();

        result.put("sEcho", Integer.valueOf(params.get("sEcho")[0]));
        result.put("iTotalRecords", iTotalRecords);
        result.put("iTotalDisplayRecords", iTotalDisplayRecords);

        ArrayNode anc = result.putArray("aaData");

        for (MFaculty cc : areaPage.getList()) {
            ObjectNode row = Json.newObject();
            //    System.out.println("in data table fetch: " + cc.RoomName);
            row.put("FName", cc.FName);
            row.put("FInt", cc.FInt);
            row.put("FMajor", cc.FMajor);
            row.put("did", cc.did);
            anc.add(row);
        }

        return ok(result);
    }
}
