package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by Meech on 9/19/2018.
 */
@Entity
public class MAssignUnit extends Model{
    @Id
    public long zid;

    @Constraints.Required
    public String AssignProg;

    @Constraints.Required
    public String AssignDept;

    @Constraints.Required
    public String AssignUnit;

    @Constraints.Required
    public String AssignLec;

    @Constraints.Required
    public String AssignLecEmail;

    @Constraints.Required
    public String AssignLecName;

    @Constraints.Required
    public String isActive;

    public static Model.Finder<Long, MAssignUnit> findAssignedUnits = new Model.Finder<Long, MAssignUnit>(Long.class, MAssignUnit.class);

    public static List<MAssignUnit> findAll() {
        return findAssignedUnits.all();
    }

    public static MAssignUnit findAssignedUnitById(long id){
        return findAssignedUnits.where().eq("zid", id).findUnique();
    }

    public static List<MAssignUnit> findUnitByEmailandDept(String AssignLecEmail, String AssignDept) {
        return findAssignedUnits.where().eq("AssignLecEmail", AssignLecEmail).eq("AssignDept", AssignDept).findList();
    }


}
