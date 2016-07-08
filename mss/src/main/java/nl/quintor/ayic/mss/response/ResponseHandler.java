package nl.quintor.ayic.mss.response;

import javax.ws.rs.core.Response;

public class ResponseHandler {
    private static ResponseHandler ourInstance = new ResponseHandler();

    /**
     * return the instance of our ResponseHandler
     * @return a ResponseHandler
     */
    public static ResponseHandler getInstance() {
        return ourInstance;
    }

    /**
     * default constructor
     */
    private ResponseHandler() {
    }

    /**
     * returns a succesful response (201)
     * @param msg msg to be sent back
     * @return Response
     */
    public Response getSuccess(String msg){
        return Response.status(201).entity(msg).build();
    }

    /**
     * returns a failed response (409)
     * @param msg msg to be sent back
     * @param e exception
     * @return Response
     */
    public Response getFailure(String msg, Exception e){
        return Response.status(409).entity(msg+": "+e.getMessage()).build();
    }
}