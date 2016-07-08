package nl.quintor.ayic.mss.category.services;

import nl.quintor.ayic.mss.authentication.domain.Secured;
import nl.quintor.ayic.mss.category.dao.CategoryDao;
import nl.quintor.ayic.mss.category.dao.ICategoryDAO;
import nl.quintor.ayic.mss.category.domain.Category;
import nl.quintor.ayic.mss.contact.domain.Contact;
import nl.quintor.ayic.mss.response.ResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/categories")
public class CategoryService implements ICategoryService {

    private static final String constCategory = "category";
    private static final Logger LOGGER = Logger.getLogger(String.valueOf(CategoryService.class));
    private ICategoryDAO dao;

    public CategoryService() throws SQLException, IOException {
        dao = new CategoryDao();
    }

    public CategoryService(ICategoryDAO daoParam){
        dao = daoParam;
    }

    @Override
    @POST
    @Path("/addCategory")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response addCategory(Category category) throws SQLException {
        try{
            dao.addCategory(category);
            String result = "Category added";
            return Response.status(201).entity(result).build();
        }catch(Exception e){
            LOGGER.log(Level.INFO,"Couldn't add category", e);
            return Response.status(409).entity("Couldn't add category: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/all")
    @Produces("application/json")
    @Secured
    public List<Category> findAll() throws SQLException {
        return dao.findAll();
    }

    @GET
    @Path("/allWithCount")
    @Produces("application/json")
    @Secured
    public List<Category> findAllWithCount() throws SQLException {
        return dao.getCategoriesWithCount();
    }

    @POST
    @Path("/findbyname")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    @Secured
    public List<Category> findByName(String json) throws SQLException {
        List<Category>  categories = new ArrayList<>();
        try{
            JSONObject inputJSON = new JSONObject(json);
            String search = inputJSON.getString("name");
            categories = dao.findByName(search);
        }catch(Exception e){
            LOGGER.log(Level.INFO,"Couldn't find category", e);
            LOGGER.fine("no categories found: " + e.getMessage());
        }
        return categories;
    }

    @POST
    @Path("/editcategory")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response editCategory(String jsonData) throws SQLException{
        try{
            JSONObject inputJSON = new JSONObject(jsonData);
            String newCategory = inputJSON.getString("newName");
            String oldName = inputJSON.getString("oldName");

            dao.editCategory(oldName, newCategory);

            return ResponseHandler.getInstance().getSuccess("Category edited");
        }catch(Exception e){
            LOGGER.fine("Couldn't edit category: " + e.getMessage());
            return ResponseHandler.getInstance().getFailure("Couldn't edit category",e);
        }
    }

    @POST
    @Path("/emailcategories")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    @Secured
    public List<Category> findCategories(String json) throws SQLException {
        JSONObject inputJSON = new JSONObject(json);
        String email = inputJSON.getString("email");
        return dao.findCategories(email);
    }

    @POST
    @Path("/related")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    @Override
    @Secured
    public List<Category> findRelated(String jsonData) throws SQLException {
        JSONObject inputJSON = new JSONObject(jsonData);
        String  category = inputJSON.getString(constCategory);
        return dao.findRelated(category);
    }

    @POST
    @Path("/addRelatedCategory")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response addRelatedCategory(String jsonData) throws SQLException {
        try{
            JSONObject inputJSON = new JSONObject(jsonData);
            String category = inputJSON.getString(constCategory);
            String subCategory = inputJSON.getString("subCategory");
            dao.addRelatedCategory(category,subCategory);
            String result = "Related category added";
            return ResponseHandler.getInstance().getSuccess(result);
        }catch(Exception e){
            LOGGER.log(Level.INFO, "Couldn't add related categories", e);
            LOGGER.fine("Couldn't add related category: " + e.getMessage());
            return ResponseHandler.getInstance().getFailure("Couldn't add related category: ",e);
        }
    }

    @POST
    @Path("/addRelatedCategories")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response addRelatedCategories(String jsonData) throws SQLException {
        try{
            JSONObject inputJSON = new JSONObject(jsonData);
            String category = inputJSON.getString(constCategory);
            JSONArray subCategories = inputJSON.getJSONArray("subCategories");
            for (int i = 0; i < subCategories.length(); i++) {
                JSONObject subCategory = subCategories.getJSONObject(i);
                dao.addRelatedCategory(category,subCategory.getString(constCategory));
            }
            return ResponseHandler.getInstance().getSuccess("Related category added");
        }catch(Exception e){
            LOGGER.log(Level.INFO, "Couldn't add related categories", e);
            LOGGER.fine("Couldn't add related categories: " + e.getMessage());
            return ResponseHandler.getInstance().getFailure("Couldn't add categories: ",e);
        }
    }

    @POST
    @Path("/deleteCategory")
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    @Secured
    public Response deleteCategory(String jsonData) throws SQLException {
        try {
            JSONObject inputJSON = new JSONObject(jsonData);
            String category = inputJSON.getString(constCategory);
            String result = "Category deleted";
            dao.deleteCategory(category);
            return ResponseHandler.getInstance().getSuccess(result);
        } catch (Exception e) {
            LOGGER.fine("Couldn't delete categories: " + e.getMessage());
            return ResponseHandler.getInstance().getFailure("Couldn't delete categories: ", e);
        }
    }

    @POST
    @Path("/getContactsInCategory")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    @Override
    @Secured
    public List<Contact> getContactsInCategory(String jsonData) throws SQLException {
        List<Contact> contacts = new ArrayList<>();
        try {
            JSONObject inputJSON = new JSONObject(jsonData);
            String category = inputJSON.getString(constCategory);
            contacts = dao.getContactsInCategory(category);
        } catch (Exception e) {
            LOGGER.log(Level.INFO,"couldn't get contacts in category", e);
        }
        return contacts;
    }

    @POST
    @Path("/getRelatedCategories")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    @Override
    @Secured
    public List<Category> getRelatedCategories(String jsonData) throws SQLException {
        List<Category> categories = new ArrayList<>();
        try {
            JSONObject inputJSON = new JSONObject(jsonData);
            String category = inputJSON.getString(constCategory);
            categories = dao.findRelated(category);
        } catch (Exception e) {
            LOGGER.log(Level.INFO,"Couldn't get related categories: ", e);
        }
        return categories;
    }

    @POST
    @Path("/getCategoriesWithDomain")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    @Override
    @Secured
    public List<Category> getCategoriesWithDomain(String json) throws SQLException {
        JSONObject inputJSON = new JSONObject(json);
        String  domain = inputJSON.getString("domain");
        return dao.findCategoriesWithDomain(domain);
    }
}
