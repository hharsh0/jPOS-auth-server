package org.jpos.rest;

import org.checkerframework.checker.units.qual.C;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.transaction.Context;

import javax.print.attribute.standard.Media;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/auth")
public class Auth {

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signup(Map<String, String> credentials){
        String username=credentials.get("username");
        String password=credentials.get("password");

        Context ctx = new Context();

        ctx.put("signInRequest",credentials);

        Space space = SpaceFactory.getSpace();
        space.out("signInQueue",ctx);

        String response = ctx.get("response",5000);
        Map<String, String> resp = new HashMap<>();

        resp.put("message", response != null? response: "Request Time out!");

        return Response.ok(resp, MediaType.APPLICATION_JSON)
                .status(Response.Status.OK)
                .build();

    }


    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Map<String, String> credentials){
        Context ctx = new Context();
        ctx.put("LoginRequest", credentials);

        Space sp = SpaceFactory.getSpace();
        sp.out("LoginRequestQueue", ctx);

        String msg = ctx.get("response", 5000);

        Map<String, Object> resp = new HashMap<>();
        resp.put("msg", msg != null ? msg : "Timeout, Login failed");

        return Response.ok(resp, MediaType.APPLICATION_JSON)
                .status(Response.Status.OK)
                .build();
    }

}
