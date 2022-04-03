-- Delete Reservation
DELETE FROM RESERVATION WHERE RESERVATIONID = 1111111111;



-- Insert new Reservation
INSERT INTO Reservation VALUES (1111111111, TO_DATE('2020-10-20','YYYY-MM-DD'), TO_DATE('2020-11-19','YYYY-MM-DD'), TO_DATE('2020-11-20','YYYY-MM-DD'),300,73648,13,92847563,23453,50);



-- Aggregation to find most expensive Invoice
SELECT * FROM INVOICE WHERE INVOICEAMOUNT = (SELECT  MAX(INVOICEAMOUNT) FROM  INVOICE);



-- Nested Aggregation to find maximum average invoice of all customers
SELECT MAX(avg) FROM
(SELECT AVG(INVOICEAMOUNT) AS avg FROM INVOICE GROUP BY CUSTOMERID);



-- Join to find mail addresses of Customers who stays more than one week in a hotel.
SELECT DISTINCT s.CUSTOMEREMAIL
FROM  CUSTOMER s, RESERVATION r
WHERE (r.CHECKOUTDATE-r.CHECKINDATE) > 7 AND s.CUSTOMERID =r.CUSTOMERID;



-- Selection from Invoice
SELECT * FROM INVOICE WHERE HOTELID = 13;



-- Projection from Reservation
SELECT CUSTOMERID FROM RESERVATION;



-- Division to fin Customers who use all available hotel services
SELECT c.CUSTOMERID FROM CUSTOMER c
WHERE NOT EXISTS((SELECT s.serviceID FROM HOTELSERVICES s)
MINUS (SELECT a.serviceID FROM INVOICE a WHERE a.CUSTOMERID = c.CUSTOMERID and a.SERVICEID IS NOT NULL));



-- Update on Hotel
UPDATE HOTEL SET HOTELTYPE = 'Standard ', HOTELNAME = 'Standarderos' WHERE HOTELID = 13;



SELECT * FROM RESERVATION;


SELECT * FROM INVOICE;