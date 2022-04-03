BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE Reservation';
    EXECUTE IMMEDIATE 'DROP TABLE Invoice';
    EXECUTE IMMEDIATE 'DROP TABLE Customer';
    EXECUTE IMMEDIATE 'DROP TABLE HotelServices';
    EXECUTE IMMEDIATE 'DROP TABLE Rooms';
    EXECUTE IMMEDIATE 'DROP TABLE Events';
    EXECUTE IMMEDIATE 'DROP TABLE Payment';
    EXECUTE IMMEDIATE 'DROP TABLE Hotel';
    EXECUTE IMMEDIATE 'DROP TABLE Facility';


EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -942 THEN
            RAISE;
        END IF;
END;

CREATE TABLE Facility(
                         facilityID	INT	NOT NULL,
                         facilityType	CHAR(20),
                         facilityAmenities CHAR(20),
                         facilityCapacity INT NOT NULL,
                         dateAvailable DATE,
                         PRIMARY KEY (facilityID)
);
INSERT INTO Facility VALUES (50,'Aquatic Centre', 'Pool',500, TO_DATE('2020-03-11','YYYY-MM-DD'));

CREATE TABLE Hotel (
                       hotelID	INT NOT NULL,
                       hotelType CHAR(20),
                       hotelName CHAR(20) NOT NULL,
                       PRIMARY KEY (hotelID)
);
INSERT INTO Hotel VALUES (13,'Resort','Resorteros');
INSERT INTO Hotel VALUES (24,'Airport','Airporteros');
INSERT INTO Hotel VALUES (12,'Motel','Moteleros');
INSERT INTO Hotel VALUES (22,'Chain','Chaineros');

CREATE TABLE Payment (
                         paymentNumber	INT	NOT NULL,
                         paymentAmount	DEC	NOT NULL,
                         paymentMethod CHAR(20) NOT NULL,
                         paymentDate DATE,
                         PRIMARY KEY (paymentNumber)
);
INSERT INTO Payment VALUES (192837,3049.00, 'Credit', TO_DATE('2020-10-21','YYYY-MM-DD'));
INSERT INTO Payment VALUES (394856,39473.00, 'Debit', TO_DATE('2020-02-11','YYYY-MM-DD'));
INSERT INTO Payment VALUES (485739,3748.00, 'Debit', TO_DATE('2020-03-16','YYYY-MM-DD'));
INSERT INTO Payment VALUES (384759,2937.00, 'Credit', TO_DATE('2020-01-21','YYYY-MM-DD'));
INSERT INTO Payment VALUES (183754,3847.00, 'Credit', TO_DATE('2020-08-02','YYYY-MM-DD'));
INSERT INTO Payment VALUES (488758,4000.00, 'Debit', TO_DATE('2020-08-03','YYYY-MM-DD'));
INSERT INTO Payment VALUES (224145,473.00, 'Debit', TO_DATE('2020-08-21','YYYY-MM-DD'));




CREATE TABLE Events (
                        eventID	INT NOT NULL,
                        numberofGuests INT NOT NULL,
                        eventType CHAR(20),
                        eventQuota DEC,
                        dateAvailable DATE,
                        PRIMARY KEY (eventID)
);
INSERT INTO Events VALUES (23453,300,'Wedding', 5010.00, TO_DATE('2020-03-11','YYYY-MM-DD'));


CREATE TABLE Rooms
(
    roomNo       INT	NOT NULL,
    floorNo      INT,
    roomType     CHAR(20),
    numberOfBeds INT,
    hotelID      INT	NOT NULL,
    typeOfView   CHAR(20),
    roomPrice    INT	NOT NULL,
    typeOfBed    CHAR(20),
    PRIMARY KEY (roomNo),
    FOREIGN KEY (hotelID) REFERENCES Hotel
);
INSERT INTO Rooms VALUES (300,3, 'Suite',null,13,'Ocean',20.00,null);
INSERT INTO Rooms VALUES (100,1, 'Multiple',3,13,null,1020.00,null );
INSERT INTO Rooms VALUES (123,1, 'Single',null,24,null,100.00,'Queen' );
INSERT INTO Rooms VALUES (200,2, 'Suite',null,12,'Garden',1.00,null);
INSERT INTO Rooms VALUES (150,1, 'Suite',null,22,'Ocean',150.00,null  );

CREATE TABLE HotelServices (
                               serviceID	INT  NOT NULL,
                               serviceName CHAR(20),
                               serviceType CHAR(20),
                               servicePrice DEC,
                               PRIMARY KEY (serviceID)
);
INSERT INTO HotelServices VALUES (2938, 'Room Service', 'Room', 100.00);
INSERT INTO HotelServices VALUES (3948, 'Massage', 'Spa', 10.00);


CREATE TABLE Customer (
                          customerID	INT	NOT NULL,
                          customerName	CHAR(20)	NOT NULL,
                          customerPhone         INT	NOT NULL,
                          customerEmail	CHAR(20) NOT NULL,
                          PRIMARY KEY (customerID),
                          UNIQUE (customerEmail)
);

