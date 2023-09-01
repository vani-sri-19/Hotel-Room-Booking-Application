# Hotel-Room-Booking-Application
#Spring #SpringBoot #Java #Rest #Microservice #RestTemplate 
The Hotel room booking application is divided into three different microservices, which are as follows:

API-Gateway - This service is exposed to the outer world and is responsible for routing all requests to the microservices internally.
Booking service - This service is responsible for collecting all information related to user booking and sending a confirmation message once the booking is confirmed.
Payment service - This is a dummy payment service; this service is called by the booking service for initiating payment after confirming rooms.

The API Gateway, Booking service and Payment service register themselves on the Eureka server. 

1. Booking Service:
This service is responsible for taking input from users like- toDate, fromDate, aadharNumber and the number of rooms required (numOfRooms) and save it in its database.
This service also generates a random list of room numbers depending on 'numOfRooms' requested by the user and returns the room number list (roomNumbers) and total roomPrice to the user.
The logic to calculate room price is as follows: 

roomPrice = 1000* numOfRooms*(number of days)
Here, 1000 INR is the base price/day/room.
 

 If the user wishes to go ahead with the booking, they can provide the payment related details like bookingMode, upiId / cardNumber, which will be further sent to the payment service to retrieve the transactionId.
 This transactionId then gets updated in the Booking table created in the database of the Booking Service and a confirmation message is printed on the console.
 Synchronous communication is established between the booking service and payment service. 

1.1 Controller Layer: 

Endpoint 1: This endpoint is responsible for collecting information like fromDate, toDate,aadharNumber,numOfRooms from the user and save it in its database.

URI: /booking
HTTP METHOD: POST
RequestBody: fromDate, toDate,aadharNumber,numOfRooms
Booking Service
Booking Service
Response Status: Created
Response: ResponseEntity<BookingInfoEntity>



Note 1: The value of the transactionId returned is 0. It means that no transaction is made for this booking. Once the transaction is done, the transactionId field in the booking table will get replaced with the transactionId received from the Payment service.
Note 2: The room numbers displayed are not based on the availability of vacant rooms. They are rather randomly generated integers between 1 and 100. This is done to trim down the complexity of the problem statement.  
Note 3: The field 'id' in the response body represents the 'BookingId'.
 

Endpoint 2: This endpoint is responsible for taking the payment related details from the user and sending it to the payment service. It gets the transactionId from the Payment service in respose and saves it in the booking table. Please note that for the field 'paymentMode', if the user provides any input other than 'UPI' or 'CARD', then it means that the user is not interested in the booking and wants to opt-out.

URL: booking/{bookingId}/transaction
HTTP METHOD: POST
PathVariable: int
RequestBody: paymentMode, bookingId, upiId,cardNumber

Note that the transaction Id this time stores the actual transactionId associated with the transaction. 

Exception 1: If the user gives any other input apart from “UPI” or “CARD”, the response message should look like the following:

{
   "message": "Invalid mode of payment",
  "statusCode": 400
}
 

Exception 2: If no transactions exist for the Booking Id passed to this endpoint then the response message should look like the following: 

{
   "message": " Invalid Booking Id ",
  "statusCode": 400
}
 

1.2   Configure this service to run on port number 8081.
1.3   Configure the hotel booking service as Eureka  Client


2. Payment Service:
This service is responsible for taking payment-related information- paymentMode, upiId or cardNumber, bookingId and returns a unique transactionId to the booking service.
It saves the data in its database and returns the transactionId as a response. 

2.1 Controller Layer: 

Endpoint 1: This endpoint is used to imitate performing a transaction for a particular booking. 
It takes details such as bookingId, paymentMode,upiId or cardNumber and returns the transactionId automatically generated while storing the details in the ‘transaction’ table. 
Note that this 'transactionId' is the primary key of the record that is being stored in the 'transaction' table.          

Note: This endpoint will be called by the ‘endpoint 2’ of the Booking service. 

URL: /transaction 
HTTP METHOD: POST
RequestBody: paymentMode, bookingId, upiId, cardNumber
Response Status: Created
Response: ResponseEntity<transactionId>



EndPoint 2: This endpoint presents the transaction details to the user based on the transactionId provided by the user.

 URL: /transaction/{transactionId}
 HTTP METHOD: GET
 RequestBody: (PathVariable) int
 Response Status: OK
 Response: ResponseEntity<TransactionDetailsEntity>

 
2.2 Configure the service to run on port 8083.
2.3 Configure the service as a Eureka client. 
