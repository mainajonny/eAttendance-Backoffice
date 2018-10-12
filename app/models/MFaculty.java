package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;
import javax.persistence.ManyToOne;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Meech on 9/6/2018.
 */
@Entity
public class MFaculty extends Model {
    @Id
    public long did;

    @Constraints.Required
    public String FName;

    @Constraints.Required
    public String FInt;

    @Constraints.Required
    public String FMajor;

    @Constraints.Required
    public String isActive;

    public static Model.Finder<Long, MFaculty> findFaculty = new Model.Finder<Long, MFaculty>(Long.class, MFaculty.class);

    public static List<MFaculty> findAll() {
        return findFaculty.all();
    }

    public static Map<String, String> options(){
        LinkedHashMap<String,String> options=new LinkedHashMap<String,String>();
        for(MFaculty c: MFaculty.findFaculty.findList()){
            options.put(Long.toString(c.did),c.FName);
        }
        return options;
    }
}
