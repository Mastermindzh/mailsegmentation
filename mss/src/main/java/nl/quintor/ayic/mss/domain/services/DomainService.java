package nl.quintor.ayic.mss.domain.services;

import nl.quintor.ayic.mss.authentication.domain.Secured;
import nl.quintor.ayic.mss.domain.dao.DomainDao;
import nl.quintor.ayic.mss.domain.dao.IDomainDao;
import nl.quintor.ayic.mss.domain.domain.Domain;
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

@Path("/domains")
public class DomainService implements IDomainService{

    private static final String constDomain = "domain";
    private IDomainDao dao;
    private static final Logger LOGGER = Logger.getLogger(String.valueOf(DomainService.class));

    //    @Inject
    public DomainService(IDomainDao paramDao) throws SQLException {
        dao = paramDao;
    }

    /**
     * constructor
     * @throws SQLException
     * @throws IOException
     */
    public DomainService() throws SQLException, IOException {
        dao = new DomainDao();
    }

    @POST
    @Path("/deleteDomain")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response deleteDomain(String str) throws SQLException {
        try {
            JSONObject inputJSON = new JSONObject(str);
            String domain = inputJSON.getString(constDomain);
            dao.deleteDomain(domain);
            return ResponseHandler.getInstance().getSuccess("Domain deleted");
        } catch (Exception e) {
            return ResponseHandler.getInstance().getFailure("Couldn't delete domain", e);
        }
    }

    @POST
    @Path("/deleteDomains")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response deleteDomains(String str) throws SQLException {
        try {
            JSONObject inputJSON = new JSONObject(str);
            JSONArray emails = inputJSON.getJSONArray("domains");
            for (int i = 0; i < emails.length(); i++) {
                JSONObject rec = emails.getJSONObject(i);
                dao.deleteDomain(rec.getString(constDomain));
            }
            return ResponseHandler.getInstance().getSuccess("Domains deleted");
        } catch (Exception e) {
            return ResponseHandler.getInstance().getFailure("Couldn't delete domain", e);
        }
    }
    @POST
    @Path("/addDomain")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response addDomain(Domain domain) throws SQLException {
        try {
            dao.addDomain(domain);
            return ResponseHandler.getInstance().getSuccess("Domain added");
        } catch (Exception e) {
            return ResponseHandler.getInstance().getFailure("Couldn't add domain", e);
        }
    }

    @GET
    @Path("/all")
    @Produces("application/json")
    @Secured
    public List<Domain> getDomains() throws SQLException {
        return dao.getDomains();
    }

    @POST
    @Path("/domain")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    @Secured
    public Domain getDomain(String str) throws SQLException {
        try {
            JSONObject inputJSON = new JSONObject(str);
            String domain = inputJSON.getString(constDomain);
            return dao.getDomain(domain);
        }catch(Exception e){
            LOGGER.log(Level.INFO, "Domain can't be found", e);
            LOGGER.fine("Domain can't be found " + e.getMessage());
            throw new SQLException("Domain can't be found");
        }
    }

    @POST
    @Path("/updateDomain")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response updateDomain(String str) throws SQLException {
        try {
            JSONObject inputJSON = new JSONObject(str);
            String identifier = inputJSON.getString("identifier");
            String newIdentifier = inputJSON.getString("newIdentifier");
            dao.updateDomain(identifier,newIdentifier);
            return ResponseHandler.getInstance().getSuccess("Domain identifier updated");
        } catch (Exception e) {
            return ResponseHandler.getInstance().getFailure("Couldn't update identifier", e);
        }
    }

    @POST
    @Path("/addCategoryToDomain")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response addCategoryToDomain(String json) throws SQLException {
        try {
            JSONObject inputJSON = new JSONObject(json);
            String domain = inputJSON.getString(constDomain);
            String category = inputJSON.getString("category");
            dao.addCategoryToDomain(category,domain);
            return ResponseHandler.getInstance().getSuccess("Category was successfully added to domain");
        } catch (Exception e) {
            return ResponseHandler.getInstance().getFailure("Couldn't add category to domain", e);
        }
    }

    @POST
    @Path("/addCategoriesToDomain")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response addCategoriesToDomain(String json) throws SQLException {
        try {
            JSONObject inputJSON = new JSONObject(json);
            String domain = inputJSON.getString(constDomain);

            JSONArray categories = inputJSON.getJSONArray("categories");
            dao.addCategoriesToDomain(domain, getCategoriesFromJsonArray(categories));
            return ResponseHandler.getInstance().getSuccess("Categories added to the domain");
        } catch (Exception e) {
            return ResponseHandler.getInstance().getFailure("Couldn't add categories to the domain", e);
        }
    }

    @POST
    @Path("/updateCategoriesForDomain")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response updateCategoriesForDomain(String str) throws SQLException {
        try {
            JSONObject inputJSON = new JSONObject(str);
            String identifier = inputJSON.getString("domain");
            dao.deleteAllCategoriesForDomain(identifier);
            dao.addCategoriesToDomain(identifier, getCategoriesFromJsonArray(inputJSON.getJSONArray("categories")));
            String result = "domain categories updated";
            return Response.status(201).entity(result).build();
        } catch (Exception e) {
            return ResponseHandler.getInstance().getFailure("Couldn't update categories", e);
        }
    }

    @GET
    @Path("/withCount")
    @Produces("application/json")
    @Secured
    public List<Domain> getDomainsWithCount() throws SQLException {
        return dao.getDomainsWithCount();
    }

    private ArrayList<String> getCategoriesFromJsonArray(JSONArray jsonArray) {
        ArrayList<String> categories = new ArrayList<String>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject rec = jsonArray.getJSONObject(i);
                categories.add(rec.getString("name"));
            }
        }
        return categories;
    }
}
