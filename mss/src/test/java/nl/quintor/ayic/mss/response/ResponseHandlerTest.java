package nl.quintor.ayic.mss.response;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResponseHandlerTest {

    ResponseHandler rh;

    @Before
    public void setUp() throws Exception {
        rh = ResponseHandler.getInstance();
    }

    @Test
    public void testGetSuccess() throws Exception {
        assertEquals("OutboundJaxrsResponse{status=201, reason=Created, hasEntity=true, closed=false, buffered=false}",rh.getSuccess("msg").toString());
    }

    @Test
    public void testGetFailure() throws Exception {
        assertEquals("OutboundJaxrsResponse{status=409, reason=Conflict, hasEntity=true, closed=false, buffered=false}",rh.getFailure("msg",new Exception("exception")).toString());
    }
}