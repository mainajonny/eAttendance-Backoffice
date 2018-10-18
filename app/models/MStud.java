package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Meech on 9/6/2018.
 */
@Entity
public class MStud extends Model{
    @Id
    public long gid;

    @Constraints.Required
    public String StudName;

    @Constraints.Required
    public String RegNo;

    @Constraints.Required
    public String Prog;

    @Constraints.Required
    public String Year;

    @Constraints.Required
    public String Sem;

    @Constraints.Required
    public String isActive;

    public static Model.Finder<Long, MStud> findStudents = new Model.Finder<Long, MStud>(Long.class, MStud.class);

    public static List<MStud> findAll() {
        return findStudents.all();
    }

    public static MStud findStudentByRegNo(String RegNo) {
        return findStudents.where().eq("RegNo", RegNo).findUnique();
    }

    public static MStud findStudentById(long id) {
        return findStudents.where().eq("gid", id).findUnique();
    }

    public static MStud findProgByStudent(String RegNo) {
        return findStudents.where().eq("RegNo", RegNo).findUnique();
    }

    public static Map<String, String> studentoptions(){
        LinkedHashMap<String,String> options=new LinkedHashMap<String,String>();
        for(MStud c: MStud.findStudents.findList()){
            options.put(Long.toString(c.gid),c.StudName);
        }
        return options;
    }
}
