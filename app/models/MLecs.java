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
public class MLecs extends Model {
    @Id
    public long cid;

    @Constraints.Required
    public String LecName;

    @Constraints.Required
    public String IdNo;

    @Constraints.Required
    public String LecDept;

    @Constraints.Required
    public String LecEmail;

    @Constraints.Required
    public String LecPassword;

    @Constraints.Required
    public String isActive;

    public static MLecs authenticateUser(String Email, String Pass, String isActive){
        return  MLecs.findLecturers.where().eq("LecEmail", Email).eq("LecPassword", Pass).eq("isActive", isActive).findUnique();
    }

    public static Model.Finder<Long, MLecs> findLecturers = new Model.Finder<Long, MLecs>(Long.class, MLecs.class);

    public static List<MLecs> findAll() {
        return findLecturers.all();
    }

    public static MLecs findLecById(long id) {
        return findLecturers.where().eq("cid", id).findUnique();
    }

    public static List<MLecs> findLecByEmail(String LecEmail) {
        return findLecturers.where().eq("LecEmail", LecEmail).findList();
    }

    public static MLecs findLecturerByEmail(String LecEmail) {
        return findLecturers.where().eq("LecEmail", LecEmail).findUnique();
    }

    public static MLecs findLecByIdNo(String IdNo) {
        return findLecturers.where().eq("IdNo", IdNo).findUnique();
    }

    public static Map<String, String> lecoptions(){
        LinkedHashMap<String,String> options=new LinkedHashMap<String,String>();
        for(MLecs c: MLecs.findLecturers.findList()){
            options.put(Long.toString(c.cid),c.LecName);
        }
        return options;
    }
}
