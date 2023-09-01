package com.upgrad.Booking.Controller;

import com.upgrad.Booking.Model.BookingInfoEntity;
import com.upgrad.Booking.Model.BookingRequestEntity;
import com.upgrad.Booking.Model.PaymentRequestEntity;
import com.upgrad.Booking.Model.PaymentResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping(value = "/hotel")
public class BookingController {
    static int bookingId = 0;
    //In Memory storage
    private static final Map<Integer, BookingInfoEntity> Booking_MAP = new HashMap<>();

    //Search a booking based on bookingID(not part of requirement, For validation purpose)
    @GetMapping(value="/booking/{id}")
    public ResponseEntity fetchBasedonBookingID(@PathVariable(name="id")int id){
        return new ResponseEntity( Booking_MAP.get(id), HttpStatus.OK);
    }

    //To Generate Random Numbers
    public static ArrayList<String> getRandomNumbers(int count){
        Random rand = new Random();
        int upperBound = 100;
        ArrayList<String>numberList = new ArrayList<String>();

        for (int i=0; i<count; i++){
            numberList.add(String.valueOf(rand.nextInt(upperBound)));
        }

        return numberList;
    }
    //Endpoint 1 - POST 

    @PostMapping(value = "/booking", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createBooking(@RequestBody BookingRequestEntity bookingRequest){
        BookingInfoEntity bookingInfo = new BookingInfoEntity();

        //bookingID
        bookingInfo.setId(++bookingId);
        //fromDate
        bookingInfo.setFromDate(bookingRequest.getFromDate().atStartOfDay());
        //toDate
        bookingInfo.setToDate(bookingRequest.getToDate().atStartOfDay());
        //aadharNumber
        bookingInfo.setAadharNumber(bookingRequest.getAadharNumber());
        //roomNumbers
        ArrayList<String> rooms = getRandomNumbers(bookingRequest.getNumOfRooms());
        bookingInfo.setRoomNumbers(rooms.toString());
        //roomPrice
        Period period = Period.between(bookingRequest.getFromDate(),bookingRequest.getToDate());
        int days = period.getDays();
        int price = 1000*bookingRequest.getNumOfRooms()*days;
        bookingInfo.setRoomPrice(price);
        //transactionId
        bookingInfo.setTransactionId(0);
        //bookedOn
        bookingInfo.setBookedOn(LocalDateTime.now());

        Booking_MAP.put(bookingInfo.getId(),bookingInfo);
        return new ResponseEntity(bookingInfo, HttpStatus.CREATED);
    }

    //Endpoint 2 - POST
    @PostMapping(value = "/booking/{id}/transaction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity initiatePayment(@PathVariable(name="id")int id,@RequestBody PaymentRequestEntity paymentRequestEntity){
        if(!paymentRequestEntity.getPaymentMode().equals("CARD")&&!paymentRequestEntity.getPaymentMode().equals("UPI")){
            PaymentResponseEntity response = new PaymentResponseEntity("Invalid mode of payment",400);
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }else if(!Booking_MAP.containsKey(id)||!Booking_MAP.containsKey(paymentRequestEntity.getBookingId())){
            PaymentResponseEntity response = new PaymentResponseEntity("Invalid Booking Id",400);
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
        else{
            //calling payment service synchronously
            String uri = "http://localhost:8083/payment/transaction";
            RestTemplate resttemplate = new RestTemplate();
            int paymenttransactionid = resttemplate.postForObject(uri,paymentRequestEntity,Integer.class);
            //Updating transaction id
            BookingInfoEntity updatedBooking = Booking_MAP.get(id);
            updatedBooking.setTransactionId(paymenttransactionid);
            Booking_MAP.put(updatedBooking.getId(),updatedBooking);
            String message = "Booking confirmed for user with aadhaar number: "
                    + updatedBooking.getAadharNumber()
                    +    "    |    "
                    + "Here are the booking details:    " + updatedBooking.toString();
            System.out.println(message);
            return new ResponseEntity(updatedBooking,HttpStatus.CREATED);
        }
    }
}
