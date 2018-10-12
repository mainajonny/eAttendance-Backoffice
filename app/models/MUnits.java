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
public class MUnits extends Model{
    @Id
    public long fid;

    @Constraints.Required
    public String UnitName;

    @Constraints.Required
    public String UnitCode;

    @Constraints.Required
    public String UnitProg;

    @Constraints.Required
    public String isActive;

    public static Model.Finder<Long, MUnits> findUnits = new Model.Finder<Long, MUnits>(Long.class, MUnits.class);

    public static List<MUnits> findAll() {
        return findUnits.all();
    }

    public static Map<String, String> unitoptions(){
        LinkedHashMap<String,String> options=new LinkedHashMap<String,String>();
        for(MUnits c: MUnits.findUnits.findList()){
            options.put(Long.toString(c.fid),c.UnitName);
        }
        return options;
    }

}
