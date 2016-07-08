package nl.quintor.ayic.mss.contact.services;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import nl.quintor.ayic.mss.authentication.domain.Secured;
import nl.quintor.ayic.mss.contact.dao.ContactDao;
import nl.quintor.ayic.mss.contact.dao.IContactDAO;
import nl.quintor.ayic.mss.contact.domain.Contact;
import nl.quintor.ayic.mss.response.ResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/contacts")
public class ContactService implements IContactService {
    private static final String constEmail = "email";
    private static final String constNoContacts = "No contact(s) found";
    private static final String constActive = "active";
    private IContactDAO dao;
    private static final Logger LOGGER = Logger.getLogger(String.valueOf(ContactService.class));

    //    @Inject
    public ContactService(IContactDAO paramDao) throws SQLException {
        dao = paramDao;
    }


    public ContactService() throws SQLException, IOException {
        dao = new ContactDao();
    }

    @POST
    @Path("/deleteContacts")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response deleteContacts(String str) throws SQLException {
        try {
            JSONObject inputJSON = new JSONObject(str);
            JSONArray emails = inputJSON.getJSONArray("emails");
            for (int i = 0; i < emails.length(); i++) {
                JSONObject rec = emails.getJSONObject(i);
                dao.deleteContact(rec.getString(constEmail));
            }
            return ResponseHandler.getInstance().getSuccess("Contacts moved to trash");
        } catch (Exception e) {
            return ResponseHandler.getInstance().getFailure("Couldn't delete contact", e);
        }
    }
    @POST
    @Path("/permDeleteContacts")
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    @Secured
    public Response permDeleteContacts(String str) throws SQLException {
        try {
            JSONObject inputJSON = new JSONObject(str);
            JSONArray emails = inputJSON.getJSONArray("emails");
            for (int i = 0; i < emails.length(); i++) {
                JSONObject rec = emails.getJSONObject(i);
                dao.permDeleteContact(rec.getString(constEmail));
            }
            return ResponseHandler.getInstance().getSuccess("Contacts deleted");
        } catch (Exception e) {
            return ResponseHandler.getInstance().getFailure("Couldn't delete contacts", e);
        }
    }

    @POST
    @Path("/deleteContact")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response deleteContact(String str) throws SQLException {
        try {
            JSONObject inputJSON = new JSONObject(str);
            String mail = inputJSON.getString(constEmail);
            dao.deleteContact(mail);
            return ResponseHandler.getInstance().getSuccess("Contact moved to trash");
        } catch (Exception e) {
            return ResponseHandler.getInstance().getFailure("Couldn't delete contact", e);
        }
    }

    @POST
    @Path("/addContactToCategories")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response addContactToCategories(String str) throws SQLException {
        try {
            JSONObject inputJSON = new JSONObject(str);
            String mail = inputJSON.getString(constEmail);

            dao.addContactToCategories(mail, getCategoriesFromJsonArray(inputJSON.getJSONArray("categories")));
            String result = "contact added to categories";
            return Response.status(201).entity(result).build();
        } catch (Exception e) {

            return ResponseHandler.getInstance().getFailure("Couldn't add contact to categories", e);

        }
    }

    @Override
    @POST
    @Path("/updateContact")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response updateContact(String str) throws SQLException {
        JSONObject inputJSON = new JSONObject(str);
        String oldEmail = inputJSON.getString("oldEmail");
        Contact contact = new Contact();
        JSONObject contactJsonObject = inputJSON.getJSONObject("contact");

        contact.setActive(contactJsonObject.getBoolean(constActive));
        contact.setEmail(contactJsonObject.getString(constEmail));
        fillContact(contact, contactJsonObject);

        try {
            dao.updateContact(contact, oldEmail);
            return ResponseHandler.getInstance().getSuccess("Contact added");
        } catch (Exception e) {
            return ResponseHandler.getInstance().getFailure("Couldn't add contact", e);
        }
    }

    private void fillContact(Contact contact, JSONObject contactJsonObject) {
        String firstName = "firstName";
        String importName = "importName";
        String lastName = "lastName";
        String phoneNumber = "phoneNumber";

        if (contactJsonObject.has(firstName) && !contactJsonObject.isNull(firstName)) {
            contact.setFirstName(contactJsonObject.getString(firstName));
        }
        if (contactJsonObject.has(importName) && !contactJsonObject.isNull(importName)) {
            contact.setImportName(contactJsonObject.getString(importName));
        }
        if (contactJsonObject.has(lastName) && !contactJsonObject.isNull(lastName)) {
            contact.setLastName(contactJsonObject.getString(lastName));
        }
        if (contactJsonObject.has(phoneNumber) && !contactJsonObject.isNull(phoneNumber)) {
            contact.setPhoneNumber(contactJsonObject.getString(phoneNumber));
        }
    }

