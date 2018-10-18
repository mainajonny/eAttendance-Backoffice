package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

/**
 * Created by Meech on 9/6/2018.
 */
@Entity
public class MUsers extends Model{

    @Id
    public long aid;

    @Constraints.Required
    public String Email;

    @Constraints.Required
    public String Password;

    @Constraints.Required
    public String isActive;

    public static Model.Finder<Long, MUsers> findUsers = new Model.Finder<Long, MUsers>(Long.class, MUsers.class);

    public static List<MUsers> findAll() {
        return findUsers.all();
    }

    public static MUsers findUserById(long id){
        return findUsers.where().eq("aid", id).findUnique();
    }

}
