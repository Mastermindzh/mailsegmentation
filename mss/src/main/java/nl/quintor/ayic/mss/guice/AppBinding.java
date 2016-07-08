package nl.quintor.ayic.mss.guice;
import com.google.inject.servlet.ServletModule;
import nl.quintor.ayic.mss.contact.dao.ContactDao;
import nl.quintor.ayic.mss.contact.dao.IContactDAO;


/**
 * Makes sure guice knows which class to inject into the injection points.
 */
public class AppBinding extends ServletModule {

    @Override
    public void configureServlets() {
        super.configureServlets();
            bind(IContactDAO.class).to(ContactDao.class);
    }
}
