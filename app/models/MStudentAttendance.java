package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

/**
 * Created by Meech on 9/25/2018.
 */
@Entity
public class MStudentAttendance extends Model{
    @Id
    public long yid;

    @Constraints.Required
    public String StudentName;

    @Constraints.Required
    public String RegNumber;

    @Constraints.Required
    public String StudentProg;

    @Constraints.Required
    public String Unit;

    @Constraints.Required
    public String UnitProg;

    @Constraints.Required
    public String Lecturer;

    @Constraints.Required
    public String Attendance;

    @Constraints.Required
    public String isUploaded;

    @Constraints.Required
    public String isActive;

    public static Model.Finder<Long, MStudentAttendance> findStudentAttendance = new Model.Finder<Long,
            MStudentAttendance>(Long.class, MStudentAttendance.class);

    public static List<MStudentAttendance> findAll() {
        return findStudentAttendance.all();
    }

    public static MStudentAttendance findStudentByRegNoUnitandLec(String RegNumber, String Unit, String Lecturer) {
        return findStudentAttendance.where().eq("RegNumber", RegNumber).eq("Unit", Unit).eq("Lecturer", Lecturer).findUnique();
    }

    public static List<MStudentAttendance> findStudentsByUnit(String Unit) {
        return findStudentAttendance.where().eq("Unit", Unit).findList();
    }

    public static MStudentAttendance findStudentById(Long yid) {
        return findStudentAttendance.where().eq("yid", yid).findUnique();
    }

}
