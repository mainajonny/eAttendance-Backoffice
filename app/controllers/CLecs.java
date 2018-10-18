package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import models.MDept;
import models.MLecs;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;
import play.libs.Json;
import play.mvc.*;
import org.apache.commons.io.FileUtils;

import views.html.Lecturers.*;

import java.util.Map;

/**
 * Created by Meech on 9/6/2018.
 */
public class CLecs extends Controller {

    public static Result renderLecs(){
        return ok(AddLec.render("Add Lecturers", MDept.deptoptions()));
    }

    public static Result renderViewLecs(){
        return ok(ViewLec.render("Lecturers"));
    }

    public static Result addLec(){

        DynamicForm requestform = Form.form().bindFromRequest();
        String LecName = requestform.get("LecName");
        String IdNo = requestform.get("IdNo");
        String LecDept = requestform.get("LecDept");
        String LecEmail = requestform.get("LecEmail");
        String LecPassword = requestform.get("LecPassword");

        MLecs lecs = new MLecs();
        lecs.LecName=LecName;
        lecs.IdNo=IdNo;
        lecs.LecDept=LecDept;
        lecs.LecEmail=LecEmail;
        lecs.LecPassword=LecPassword;
        lecs.isActive="1";
        lecs.save();

        return redirect(routes.CLecs.renderViewLecs());
    }

    public static Result renderEditLec(Long id) {

        MLecs mLecs= MLecs.findLecById(id);
        return ok(EditLec.render("Edit Lecturer", mLecs, MDept.deptoptions()));
    }

    public static Result EditLecturer(Long id){
        MLecs mLecs=new MLecs();
        mLecs.cid=id;
        mLecs.isActive="1";
        mLecs.IdNo=Form.form().bindFromRequest().get("IdNo");
        mLecs.LecName=Form.form().bindFromRequest().get("LecName");
        mLecs.LecDept=Form.form().bindFromRequest().get("LecDept");
        mLecs.LecEmail=Form.form().bindFromRequest().get("LecEmail");
        mLecs.LecPassword=Form.form().bindFromRequest().get("LecPassword");
        Ebean.update(mLecs);

        return redirect(routes.CLecs.renderViewLecs());
    }

    public static  Result deleteLec(Long id){

        MLecs mLecs=MLecs.findLecById(id);
        mLecs.delete();

        return  redirect(routes.CLecs.renderViewLecs());
    }

    public static Result lecslist() {

        Map<String, String[]> params = request().queryString();

        Integer iTotalRecords = MLecs.findLecturers.findRowCount();
        String filter = params.get("sSearch")[0];
        Integer pageSize = Integer.valueOf(params.get("iDisplayLength")[0]);
        Integer page = Integer.valueOf(params.get("iDisplayStart")[0]) / pageSize;

        /**
         * Get sorting order and column
         */
        String sortBy = "LecName";
        String order = params.get("sSortDir_0")[0];

        switch (Integer.valueOf(params.get("iSortCol_0")[0])) {
            case 0:
                sortBy = "LecName";
                break;
            case 1:
                sortBy = "cid";
                break;
            case 2:
                sortBy = "LecDept";
                break;
            case 3:
                sortBy = "IdNo";
                break;
            case 4:
                sortBy = "LecEmail";
                break;
            case 5:
                sortBy = "LecPassword";
                break;

        }

        /**
         * Get page to show from database
         * It is important to set setFetchAhead to false, since it doesn't benefit a stateless application at all.
         */
        Page<MLecs> areaPage = MLecs.findLecturers.where(
                Expr.or(
                        Expr.ilike("LecName", "%" + filter + "%"),
                        Expr.or(
                                Expr.ilike("cid", "%" + filter + "%"),
                                Expr.ilike("LecEmail", "%" + filter + "%")
                        )
                )
        )
                .orderBy(sortBy + " " + order + ", cid " + order)
                .findPagingList(pageSize).setFetchAhead(false)
                .getPage(page);

        Integer iTotalDisplayRecords = areaPage.getTotalRowCount();


        ObjectNode result = Json.newObject();

        result.put("sEcho", Integer.valueOf(params.get("sEcho")[0]));
        result.put("iTotalRecords", iTotalRecords);
        result.put("iTotalDisplayRecords", iTotalDisplayRecords);

        ArrayNode anc = result.putArray("aaData");

        for (MLecs cc : areaPage.getList()) {
            ObjectNode row = Json.newObject();
            //    System.out.println("in data table fetch: " + cc.RoomName);
            row.put("LecName", cc.LecName);
            row.put("IdNo", cc.IdNo);
            row.put("LecDept", cc.LecDept);
            row.put("LecEmail", cc.LecEmail);
            row.put("LecPassword", cc.LecPassword);
            row.put("cid", cc.cid);
            anc.add(row);
        }

        return ok(result);
    }

}
