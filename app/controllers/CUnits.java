package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import models.MLecs;
import models.MProgs;
import models.MUnits;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;
import play.libs.Json;
import play.mvc.*;
import org.apache.commons.io.FileUtils;

import views.html.Units.*;

import java.util.Map;
/**
 * Created by Meech on 9/6/2018.
 */
public class CUnits extends Controller {

    public static Result renderUnits(){
        return ok(AddUnits.render("Add Units", MProgs.options()));
    }

    public static Result renderViewUnits(){
        return ok(ViewUnits.render("View Units"));
    }

    public static Result addUnits(){

        DynamicForm requestform = Form.form().bindFromRequest();
        String UnitName = requestform.get("UnitName");
        String UnitCode = requestform.get("UnitCode");
        String UnitProg = requestform.get("UnitProg");

        MUnits units = new MUnits();
        units.UnitName=UnitName;
        units.UnitCode=UnitCode;
        units.UnitProg =UnitProg;
        units.isActive="1";
        units.save();
        return renderViewUnits();
    }

    public static Result unitslist() {

        Map<String, String[]> params = request().queryString();

        Integer iTotalRecords = MUnits.findUnits.findRowCount();
        String filter = params.get("sSearch")[0];
        Integer pageSize = Integer.valueOf(params.get("iDisplayLength")[0]);
        Integer page = Integer.valueOf(params.get("iDisplayStart")[0]) / pageSize;

        /**
         * Get sorting order and column
         */
        String sortBy = "UnitName";
        String order = params.get("sSortDir_0")[0];

        switch (Integer.valueOf(params.get("iSortCol_0")[0])) {
            case 0:
                sortBy = "UnitName";
                break;
            case 1:
                sortBy = "fid";
                break;
            case 2:
                sortBy = "UnitProg";
                break;
            case 3:
                sortBy = "UnitCode";
                break;

        }

        /**
         * Get page to show from database
         * It is important to set setFetchAhead to false, since it doesn't benefit a stateless application at all.
         */
        Page<MUnits> areaPage = MUnits.findUnits.where(
                Expr.or(
                        Expr.ilike("UnitName", "%" + filter + "%"),
                        Expr.or(
                                Expr.ilike("fid", "%" + filter + "%"),
                                Expr.ilike("UnitCode", "%" + filter + "%")
                        )
                )
        )
                .orderBy(sortBy + " " + order + ", fid " + order)
                .findPagingList(pageSize).setFetchAhead(false)
                .getPage(page);

        Integer iTotalDisplayRecords = areaPage.getTotalRowCount();


        ObjectNode result = Json.newObject();

        result.put("sEcho", Integer.valueOf(params.get("sEcho")[0]));
        result.put("iTotalRecords", iTotalRecords);
        result.put("iTotalDisplayRecords", iTotalDisplayRecords);

        ArrayNode anc = result.putArray("aaData");

        for (MUnits cc : areaPage.getList()) {
            ObjectNode row = Json.newObject();
            //    System.out.println("in data table fetch: " + cc.RoomName);
            row.put("UnitName", cc.UnitName);
            row.put("UnitCode", cc.UnitCode);
            row.put("UnitProg", cc.UnitProg);
            row.put("fid", cc.fid);
            anc.add(row);
        }

        return ok(result);
    }
}
