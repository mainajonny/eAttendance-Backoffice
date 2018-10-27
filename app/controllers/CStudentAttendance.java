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

import views.html.Student_Attendance.*;

import java.util.Map;

/**
 * Created by Meech on 9/25/2018.
 */
public class CStudentAttendance extends Controller{

    /*public static Result renderStudentAttendance(){

        return ok(SelectUnit.render("View students attendance", MUnits.unitoptions()));
    }*/

    public static Result renderViewStudentAttendance(){

        /*String unit = String.valueOf(id);
        MStudentAttendance.findStudentsByUnit(String.valueOf(unit));*/
        return ok(ViewStudentAttendance.render("View students attendance"));
    }

    /*public static Result GetUnit(){
        DynamicForm requestform = Form.form().bindFromRequest();
        String Unit = requestform.get("Unit");

        Long Id = Long.valueOf(Unit);

        return redirect(routes.CStudentAttendance.renderViewStudentAttendance(Id));
    }*/

    /*public static Result addAttendance(){

        DynamicForm requestform = Form.form().bindFromRequest();
        String RegNumber = requestform.get("RegNumber");
        String Unit = requestform.get("Unit");
        //String StudentProg = requestform.get("StudentProg");
        String Lecturer = requestform.get("Lecturer");
        String UnitProg = requestform.get("UnitProg");
        String Attendance = requestform.get("Attendance");
        String StudentProg = MStud.findProgByStudent(RegNumber).Prog;

        String RegNo=String.valueOf(RegNumber);

        String StudentName=MStud.findStudentByRegNo(RegNo).StudName;

        System.out.print("StudentName:  "+StudentName);

        MStudentAttendance attendance = new MStudentAttendance();
        attendance.StudentName=StudentName;
        attendance.RegNumber=RegNumber;
        attendance.Unit=Unit;
        attendance.StudentProg =StudentProg;
        attendance.UnitProg =UnitProg;
        attendance.Lecturer =Lecturer;
        attendance.Attendance=Attendance;
        attendance.isUploaded="1";
        attendance.isActive="1";
        attendance.save();
        return redirect(routes.CStudentAttendance.renderViewStudentAttendance());
    }*/

    public static Result attendancelist() {

        Map<String, String[]> params = request().queryString();

        Integer iTotalRecords = MStudentAttendance.findStudentAttendance.findRowCount();
        String filter = params.get("sSearch")[0];
        Integer pageSize = Integer.valueOf(params.get("iDisplayLength")[0]);
        Integer page = Integer.valueOf(params.get("iDisplayStart")[0]) / pageSize;

        /**
         * Get sorting order and column
         */
        String sortBy = "Unit";
        String order = params.get("sSortDir_0")[0];

        switch (Integer.valueOf(params.get("iSortCol_0")[0])) {
            case 0:
                sortBy = "Unit";
                break;
            case 1:
                sortBy = "yid";
                break;
            case 2:
                sortBy = "StudentName";
                break;
            case 3:
                sortBy = "RegNumber";
                break;
            case 4:
                sortBy = "StudentProg";
                break;
            case 5:
                sortBy = "Lecturer";
                break;
            case 6:
                sortBy = "UnitProg";
                break;

        }

        /**
         * Get page to show from database
         * It is important to set setFetchAhead to false, since it doesn't benefit a stateless application at all.
         */
        Page<MStudentAttendance> areaPage = MStudentAttendance.findStudentAttendance.where(
                Expr.or(
                        Expr.ilike("RegNumber", "%" + filter + "%"),
                        Expr.or(
                                Expr.ilike("yid", "%" + filter + "%"),
                                Expr.ilike("StudentName", "%" + filter + "%")
                        )
                )
        )
                .orderBy(sortBy + " " + order + ", yid " + order)
                .findPagingList(pageSize).setFetchAhead(false)
                .getPage(page);

        Integer iTotalDisplayRecords = areaPage.getTotalRowCount();


        ObjectNode result = Json.newObject();

        result.put("sEcho", Integer.valueOf(params.get("sEcho")[0]));
        result.put("iTotalRecords", iTotalRecords);
        result.put("iTotalDisplayRecords", iTotalDisplayRecords);

        ArrayNode anc = result.putArray("aaData");

        for (MStudentAttendance cc : areaPage.getList()) {
            ObjectNode row = Json.newObject();

            row.put("RegNumber", cc.RegNumber);
            row.put("StudentName", cc.StudentName);
            row.put("Unit", cc.Unit);
            row.put("Lecturer", cc.Lecturer);
            row.put("StudentProg", cc.StudentProg);
            row.put("UnitProg", cc.UnitProg);
            row.put("Attendance", cc.Attendance);
            row.put("isUploaded", cc.isUploaded);
            row.put("yid", cc.yid);
            anc.add(row);
        }

        return ok(result);
    }
}
