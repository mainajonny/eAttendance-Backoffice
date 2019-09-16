package models;

import controllers.CUnits;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import models.MUnits;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Meech on 9/6/2018.
 */
@Entity
public class MProgs extends Model {
    @Id
    public long eid;

    @Constraints.Required
    public String ProgName;

    @Constraints.Required
    public String ProgDept;

    @Constraints.Required
    public String ProgLevel;

    @Constraints.Required
    public String isActive;

    public static Model.Finder<Long, MProgs> findProgrammes = new Model.Finder<Long, MProgs>(Long.class, MProgs.class);

    public static List<MProgs> findAll() {
        return findProgrammes.all();
    }

    public static MProgs findDeptByCourse(String ProgName) {
        return findProgrammes.where().eq("ProgName", ProgName).findUnique();
    }

    public static MProgs findProgById(long id) {
        return findProgrammes.where().eq("eid", id).findUnique();
    }

    public static List<MProgs> findProgByDept(String ProgDept) {
        return findProgrammes.where().eq("ProgDept", ProgDept).findList();
    }

    public static Map<String, String> options(){
        LinkedHashMap<String,String> options=new LinkedHashMap<String,String>();
        for(MProgs c: MProgs.findProgrammes.findList()){
            options.put(Long.toString(c.eid),c.ProgName);
        }
        return options;
    }

}
