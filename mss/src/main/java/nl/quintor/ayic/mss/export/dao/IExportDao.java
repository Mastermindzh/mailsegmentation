package nl.quintor.ayic.mss.export.dao;

import nl.quintor.ayic.mss.contact.domain.Contact;
import nl.quintor.ayic.mss.domain.domain.Domain;
import nl.quintor.ayic.mss.export.domain.CategoryBox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IExportDao {

    /**
     *
     * @param categoryBoxes boxes of categories to get contacts from. Every box can contain categories. The contacts must be linked to at least one category in every box to be exported.
     * @param domains domains to get contacts from. Regardless of whether they are connected to the categories in the categoryBoxes.
     * @return list of contacts
     * @throws SQLException
     */
    public List<Contact> getExportContacts(ArrayList<CategoryBox> categoryBoxes, ArrayList<Domain> domains) throws SQLException;
}
