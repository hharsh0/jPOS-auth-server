package org.jpos.rest;

import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import org.jpos.transaction.Context;


@Path("/transaction")
public class TransactionService {

    @POST
    @Path("/credit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response creditTransaction(Map<String, String> transactionDetails) {
        Context ctx = new Context();
        ctx.put("TRANSACTION_TYPE", "CREDIT");
        ctx.put("transactionDetails", transactionDetails);

        Space space = SpaceFactory.getSpace();
        space.out("TransactionQueue", ctx);

        String response = ctx.get("response", 5000);
        Map<String, String> resp = new HashMap<>();
        resp.put("message", response != null ? response : "Credit transaction timed out!");

        return Response.ok(resp, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/debit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response debitTransaction(Map<String, String> transactionDetails) {
        Context ctx = new Context();
        ctx.put("TRANSACTION_TYPE", "DEBIT");
        ctx.put("transactionDetails", transactionDetails);

        Space space = SpaceFactory.getSpace();
        space.out("TransactionQueue", ctx);

        String response = ctx.get("response", 5000);
        Map<String, String> resp = new HashMap<>();
        resp.put("message", response != null ? response : "Debit transaction timed out!");

        return Response.ok(resp, MediaType.APPLICATION_JSON).build();
    }
}