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
public class MDept extends Model {
    @Id
    public long bid;

    @Constraints.Required
    public String DeptName;

    @Constraints.Required
    public String DeptHead;

    @Constraints.Required
    public String DeptFaculty;

    @Constraints.Required
    public String isActive;

    public static Model.Finder<Long, MDept> findDepartments = new Model.Finder<Long, MDept>(Long.class, MDept.class);

    public static List<MDept> findAll() {
        return findDepartments.all();
    }

    public static List<MDept> findByFaculty(String FacultyName) {
        return findDepartments.where().eq("DeptFaculty", FacultyName).findList();
    }

    public static MDept findDeptById(long id) {
        return findDepartments.where().eq("bid", id).findUnique();
    }

    public static Map<String, String> deptoptions(){
        LinkedHashMap<String,String> options=new LinkedHashMap<String,String>();
        for(MDept c: MDept.findDepartments.findList()){
            options.put(Long.toString(c.bid),c.DeptName);
        }
        return options;
    }
}
