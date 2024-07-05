package org.jpos.myParticipants;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jpos.ee.DB;
import org.jpos.models.User;
import org.jpos.q2.Q2;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;
import org.jpos.util.NameRegistrar;

import java.io.Serializable;
import java.util.Map;

public class LoginParticipant implements TransactionParticipant {
    @Override
    public int prepare(long l, Serializable serializable) {
        Context ctx = (Context) serializable;
        Map<String, String> loginRequest = ctx.get("LoginRequest");

        String username = loginRequest.get("username");
        String password = loginRequest.get("password");


//        try{
//            Session session = (Session) ((SessionFactory) NameRegistrar.get("SessionFactory")).getCurrentSession();
//            session.getTransaction().begin();
//
//            User userDetails = session.createQuery("FROM User WHERE name = :username AND password = :password", User.class)
//                    .setParameter("username", username)
//                    .setParameter("password", password)
//                    .uniqueResult();
//
//            if (userDetails == null) {
//                // User does not exist
//                ctx.put("response", "Invalid username or password!");
//                return ABORTED;
//            }
//
//            session.getTransaction().commit();
//            ctx.put("response", "Signup successful");
//
//        }catch(NameRegistrar.NotFoundException e){
//            ctx.put("response", "Signup failed");
//        }

        try {
            User userDetails = DB.exec((db) -> {
                return db.session().createQuery("FROM User WHERE name = :username AND password = :password", User.class)
                        .setParameter("username", username)
                        .setParameter("password",password)
                        .uniqueResult();
            });
            System.out.println("******************"+userDetails);

            if(userDetails == null){
                ctx.put("response", "Invalid username or password!");
                return ABORTED;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        ctx.put("response", "login successful");

        return PREPARED;
    }
}
