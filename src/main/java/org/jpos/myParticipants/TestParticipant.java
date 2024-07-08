package org.jpos.myParticipants;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jpos.models.User;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;
import org.jpos.util.NameRegistrar;
import org.jpos.utils.PasswordUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

public class TestParticipant implements TransactionParticipant {
    @Override
    public int prepare(long l, Serializable serializable) {
        Context ctx = (Context) serializable;
        Map<String, String> signupRequest = ctx.get("signInRequest");
        try{
            Session session = (Session) ((SessionFactory) NameRegistrar.get("SessionFactory")).getCurrentSession();
            session.getTransaction().begin();
            User newUser = new User();
            newUser.setName(signupRequest.get("username"));
            newUser.setPassword(signupRequest.get("password"));
            newUser.setBalance(new BigDecimal(signupRequest.get("amount")) );

            System.out.println("trying to signup!!!");

            session.save(newUser);
            session.getTransaction().commit();
            ctx.put("response", "Signup successful");

        }catch(NameRegistrar.NotFoundException e){
            ctx.put("response", "Signup failed");
        }

        return PREPARED;
    }
}
