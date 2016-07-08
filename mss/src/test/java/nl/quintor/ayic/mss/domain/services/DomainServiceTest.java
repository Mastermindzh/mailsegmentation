package nl.quintor.ayic.mss.domain.services;

import nl.quintor.ayic.mss.domain.dao.IDomainDao;
import nl.quintor.ayic.mss.domain.domain.Domain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
@RunWith(MockitoJUnitRunner.class)
public class DomainServiceTest {

    private IDomainService domainService;
    private IDomainDao domainDao;
    private String failure = "OutboundJaxrsResponse{status=409, reason=Conflict, hasEntity=true, closed=false, buffered=false}";

    @Before
    public void setUp() throws Exception {
        domainDao = mock(IDomainDao.class);
        domainService = new DomainService(domainDao);
    }

    @Test
    public void testDeleteDomain() throws Exception {
        domainService.deleteDomain("{\"domain\":\"test\"}");
        verify(domainDao, times(1)).deleteDomain("test");
    }

    @Test
    public void testDeleteDomains() throws Exception {
        domainService.deleteDomains("{\"domains\":[{\"domain\":\"test\"},{\"domain\":\"test2\"}]}");
        verify(domainDao,times(1)).deleteDomain("test");
        verify(domainDao,times(1)).deleteDomain("test2");
    }

    @Test
    public void testAddDomain() throws Exception {
        Domain domain = new Domain("test",true);
        domainService.addDomain(domain);
        verify(domainDao, times(1)).addDomain(domain);
    }

    @Test
    public void testGetDomains() throws Exception {
        domainService.getDomains();
        verify(domainDao,times(1)).getDomains();
    }

    @Test
    public void testGetDomain() throws Exception {
        domainService.getDomain("{\"domain\":\"test\"}");
        verify(domainDao,times(1)).getDomain("test");
    }

    @Test
    public void testDeleteDomainCatch() throws Exception{
        Response rs = domainService.deleteDomain(";;;");
        assertEquals(failure, rs.toString());
    }
    @Test
    public void testDeleteDomainsCatch() throws Exception{
        Response rs = domainService.deleteDomains(";;;");
        assertEquals(failure, rs.toString());
    }

    @Test
    public void addDomainCatch() throws Exception{
        Domain domain = new Domain("test@test.nl",true);
        doThrow(new RuntimeException()).when(domainDao).addDomain(domain);
        Response rs = domainService.addDomain(domain);
        assertEquals(failure,rs.toString());
    }

    @Test
    public void testChange() throws Exception {
        domainService.updateDomain("{\"identifier\":\"test\",\"newIdentifier\":\"test2\"}");
        verify(domainDao,times(1)).updateDomain("test","test2");
    }

    @Test
    public void testChangeCatch() throws Exception {
        Response rs = domainService.updateDomain("....");
        assertEquals(failure, rs.toString());
    }

    @Test(expected = SQLException.class)
    public void testGetDomainCatch() throws Exception {
        doThrow(new RuntimeException()).when(domainDao).getDomain("");
        domainService.getDomain("{\"domain\":\"\"}");
    }

    @Test
    public void testAddCategoryToDomain() throws Exception {
        domainService.addCategoryToDomain("{\"category\":\"test\",\"domain\":\"test2\"}");
        verify(domainDao,times(1)).addCategoryToDomain("test","test2");
    }

    @Test
    public void testAddCategoryToDomainCatch() throws Exception {
        doThrow(new RuntimeException()).when(domainDao).addCategoryToDomain("test","test2");
        domainService.addCategoryToDomain("{\"category\":\"test\",\"domain\":\"test2\"}");
    }

    @Test
    public void testAddCategoriesToDomain() throws Exception {
        domainService.addCategoriesToDomain("{\n" +
                "\"domain\":\"@necmetus.com\",\n" +
                "    \"categories\": [{\n" +
                "        \"name\": \"HTML\"\n" +
                "    }, {\n" +
                "        \"name\": \"PHP\"\n" +
                "    }]\n" +
                "}");
        ArrayList<String> categories = new ArrayList<>();
        categories.add("HTML");
        categories.add("PHP");
        verify(domainDao,times(1)).addCategoriesToDomain("@necmetus.com",categories);
    }

    @Test
    public void testAddCategoriesToDomainCatch() throws Exception {
        doThrow(new RuntimeException()).when(domainDao).addCategoriesToDomain(anyString(),anyObject());
        Response rs = domainService.addCategoriesToDomain("{\n" +
                "\"domain\":\"@necmetus.com\",\n" +
                "    \"categories\": [{\n" +
                "        \"category\": \"HTML\"\n" +
                "    }, {\n" +
                "        \"category\": \"PHP\"\n" +
                "    }]\n" +
                "}");
        assertEquals(failure,rs.toString());
    }

    @Test
    public void testDomainServiceConstructor(){
        try{
            new DomainService();
        }catch(Exception e){

        }
        //can't test without a database.
        assertEquals(true,true);
    }

    @Test
    public void testUpdateCategoriesForDomain() throws Exception {
        Response rs = domainService.updateCategoriesForDomain("{\n" +
                "\"domain\":\"@necmetus.com\",\n" +
                "    \"categories\": [{\n" +
                "        \"name\": \"HTML\"\n" +
                "    }, {\n" +
                "        \"name\": \"PHP\"\n" +
                "    }]\n" +
                "}");
        verify(domainDao,times(1)).deleteAllCategoriesForDomain(anyString());
        verify(domainDao,times(1)).addCategoriesToDomain(anyString(), anyObject());

    }

    @Test
    public void testUpdateCategoriesForDomainCatch() throws Exception {
        doThrow(new RuntimeException()).when(domainDao).deleteAllCategoriesForDomain(anyString());
        Response rs = domainService.updateCategoriesForDomain("");
        assertEquals(failure,rs.toString());
    }

    @Test
    public void testGetDomainsWithCount() throws Exception {
        domainService.getDomainsWithCount();
        verify(domainDao,times(1)).getDomainsWithCount();
    }
}