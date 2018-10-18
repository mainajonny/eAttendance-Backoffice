package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import models.MProgs;
import models.MStud;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;
import play.libs.Json;
import play.mvc.*;
import org.apache.commons.io.FileUtils;

import views.html.Students.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * Created by Meech on 9/6/2018.
 */
public class CStud extends Controller {

    public static Result renderStuds(){
        return ok(AddStuds.render("Add Students", MProgs.options()));
    }

    public static Result renderViewStuds(){
        return ok(ViewStuds.render("View Students"));
    }

    public static Result addStuds(){

        DynamicForm requestform = Form.form().bindFromRequest();
        String StudName = requestform.get("StudName");
        String RegNo = requestform.get("RegNo");
        String Prog = requestform.get("Prog");
        String Year = requestform.get("Year");
        String Sem = requestform.get("Sem");

        MStud stud = new MStud();
        stud.StudName=StudName;
        stud.RegNo=RegNo;
        stud.Prog=Prog;
        stud.Year=Year;
        stud.Sem=Sem;
        stud.isActive="1";
        stud.save();
        return redirect(routes.CStud.renderViewStuds());
    }

    public static Result renderEditStudent(Long id) {

        MStud mStud= MStud.findStudentById(id);
        return ok(EditStuds.render("Edit Students", mStud, MProgs.options()));
    }

    public static Result EditStudent(Long id){
        MStud mStud=new MStud();
        mStud.gid=id;
        mStud.isActive="1";
        mStud.StudName=Form.form().bindFromRequest().get("StudName");
        mStud.RegNo=Form.form().bindFromRequest().get("RegNo");
        mStud.Prog=Form.form().bindFromRequest().get("Prog");
        mStud.Year=Form.form().bindFromRequest().get("Year");
        mStud.Sem=Form.form().bindFromRequest().get("Sem");
        Ebean.update(mStud);

        return redirect(routes.CStud.renderViewStuds());
    }

    public static  Result deleteStudent(Long id){

        MStud mStud=MStud.findStudentById(id);
        mStud.delete();

        return  redirect(routes.CStud.renderViewStuds());
    }

    public static Result studslist() {

        Map<String, String[]> params = request().queryString();

        Integer iTotalRecords = MStud.findStudents.findRowCount();
        String filter = params.get("sSearch")[0];
        Integer pageSize = Integer.valueOf(params.get("iDisplayLength")[0]);
        Integer page = Integer.valueOf(params.get("iDisplayStart")[0]) / pageSize;

        /**
         * Get sorting order and column
         */
        String sortBy = "RegNo";
        String order = params.get("sSortDir_0")[0];

        switch (Integer.valueOf(params.get("iSortCol_0")[0])) {
            case 0:
                sortBy = "RegNo";
                break;
            case 1:
                sortBy = "gid";
                break;
            case 2:
                sortBy = "StudName";
                break;
            case 3:
                sortBy = "Prog";
                break;
            case 4:
                sortBy = "Year";
                break;
            case 5:
                sortBy = "Sem";
                break;

        }

        /**
         * Get page to show from database
         * It is important to set setFetchAhead to false, since it doesn't benefit a stateless application at all.
         */
        Page<MStud> areaPage = MStud.findStudents.where(
                Expr.or(
                        Expr.ilike("StudName", "%" + filter + "%"),
                        Expr.or(
                                Expr.ilike("gid", "%" + filter + "%"),
                                Expr.ilike("RegNo", "%" + filter + "%")
                        )
                )
        )
                .orderBy(sortBy + " " + order + ", gid " + order)
                .findPagingList(pageSize).setFetchAhead(false)
                .getPage(page);

        Integer iTotalDisplayRecords = areaPage.getTotalRowCount();


        ObjectNode result = Json.newObject();

        result.put("sEcho", Integer.valueOf(params.get("sEcho")[0]));
        result.put("iTotalRecords", iTotalRecords);
        result.put("iTotalDisplayRecords", iTotalDisplayRecords);

        ArrayNode anc = result.putArray("aaData");

        for (MStud cc : areaPage.getList()) {
            ObjectNode row = Json.newObject();
            //    System.out.println("in data table fetch: " + cc.RoomName);
            row.put("StudName", cc.StudName);
            row.put("RegNo", cc.RegNo);
            row.put("Prog", cc.Prog);
            row.put("Year", cc.Year);
            row.put("Sem", cc.Sem);
            row.put("gid", cc.gid);
            anc.add(row);
        }

        return ok(result);
    }
}
