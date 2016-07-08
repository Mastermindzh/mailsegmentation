package nl.quintor.ayic.mss.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import javax.servlet.annotation.WebListener;

/**
 * Class to get the guice injector
 */
@WebListener
public class GuiceServletConfig extends GuiceServletContextListener {
    @Override
    public Injector getInjector() {
        return Guice.createInjector(new AppBinding());
    }
}
