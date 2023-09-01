package com.upgrad.Payment.Controller;

import com.upgrad.Payment.Model.TransactionDetailsEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/payment")
public class PaymentController {

    static int transactionId = 0;
    private static final Map<Integer, TransactionDetailsEntity> Transaction_MAP = new HashMap<>();

    //Endpoint 1 - POST
    @PostMapping(value = "/transaction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity completePaymentTransaction(@RequestBody TransactionDetailsEntity transactionDetailsEntity){
        TransactionDetailsEntity bookingInfo = new TransactionDetailsEntity();

        //TransactionID
        bookingInfo.setTransactionId(++transactionId);
        //PaymentMode
        bookingInfo.setPaymentMode(transactionDetailsEntity.getPaymentMode());
        //BookingId
        bookingInfo.setBookingId(transactionDetailsEntity.getBookingId());
        //UpiId
        bookingInfo.setUpiId(transactionDetailsEntity.getUpiId());
        //cardNumber
        bookingInfo.setCardNumber(transactionDetailsEntity.getCardNumber());

        Transaction_MAP.put(bookingInfo.getTransactionId(),bookingInfo);
        
        return new ResponseEntity<>(bookingInfo.getTransactionId(), HttpStatus.CREATED);
    }

    //Endpoint 2 - GET
    //Search based on transactionID
    @GetMapping(value="/transaction/{transactionId}")
    public ResponseEntity fetchBasedonTransactionID(@PathVariable(name="transactionId")int transactionId){
        return new ResponseEntity( Transaction_MAP.get(transactionId), HttpStatus.OK);
    }

}
