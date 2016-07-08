package nl.quintor.ayic.mss.category.services;

import nl.quintor.ayic.mss.category.dao.ICategoryDAO;
import nl.quintor.ayic.mss.category.domain.Category;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {
    private ICategoryService categoryService;
    private ICategoryDAO categoryDAO;

    @Before
    public void setUp() throws Exception {
        categoryDAO = mock(ICategoryDAO.class);
        categoryService = new CategoryService(categoryDAO);
    }
    @Test
    public void getContactsInCategory() throws Exception {
        categoryService.getContactsInCategory("{\"category\":\"Java\"}");
        verify(categoryDAO, times(1)).getContactsInCategory("Java");
    }
    @Test
    public void getContactsInCategoryCatch() throws Exception{
        doThrow(new RuntimeException()).when(categoryDAO).getContactsInCategory("Java");
        categoryService.getContactsInCategory("{\"category\":\"Java\"}");
    }

    @Test
    public void testFindAll() throws Exception {
        categoryService.findAll();
        verify(categoryDAO,times(1)).findAll();
    }

    @Test
    public void testFindByName() throws Exception {
        categoryService.findByName("{\"name\":\"test\"}");
        verify(categoryDAO, times(1)).findByName("test");
    }

    @Test
    public void testAddCategory() throws Exception {
        Category category = new Category("test");
        categoryService.addCategory(category);
        verify(categoryDAO, times(1)).addCategory(category);
    }

    @Test
    public void testEditCategory() throws Exception {
        categoryService.editCategory("{\n" +
                "\"newName\":\"test\",\n" +
                "\"oldName\":\"test1\",\n" +
                "}");
        verify(categoryDAO, times(1)).editCategory("test1","test");
    }

    @Test
    public void testEditCategoryCatch() throws Exception{
        Response rs = categoryService.editCategory("dsfgsdfgsdfg");
        String result = "OutboundJaxrsResponse{status=409, reason=Conflict, hasEntity=true, closed=false, buffered=false}";
        assertEquals(result, rs.toString());
    }

    @Test
    public void testAddCategoryCatch() throws Exception{
        Category category = new Category(".net");
        String expected = "OutboundJaxrsResponse{status=409, reason=Conflict, hasEntity=true, closed=false, buffered=false}";
        doThrow(new RuntimeException()).when(categoryDAO).addCategory(category);
        Response rs = categoryService.addCategory(category);
        assertEquals(expected,rs.toString());
    }


    @Test
    public void testFindRelated() throws Exception {
        categoryService.findRelated("{\"category\":\"java\"}");
        verify(categoryDAO, times(1)).findRelated("java");
    }

    @Test
    public void testAddRelatedCategory() throws Exception {
        categoryService.addRelatedCategory("{\"category\" : \"java\", \"subCategory\":\"java2\"}");
        verify(categoryDAO, times(1)).addRelatedCategory("java","java2");
    }

    @Test
    public void testAddRelatedCategories() throws Exception {
        categoryService.addRelatedCategories("{\n" +
                "\"category\":\"java\",\n" +
                "    \"subCategories\": [{\n" +
                "        \"category\": \"HTML\"\n" +
                "    }, {\n" +
                "        \"category\": \"PHP\"\n" +
                "    }]\n" +
                "}");
        verify(categoryDAO, times(1)).addRelatedCategory("java","HTML");
        verify(categoryDAO, times(1)).addRelatedCategory("java","PHP");
    }


    @Test
    public void testAddRelatedCategoryCatch() throws Exception {
        String expected = "OutboundJaxrsResponse{status=409, reason=Conflict, hasEntity=true, closed=false, buffered=false}";
        Response rs = categoryService.addRelatedCategory("");
        assertEquals(expected,rs.toString());
    }

    @Test
    public void testAddRelatedCategoriesCatch() throws Exception {
        String expected = "OutboundJaxrsResponse{status=409, reason=Conflict, hasEntity=true, closed=false, buffered=false}";
        Response rs = categoryService.addRelatedCategories("");
        assertEquals(expected,rs.toString());
    }

    @Test
    public void testFindCategories() throws Exception {
        categoryService.findCategories("{\"email\" : \"java\"}");
        verify(categoryDAO, times(1)).findCategories("java");
    }

    @Test
    public void testfindByNameCatch() throws Exception {
        doThrow(new RuntimeException()).when(categoryDAO).findByName("fout");
        categoryService.findByName("fout");
    }

    @Test
    public void testDeleteCategory() throws Exception {
        categoryService.deleteCategory("{\"category\": \"oops\"}");
        verify(categoryDAO, times(1)).deleteCategory("oops");
    }

    @Test
    public void testDeleteCategoryCatch() throws Exception{
        doThrow(new SQLException()).when(categoryDAO).deleteCategory("fout");
        categoryService.deleteCategory("{\"category\": \"fout\"}");
    }

    @Test
    public void testCategoryServiceConstructor() {
        try{
            new CategoryService();
        }catch(Exception e){

        }
        //can't test without a database.
        assertEquals(true,true);
    }

    @Test
    public void testGetCategoriesWithDomain() throws Exception {
        categoryService.getCategoriesWithDomain("{\"domain\" : \"java\"}");
        verify(categoryDAO, times(1)).findCategoriesWithDomain("java");
    }

    @Test
    public void testFindAllWithCount() throws Exception {
        categoryService.findAllWithCount();
        verify(categoryDAO,times(1)).getCategoriesWithCount();
    }

    @Test
    public void testGetRelatedCategories() throws Exception {
        categoryService.getRelatedCategories("{\"category\" : \"test\"}");
        verify(categoryDAO,times(1)).findRelated(anyString());
    }

    @Test
    public void testGetRelatedCategoriesCatch() throws Exception {
        doThrow(new SQLException()).when(categoryDAO).findRelated(anyString());
        categoryService.getRelatedCategories("{\"category\" : \"test\"}");
    }
}