INSERT INTO Customer VALUES (73648,'Shania Twain',9283756504,'shania@gmail.com');
INSERT INTO Customer VALUES (92837,'Justin Bieber',8372915485,'jb@gmail.com');
INSERT INTO Customer VALUES (46352,'Prince Philips',2735485960,'pp@gmail.com');
INSERT INTO Customer VALUES (85769,'Elisa Liu',1246584910,'el@gmail.com');
INSERT INTO Customer VALUES (36252,'Brad Pitt',4193520673,'bp@gmail.com');

CREATE TABLE Invoice (
                         invoiceNumber INT NOT NULL,
                         invoiceDate DATE,
                         customerID	INT	NOT NULL,
                         hotelID	INT NOT NULL,
                         invoiceAmount	DEC	NOT NULL,
                         serviceID INT,
                         facilityID	INT,
                         paymentNumber	INT	NOT NULL,
                         PRIMARY KEY (invoiceNumber),
                         FOREIGN KEY (customerID) REFERENCES Customer,
                         FOREIGN KEY (hotelID) REFERENCES Hotel,
                         FOREIGN KEY (serviceID) REFERENCES HotelServices,
                         FOREIGN KEY (facilityID) REFERENCES Facility,
                         FOREIGN KEY (paymentNumber) REFERENCES Payment

);

INSERT INTO Invoice VALUES (92847563,TO_DATE('2020-10-21','YYYY-MM-DD'),73648,13,3049.00,2938,null,192837);
INSERT INTO Invoice VALUES (10293846,TO_DATE('2020-02-11','YYYY-MM-DD'),92837,13,39473.00,3948,null,394856);
INSERT INTO Invoice VALUES (38273957,TO_DATE('2020-03-16','YYYY-MM-DD'),46352,24,3748.00,null,50,485739);
INSERT INTO Invoice VALUES (38264061,TO_DATE('2020-01-21','YYYY-MM-DD'),85769,12,2937.00,null,null,384759);
INSERT INTO Invoice VALUES (27364951,TO_DATE('2020-08-02','YYYY-MM-DD'),36252,22,3847.00,null,null,183754);
/*new row for population*/
INSERT INTO Invoice VALUES (87384958,TO_DATE('2020-08-03','YYYY-MM-DD'),73648,22,4000.00,3948,null,488758);
INSERT INTO Invoice VALUES (40543821,TO_DATE('2020-08-21','YYYY-MM-DD'),92837,13,473.00,2938,null,224145);


CREATE TABLE Reservation (
     reservationID	INT	NOT NULL,
     reservationDate	DATE	NOT NULL,
     checkInDate	DATE	NOT NULL,
     checkOutDate	DATE	NOT NULL,
     roomNo	INT,
     customerID	INT	NOT NULL,
     hotelID	INT	NOT NULL,
     invoiceNumber	INT	NOT NULL,
     eventID	INT,
     facilityID	INT,
     PRIMARY KEY (reservationID),
     UNIQUE (customerID, hotelID, invoiceNumber),
     FOREIGN KEY (customerID) REFERENCES Customer,
     FOREIGN KEY (hotelID) REFERENCES Hotel,
     FOREIGN KEY (eventID) REFERENCES Events,
     FOREIGN KEY (facilityID) REFERENCES Facility,
     FOREIGN KEY (invoiceNumber) REFERENCES Invoice
);

INSERT INTO Reservation VALUES (1111111111, TO_DATE('2020-10-20','YYYY-MM-DD'), TO_DATE('2020-11-19','YYYY-MM-DD'), TO_DATE('2020-11-20','YYYY-MM-DD'),300,73648,13,92847563, null,null);
INSERT INTO Reservation VALUES (2222222222, TO_DATE('2020-02-10','YYYY-MM-DD'), TO_DATE('2020-03-11','YYYY-MM-DD'), TO_DATE('2020-03-14','YYYY-MM-DD'),100,92837,13,10293846, 23453,50);
INSERT INTO Reservation VALUES (3333333333, TO_DATE('2020-03-15','YYYY-MM-DD'), TO_DATE('2020-03-16','YYYY-MM-DD'), TO_DATE('2020-03-25','YYYY-MM-DD'),123,46352,24,38273957,null, null);
INSERT INTO Reservation VALUES (4444444444, TO_DATE('2020-01-20','YYYY-MM-DD'), TO_DATE('2020-02-21','YYYY-MM-DD'), TO_DATE('2020-03-01','YYYY-MM-DD'),200,85769,12,38264061,null,null );
INSERT INTO Reservation VALUES (5555555555, TO_DATE('2020-08-01','YYYY-MM-DD'), TO_DATE('2020-09-02','YYYY-MM-DD'), TO_DATE('2020-09-02','YYYY-MM-DD'),150,36252,22,27364951,null,null );

COMMIT;





















