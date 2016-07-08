package nl.quintor.ayic.mss.export.services;

import nl.quintor.ayic.mss.contact.domain.Contact;

import java.sql.SQLException;
import java.util.List;

public interface IExportService {

    /**
     *
     * @param jsonData categories and domains to get contacts from
     * @return contacts
     * @throws SQLException
     */
    List<Contact> getExportContacts(String jsonData) throws SQLException;
}
