package org.jpos.myParticipants;

import org.jpos.ee.DB;
import org.jpos.models.User;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

public class Transaction implements TransactionParticipant {
    @Override
    public int prepare(long l, Serializable serializable) {
        Context ctx = (Context) serializable;
        Map<String, String> transactionDetails = ctx.get("transactionDetails");

        String username = transactionDetails.get("username");
        String password = transactionDetails.get("password");
//        String amount = transactionDetails.get("amount");
        BigDecimal amount = new BigDecimal(transactionDetails.get("amount"));

        String transactionType = (String) ctx.get("TRANSACTION_TYPE");

//        if ("CREDIT".equals(transactionType)) {
//            System.out.println("broom broom!!!");
//            ctx.put("response", "Transaction successful!");
//            return PREPARED; // Credit group
//        } else if ("DEBIT".equals(transactionType)) {
//            System.out.println("fooooooo");
//            ctx.put("response", "Transaction successful!");
//            return PREPARED; // Debit group
//        }
//
//        ctx.put("response", "Transaction successful!");
//
//        return PREPARED;

        try {
            User userDetails = DB.exec((db) -> {
                return db.session().createQuery("FROM User WHERE name = :username AND password = :password", User.class)
                        .setParameter("username", username)
                        .setParameter("password", password)
                        .uniqueResult();
            });

            if (userDetails == null) {
                ctx.put("response", "Invalid username or password!");
                return ABORTED;
            }

            if ("CREDIT".equals(transactionType)) {
                userDetails.setBalance(userDetails.getBalance().add(amount));

                System.out.println("Credit transaction processed.");
            } else if ("DEBIT".equals(transactionType)) {
                if (userDetails.getBalance().compareTo(amount) < 0) {
                    ctx.put("response", "Insufficient balance for debit transaction!");
                    return ABORTED;
                }
                userDetails.setBalance(userDetails.getBalance().subtract(amount));
                System.out.println("Debit transaction processed.");
            }

//            DB.exec((db) -> {
//                db.session().update(userDetails);
//                return null;
//            });

            DB.execWithTransaction((db) -> {
                db.session().update(userDetails);
                return userDetails;
            });

            ctx.put("response", "Transaction successful!");
            return PREPARED;

        } catch (Exception e) {
            ctx.put("response", "Transaction failed due to an error!");
            throw new RuntimeException(e);
        }
    }
}