    @POST
    @Path("/addContact")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response addContact(Contact contact) throws SQLException {
        try {
            dao.addContact(contact);
            return ResponseHandler.getInstance().getSuccess("Contact added");
        } catch (Exception e) {
            return ResponseHandler.getInstance().getFailure("Couldn't add contact", e);
        }
    }

    @GET
    @Path("/all")
    @Produces("application/json")
    @Secured
    public List<Contact> getContacts() throws SQLException {
        return dao.getContacts();
    }

    @POST
    @Path("/contact")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    @Secured
    public Contact getContact(String str) throws SQLException {
        try {
            JSONObject inputJSON = new JSONObject(str);
            String mail = inputJSON.getString(constEmail);
            return dao.getContact(mail);

        } catch (Exception e) {
            LOGGER.log(Level.FINE, constNoContacts, e);
            LOGGER.fine("No contact(s) found " + e.getMessage());
            throw new SQLException(constNoContacts);
        }
    }

    @POST
    @Path("/updateCategoriesForContact")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response updateCategoriesForContact(String str) throws SQLException {
        try {
            JSONObject inputJSON = new JSONObject(str);
            String mail = inputJSON.getString(constEmail);

            dao.deleteAllCategoriesForContact(mail);
            dao.addContactToCategories(mail, getCategoriesFromJsonArray(inputJSON.getJSONArray("categories")));

            String result = "contact categories updated";
            return Response.status(201).entity(result).build();
        } catch (Exception e) {
            return ResponseHandler.getInstance().getFailure("Couldn't update categories", e);
        }
    }


    @POST
    @Path("/importCSV")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Contact> importCSV(String str) throws SQLException {
        List<Contact> contacts = csvToContactList(str);
        for (Contact c : contacts) {
            addContact(c);
        }
        return contacts;
    }


    @POST
    @Path("/range")
    @Produces("application/json")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public List<Contact> getContactsInRange(String json) throws SQLException {
        try {
            JSONObject inputJSON = new JSONObject(json);
            int min = inputJSON.getInt("min");
            int max = inputJSON.getInt("max");
            boolean active = inputJSON.getBoolean(constActive);
            return dao.getContactsInRange(min,max,active);
        } catch (Exception e) {
            LOGGER.log(Level.FINE, constNoContacts, e);
            LOGGER.fine("Contact can't be found " + e.getMessage());
            throw new SQLException(constNoContacts);
        }
    }

    @POST
    @Path("/search")
    @Produces("application/json")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public List<Contact> searchContacts(String json) throws SQLException {
        try {
            JSONObject inputJSON = new JSONObject(json);
            String search = inputJSON.getString("search");
            Boolean active = inputJSON.getBoolean(constActive);
            return dao.searchContacts(search, active);
        } catch (Exception e) {
            LOGGER.log(Level.FINE, constNoContacts, e);
            LOGGER.fine("Contact can't be found " + e.getMessage());
            throw new SQLException(constNoContacts);
        }
    }

    /**
     * Uses openCSV to create a list of contacts.
     *
     * @param str csv file as string
     * @return List of contacts
     */
    private List<Contact> csvToContactList(String str) {
        CsvToBean<Contact> csvToBean = new CsvToBean<Contact>();
        Map<String, String> columnMapping = new HashMap<String, String>();
        columnMapping.put("Email Address", constEmail);
        columnMapping.put("First Name", "firstName");
        columnMapping.put("Last Name", "lastName");
        columnMapping.put("Import Name", "importName");
        HeaderColumnNameTranslateMappingStrategy<Contact> strategy = new HeaderColumnNameTranslateMappingStrategy<Contact>();
        strategy.setColumnMapping(columnMapping);
        /* deprecated but openCSV has no alternative available yet. Indicating that the @deprecated annotation was used too early.*/
        strategy.setType(Contact.class);
        CSVReader reader = new CSVReader(new StringReader(str));
        return csvToBean.parse(strategy, reader);
    }

    /**
     * convert jsonarray to string array with categories
     *
     * @param jsonArray jsonarray with categories
     * @return ArrayList with strings
     */
    private ArrayList<String> getCategoriesFromJsonArray(JSONArray jsonArray) {
        ArrayList<String> categories = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject rec = jsonArray.getJSONObject(i);
                categories.add(rec.getString("name"));
            }
        }
        return categories;
    }
}
