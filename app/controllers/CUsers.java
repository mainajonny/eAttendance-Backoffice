package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import models.MUsers;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;
import play.libs.Json;
import play.mvc.*;
import org.apache.commons.io.FileUtils;

import views.html.Users.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Meech on 9/6/2018.
 */
public class CUsers extends Controller {

    public static Result renderUsers(){
        return ok(AddUsers.render("Add Users"));
    }

    public static Result renderViewUsers(){
        return ok(ViewUsers.render("Users List"));
    }

    public static Result addUsers(){

        DynamicForm requestform = Form.form().bindFromRequest();
        String Email = requestform.get("Email");
        String Password = requestform.get("Password");

        MUsers users = new MUsers();
        users.Email=Email;
        users.Password=Password;
        users.isActive="1";
        users.save();
        return redirect(routes.CUsers.renderViewUsers());
    }

    public static Result renderEditUsers(Long id) {

        MUsers mUsers= MUsers.findUserById(id);
        return ok(EditUsers.render("Edit User", mUsers));
    }

    public static Result EditUsers(Long id){
        MUsers mUsers=new MUsers();
        mUsers.aid=id;
        mUsers.isActive="1";
        mUsers.Email=Form.form().bindFromRequest().get("Email");
        mUsers.Password=Form.form().bindFromRequest().get("Password");
        Ebean.update(mUsers);

        return redirect(routes.CUsers.renderViewUsers());
    }

    public static Result ActivateUser(String id){
        System.out.println("activate"+ id);
        MUsers mUsers = new MUsers();
        mUsers.aid = Long.valueOf(id);
        mUsers.isActive = "1";
        mUsers.update();

        return redirect(routes.CUsers.renderViewUsers());
    }

    public static Result DeactivateUser(String id) {
        System.out.println("Deactivate" + id);
        MUsers mUsers = new MUsers();
        mUsers.aid = Long.valueOf(id);
        mUsers.isActive = "0";
        mUsers.update();

        return redirect(routes.CUsers.renderViewUsers());
    }

    /*public static  Result deleteUsers(Long id){

        MUsers mUsers=MUsers.findUserById(id);
        mUsers.delete();

        return  redirect(routes.CUsers.renderViewUsers());
    }*/

    public static Result userslist() {

        Map<String, String[]> params = request().queryString();

        Integer iTotalRecords = MUsers.findUsers.findRowCount();
        String filter = params.get("sSearch")[0];
        Integer pageSize = Integer.valueOf(params.get("iDisplayLength")[0]);
        Integer page = Integer.valueOf(params.get("iDisplayStart")[0]) / pageSize;

        /**
         * Get sorting order and column
         */
        String sortBy = "Email";
        String order = params.get("sSortDir_0")[0];

        switch (Integer.valueOf(params.get("iSortCol_0")[0])) {
            case 0:
                sortBy = "Email";
                break;
            case 1:
                sortBy = "aid";
                break;
            case 2:
                sortBy = "Password";
                break;

        }

        /**
         * Get page to show from database
         * It is important to set setFetchAhead to false, since it doesn't benefit a stateless application at all.
         */
        Page<MUsers> areaPage = MUsers.findUsers.where(
                Expr.or(
                        Expr.ilike("Email", "%" + filter + "%"),
                        Expr.or(
                                Expr.ilike("aid", "%" + filter + "%"),
                                Expr.ilike("Password", "%" + filter + "%")
                        )
                )
        )
                .orderBy(sortBy + " " + order + ", aid " + order)
                .findPagingList(pageSize).setFetchAhead(false)
                .getPage(page);

        Integer iTotalDisplayRecords = areaPage.getTotalRowCount();


        ObjectNode result = Json.newObject();

        result.put("sEcho", Integer.valueOf(params.get("sEcho")[0]));
        result.put("iTotalRecords", iTotalRecords);
        result.put("iTotalDisplayRecords", iTotalDisplayRecords);

        ArrayNode anc = result.putArray("aaData");

        for (MUsers cc : areaPage.getList()) {
            ObjectNode row = Json.newObject();
            //    System.out.println("in data table fetch: " + cc.RoomName);
            row.put("Email", cc.Email);
            row.put("Password", cc.Password);
            row.put("isActive", cc.isActive);
            row.put("aid", cc.aid);
            anc.add(row);
        }

        return ok(result);
    }






}
