package controllers;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import models.*;
import org.codehaus.jackson.node.ArrayNode;
import play.*;
import org.codehaus.jackson.node.ObjectNode;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Result;

import java.util.Map;

import static models.MStudentAttendance.findStudentByRegNoUnitandLec;


/**
 * Created by Meech on 9/6/2018.
 */
public class MobileApi extends Controller {

    public static Result AuthenticateUser(){

        ObjectNode result;
        result = Json.newObject();

        DynamicForm requestform = Form.form().bindFromRequest();
        String LecEmail = requestform.get("LecEmail");
        String LecPassword = requestform.get("LecPassword");

        if (MLecs.findLecByEmail(LecEmail).size() > 0){

            String isActive = MLecs.findLecByEmail(LecEmail).get(0).isActive;

        System.out.println("Authenticating lecturer: "+LecEmail+" password: "+LecPassword+" isActive: "+isActive);

            if (MLecs.authenticateUser(LecEmail, LecPassword, isActive)!=null){

                if (isActive.equals("1")) {
                    result.put("responseCode", "200");

                }else {
                    result.put("responseCode", "202");

                }

            }else{
                result.put("responseCode", "201");

            }

        }else {
            System.out.println("Authenticating lecturer: "+LecEmail+" password: "+LecPassword);
            result.put("responseCode", "201");
        }



        return ok(result);
    }


    public static Result ReturnAllFaculty(){
        System.out.println("Getting faculties");
        return ok(Json.toJson(MFaculty.findAll()));

    }


    public static Result returnAllLecs(){
        System.out.println("Getting lecs");
        return ok(Json.toJson(MLecs.findAll()));

    }


    public  static  Result returnDepartments(){
        ObjectNode result;
        result = Json.newObject();

        DynamicForm requestform = Form.form().bindFromRequest();
        String FacultyName = requestform.get("FName");
        //System.out.print("Faculty Name: "+FacultyName);
        System.out.print("Getting..."+FacultyName);

        result.put("responseCode", Json.toJson(MDept.findByFaculty(FacultyName)));
        return  ok(result);

    }


    public static Result returnAssignedUnits(){
        ObjectNode result;
        result = Json.newObject();

        DynamicForm requestform=Form.form().bindFromRequest();
        String AssignDept = requestform.get("AssignDept");
        String AssignLec = requestform.get("AssignLecEmail");


        System.out.println("Getting..."+AssignLec);
        System.out.println("Getting..."+AssignDept);

        if(MAssignUnit.findUnitByEmailandDept(AssignLec, AssignDept) != null){
                result.put("responseCode", Json.toJson(MAssignUnit.findUnitByEmailandDept(AssignLec, AssignDept)));
            }

            return ok(result);

        }

    public static Result uploadattendance(){

        ObjectNode result;
        result = Json.newObject();

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

            if(findStudentByRegNoUnitandLec(RegNumber, Unit, Lecturer) != null){

                    //get the id of that column you want to update
                    Long aid= MStudentAttendance.findStudentByRegNoUnitandLec(RegNumber, Unit, Lecturer).yid;

                    MStudentAttendance mStudentAttendance=new MStudentAttendance();
                    //update the attendance at that column with that id
                    mStudentAttendance.yid=aid;
                    mStudentAttendance.Attendance=Attendance;
                    mStudentAttendance.update();

                result.put("responseCode", "204");
            }
            else{

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

                result.put("responseCode", "204");
            }

        System.out.println("Getting..."+StudentName);
        System.out.println("Getting..."+RegNumber);
        System.out.println("Getting..."+StudentProg);
        System.out.println("Getting..."+Unit);
        System.out.println("Getting..."+UnitProg);
        System.out.println("Getting..."+Lecturer);
        System.out.println("Getting..."+Attendance);
        //System.out.println("Getting..."+MStudentAttendance.findAll());

        return ok(result);
    }

    public static Result returnAllStudents(){
        System.out.println("Getting students");
        return ok(Json.toJson(MStud.findAll()));

    }

    public static Result returnProfiledetails(){
        ObjectNode result;
        result = Json.newObject();

        DynamicForm requestform=Form.form().bindFromRequest();
        String LecEmail = requestform.get("LecEmail");

        System.out.println("Getting..."+LecEmail);

        if(MLecs.findLecByEmail(LecEmail) != null){
            result.put("responseCode", Json.toJson(MLecs.findLecByEmail(LecEmail)));
        }

        return ok(result);

    }

    public static Result Updateprofile(){

        ObjectNode result;
        result = Json.newObject();

        DynamicForm requestform = Form.form().bindFromRequest();
        String LecName = requestform.get("LecName");
        String IdNo = requestform.get("IdNo");
        String LecDept = requestform.get("LecDept");
        String LecEmail = requestform.get("LecEmail");
        String LecPassword = requestform.get("LecPassword");


            //get the id of that column you want to update
            Long aid= MLecs.findLecByIdNo(IdNo).cid;

            MLecs mLecs = new MLecs();
            //update the lecturer at that column with that id
            mLecs.cid=aid;
            mLecs.LecName=LecName;
            mLecs.LecDept=LecDept;
            mLecs.LecEmail=LecEmail;
            mLecs.LecPassword=LecPassword;
            mLecs.update();

            result.put("responseCode", "215");

        System.out.println("Getting..."+LecName);
        System.out.println("Getting..."+IdNo);
        System.out.println("Getting..."+LecDept);
        System.out.println("Getting..."+LecEmail);
        System.out.println("Getting..."+LecPassword);

        return ok(result);
    }

}
