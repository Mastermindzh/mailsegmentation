package nl.quintor.ayic.mss.export.services;

import nl.quintor.ayic.mss.authentication.domain.Secured;
import nl.quintor.ayic.mss.category.dao.CategoryDao;
import nl.quintor.ayic.mss.category.dao.ICategoryDAO;
import nl.quintor.ayic.mss.category.domain.Category;
import nl.quintor.ayic.mss.contact.domain.Contact;
import nl.quintor.ayic.mss.domain.domain.Domain;
import nl.quintor.ayic.mss.export.dao.ExportDao;
import nl.quintor.ayic.mss.export.dao.IExportDao;
import nl.quintor.ayic.mss.export.domain.CategoryBox;
import nl.quintor.ayic.mss.response.ResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/export")
public class ExportService {

    private IExportDao dao;
    private ICategoryDAO categoryDao;
    private static final Logger LOGGER = Logger.getLogger(String.valueOf(ExportService.class));

    //    @Inject
    public ExportService(IExportDao paramDao, ICategoryDAO categoryDao) throws SQLException {
        dao = paramDao;
        this.categoryDao = categoryDao;
    }

    /**
     * constructor
     *
     * @throws SQLException
     * @throws IOException
     */
    public ExportService() throws SQLException, IOException {
        dao = new ExportDao();
        categoryDao = new CategoryDao();
    }

    @POST
    @Path("/exportCSV")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response getExportCSV(String jsonData) throws SQLException {
        try {
            JSONObject inputJSON = new JSONObject(jsonData);
            ArrayList<CategoryBox> categoryBoxes = getCategoryBoxesFromJSONArray(inputJSON.getJSONArray("categoryBoxes"));
            getRelatedCategories(categoryBoxes);

            ArrayList<Domain> domains = getDomainsFromJSONArray(inputJSON.getJSONArray("domains"));

            List<Contact> contacts = dao.getExportContacts(categoryBoxes, domains);
            return Response.ok(createCSVString(contacts)).header("Content-Disposition", "attachment; filename=export").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Could not create CSV export file", e);
            return ResponseHandler.getInstance().getFailure("Could not create CSV export file", e);
        }
    }

    /**
     * Converts JSON to a ArrayList of categoryBoxes
     * @param jsonArray JSON array with categoryBoxes
     * @return ArrayList of categoryBoxes extracted form JSON
     */
    private ArrayList<CategoryBox> getCategoryBoxesFromJSONArray(JSONArray jsonArray) {
        ArrayList<CategoryBox> categoryBoxes = new ArrayList<CategoryBox>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject rec = jsonArray.getJSONObject(i);

            CategoryBox categoryBox = new CategoryBox();
            categoryBox.setIncludeRelatedCategories(rec.getBoolean("includeRelatedCategories"));
            categoryBox.setCategories(getCategoriesFromJSONArray(rec.getJSONArray("categories")));

            categoryBoxes.add(categoryBox);
        }
        return categoryBoxes;
    }

    /**
     * Get all related categories for each category box if isIncludedRelatedCategories == true
     * @param categoryBoxes the updated categoryBox
     */
    private void getRelatedCategories(ArrayList<CategoryBox> categoryBoxes) {
        try {
            for (CategoryBox categoryBox : categoryBoxes) {
                if (categoryBox.isIncludeRelatedCategories()) {
                    categoryBox.addCategories(categoryDao.findRelated(categoryBox.getCategories()));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not get related categories", e);
        }
    }

    /**
     * Converts a JSONArray with domains to al list of domains
     *
     * @param jsonArray JSONArray to get domains from
     * @return domain list
     */
    private ArrayList<Domain> getDomainsFromJSONArray(JSONArray jsonArray) {
        ArrayList<Domain> domains = new ArrayList<Domain>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject domainJSON = jsonArray.getJSONObject(i);

            Domain domain = new Domain();
            domain.setIdentifier(domainJSON.getString("identifier"));
            domains.add(domain);
        }
        return domains;
    }

    /**
     * Converts JSONArray to a list of category names
     *
     * @param jsonArray JSONArray with categories
     * @return List of category names
     */
    private ArrayList<Category> getCategoriesFromJSONArray(JSONArray jsonArray) {
        ArrayList<Category> categories = new ArrayList<Category>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject rec = jsonArray.getJSONObject(i);

            Category category = new Category();
            category.setName(rec.getString("name"));

            categories.add(category);
        }
        return categories;
    }

    /**
     * Creates a CSV String
     *
     * @param contacts contacts to add to the CSV string
     * @return CSV string with contact
     * @throws IOException
     */
    private String createCSVString(List<Contact> contacts) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Email,DateAdded,FirstName,LastName,PhoneNumber\n");

        for (Contact contact : contacts) {
            stringBuilder.append(contact.getEmail());

            csvStringBuilderAppendValue(stringBuilder, contact.getDateAdded());
            csvStringBuilderAppendValue(stringBuilder, contact.getFirstName());
            csvStringBuilderAppendValue(stringBuilder, contact.getLastName());
            csvStringBuilderAppendValue(stringBuilder, contact.getPhoneNumber());

            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    /**
     * Append column to csv string
     *
     * @param stringBuilder StringBuilder to append a column
     * @param data          data to append
     */
    private void csvStringBuilderAppendValue(StringBuilder stringBuilder, String data) {
        stringBuilder.append(",");
        if (data != null) {
            stringBuilder.append(data);
        }
    }
}
