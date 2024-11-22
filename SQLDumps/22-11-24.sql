-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3310
-- Generation Time: Nov 22, 2024 at 06:15 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `b9vpudlz4yz3pd30claw`
--

-- --------------------------------------------------------

--
-- Table structure for table `busservice`
--

CREATE TABLE `busservice` (
  `ServiceID` int(11) NOT NULL,
  `StationName` varchar(1000) NOT NULL,
  `StationLocation` varchar(1000) NOT NULL,
  `BusNo` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `busservice`
--

INSERT INTO `busservice` (`ServiceID`, `StationName`, `StationLocation`, `BusNo`) VALUES
(11, 'Faizabad', 'Islamabad', '123-123'),
(12, 'Skyways', 'Faizabad', 'A-304'),
(13, 'Station222', 'MultanStation', 'AFE-303'),
(14, 'GBStation', 'C3HV', 'FAE-123'),
(16, 'RandStation', 'H11', 'TAF-909');

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `CustomerID` int(11) NOT NULL,
  `name` varchar(1000) NOT NULL,
  `email` varchar(1000) NOT NULL,
  `phoneNo` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`CustomerID`, `name`, `email`, `phoneNo`) VALUES
(1, 'Ammar', 'ammar.com', '1234563'),
(3, 'abc', 'abc.com', '123'),
(4, 'was', 'was.com', '1234'),
(5, 'faiq', '1234.com', '1234'),
(6, 'xyz', 'xyz.com', '1234'),
(7, 'asd', 'asd.com', '123'),
(8, 'maria', 'maria.com', '1234'),
(9, 'heuheu', 'heuheu@gmail.com', '12345678');

-- --------------------------------------------------------

--
-- Table structure for table `customerauth`
--

CREATE TABLE `customerauth` (
  `userID` int(11) NOT NULL,
  `username` varchar(1000) NOT NULL,
  `password` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `customerauth`
--

INSERT INTO `customerauth` (`userID`, `username`, `password`) VALUES
(1, 'Ammar', '12'),
(2, 'faiq', '123'),
(3, 'abc', '123'),
(4, 'was', '123'),
(5, 'faiq', '1234'),
(6, 'xyz', '111'),
(7, 'asd', '1111'),
(8, 'maria', '111'),
(9, 'heu', '123');

-- --------------------------------------------------------

--
-- Table structure for table `customernotifications`
--

CREATE TABLE `customernotifications` (
  `customerID` int(11) NOT NULL,
  `message` varchar(2000) NOT NULL,
  `date` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `customernotifications`
--

INSERT INTO `customernotifications` (`customerID`, `message`, `date`) VALUES
(1, 'Your booking has been cancelled due to service unavailability (4), you have been refunded', '2024-11-21');

-- --------------------------------------------------------

--
-- Table structure for table `flightservice`
--

CREATE TABLE `flightservice` (
  `ServiceID` int(11) NOT NULL,
  `AirportName` varchar(1000) NOT NULL,
  `AirportLocation` varchar(1000) NOT NULL,
  `FlightNumber` varchar(1000) NOT NULL,
  `GateNumber` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `hotelbooking`
--

CREATE TABLE `hotelbooking` (
  `bookingID` int(11) NOT NULL,
  `customerID` int(11) NOT NULL,
  `listingID` int(11) NOT NULL,
  `roomType` varchar(1000) NOT NULL,
  `price` int(11) NOT NULL,
  `bookingDate` varchar(1000) NOT NULL,
  `username` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `hotelbooking`
--

INSERT INTO `hotelbooking` (`bookingID`, `customerID`, `listingID`, `roomType`, `price`, `bookingDate`, `username`) VALUES
(2, 1, 2, 'Basic', 15000, '11/22/2024', 'Rehan');

-- --------------------------------------------------------

--
-- Table structure for table `hotelfeedback`
--

CREATE TABLE `hotelfeedback` (
  `serviceID` int(11) NOT NULL,
  `customerID` int(11) NOT NULL,
  `customerUsername` varchar(1000) NOT NULL,
  `rating` int(11) NOT NULL,
  `comment` varchar(2000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hotelfeedback`
--

INSERT INTO `hotelfeedback` (`serviceID`, `customerID`, `customerUsername`, `rating`, `comment`) VALUES
(1, 1, 'Ammar', 3, 'Average Experience');

-- --------------------------------------------------------

--
-- Table structure for table `hotelservice`
--

CREATE TABLE `hotelservice` (
  `HotelServiceID` int(11) NOT NULL,
  `ServiceProviderID` int(11) NOT NULL,
  `HotelName` varchar(1000) NOT NULL,
  `HotelLocation` varchar(1000) NOT NULL,
  `Rating` int(11) NOT NULL,
  `BasicRoomPrice` int(11) NOT NULL,
  `DoubleRoomPrice` int(11) NOT NULL,
  `city` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `hotelservice`
--

INSERT INTO `hotelservice` (`HotelServiceID`, `ServiceProviderID`, `HotelName`, `HotelLocation`, `Rating`, `BasicRoomPrice`, `DoubleRoomPrice`, `city`) VALUES
(2, 8, 'MNKHotels', 'SectorJ,DHA2', 1, 15000, 20000, 'Islamabad');

-- --------------------------------------------------------

--
-- Table structure for table `servicefeedback`
--

CREATE TABLE `servicefeedback` (
  `serviceID` int(11) NOT NULL,
  `rating` int(11) NOT NULL,
  `comment` varchar(1000) NOT NULL,
  `customerID` int(11) NOT NULL,
  `customerUsername` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `serviceprovider`
--

CREATE TABLE `serviceprovider` (
  `serviceProviderID` int(11) NOT NULL,
  `phoneNum` varchar(1000) DEFAULT NULL,
  `email` varchar(1000) NOT NULL,
  `travelAgencyName` varchar(1000) NOT NULL,
  `name` varchar(1000) DEFAULT NULL,
  `rating` int(11) DEFAULT NULL,
  `serviceType` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `serviceprovider`
--

INSERT INTO `serviceprovider` (`serviceProviderID`, `phoneNum`, `email`, `travelAgencyName`, `name`, `rating`, `serviceType`) VALUES
(8, NULL, 'mariank@gmail.com', 'MariaNK', NULL, 0, 'Hotel'),
(9, '123', 'ammarhussain@gmail.com', 'AmmarTravels', NULL, 0, 'Bus'),
(10, NULL, 'rehannaeem@gmail.com', 'RehanTravels', NULL, 0, 'Flight'),
(11, NULL, 'faizan@gmail.com', 'FaizanTravels', NULL, 0, 'Train'),
(14, NULL, '01.com', '01_travels', NULL, 0, 'Flight'),
(15, NULL, 'heu@gmail.com', 'HeuHotel', NULL, 0, 'Hotel'),
(16, NULL, 'aaa', 'aaa_Travel', NULL, 0, 'Bus');

-- --------------------------------------------------------

--
-- Table structure for table `serviceproviderauth`
--

CREATE TABLE `serviceproviderauth` (
  `userID` int(11) NOT NULL,
  `username` varchar(1000) NOT NULL,
  `password` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=COMPACT;

--
-- Dumping data for table `serviceproviderauth`
--

INSERT INTO `serviceproviderauth` (`userID`, `username`, `password`) VALUES
(8, 'MariaNK', 'sllg123hop'),
(9, 'AmmarH', 'heh'),
(10, 'RehanNKh', '123456'),
(11, 'FaizanNaeem', '123456'),
(12, 'faiq', '1234'),
(13, 'faiq', '12345'),
(14, 'Sample01', '01'),
(15, 'heu', '123'),
(16, 'aaa', '111');

-- --------------------------------------------------------

--
-- Table structure for table `trainservice`
--

CREATE TABLE `trainservice` (
  `ServiceID` int(11) NOT NULL,
  `StationName` varchar(1000) NOT NULL,
  `StationLocation` varchar(1000) NOT NULL,
  `TrainNumber` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `trainservice`
--

INSERT INTO `trainservice` (`ServiceID`, `StationName`, `StationLocation`, `TrainNumber`) VALUES
(17, 'RailwayStation', 'H-11', 'A-12');

-- --------------------------------------------------------

--
-- Table structure for table `travelbooking`
--

CREATE TABLE `travelbooking` (
  `bookingID` int(11) NOT NULL,
  `customerID` int(11) NOT NULL,
  `serviceID` int(11) NOT NULL,
  `TotalPrice` int(11) NOT NULL,
  `serviceType` varchar(1000) NOT NULL,
  `bookingDate` varchar(1000) NOT NULL,
  `username` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `travelbooking`
--

INSERT INTO `travelbooking` (`bookingID`, `customerID`, `serviceID`, `TotalPrice`, `serviceType`, `bookingDate`, `username`) VALUES
(6, 1, 12, 1500, 'Bus', '11/22/2024', 'Ammar');

-- --------------------------------------------------------

--
-- Table structure for table `travelservice`
--

CREATE TABLE `travelservice` (
  `serviceID` int(11) NOT NULL,
  `serviceProviderID` int(11) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `serviceType` varchar(100) NOT NULL,
  `arrivalTime` varchar(1000) NOT NULL,
  `departureTime` varchar(1000) NOT NULL,
  `arrivalLocation` varchar(1000) NOT NULL,
  `departureLocation` varchar(1000) NOT NULL,
  `departureDate` varchar(1000) NOT NULL,
  `arrivalDate` varchar(1000) NOT NULL,
  `ticketPrice` int(11) NOT NULL,
  `status` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `travelservice`
--

INSERT INTO `travelservice` (`serviceID`, `serviceProviderID`, `description`, `serviceType`, `arrivalTime`, `departureTime`, `arrivalLocation`, `departureLocation`, `departureDate`, `arrivalDate`, `ticketPrice`, `status`) VALUES
(11, 16, 'Bus No. 123-123 from Islamabad to Karachi departing on 01-01 at 12:00 and arriving on 02-01 at 20:00. \nTicket Price: PKR 12000. \nBus station: Faizabad located at Islamabad.', 'Bus', '20:00', '12:00', 'Karachi', 'Islamabad', '01-01', '02-01', 12000, 'ONGOING'),
(12, 9, 'Bus No. A-304 from Karachi to Multan departing on 13th Nov at 11:00AM and arriving on 13th Nov at 5:00PM. \nTicket Price: PKR 2000. \nBus station: Skyways located at Faizabad.', 'Bus', '5:00PM', '11:00AM', 'Multan', 'Karachi', '13th Nov', '13th Nov', 2000, 'DONE'),
(13, 9, 'Bus No. AFE-303 from Multan to Islamabad departing on 15th Nov at 10:00AM and arriving on 15th Nov at 4:00PM. \nTicket Price: PKR 2000. \nBus station: Station222 located at MultanStation.', 'Bus', '4:00PM', '10:00AM', 'Islamabad', 'Multan', '15th Nov', '15th Nov', 2000, 'DONE'),
(14, 9, 'Bus No. FAE-123 from Faislabad to Islamabad departing on 16th Nov at 1:00PM and arriving on 16th Nov at 6:PM. \nTicket Price: PKR 5000. \nBus station: GBStation located at C3HV.', 'Bus', '6:PM', '1:00AM', 'Islamabad', 'Faislaba', '16th Nov', '16th Nov', 5000, 'ONGOING'),
(16, 9, 'Bus No. TAF-909 from Islamabad to Rawat departing on 13th Nov at 1:00PM and arriving on 13th Nov at 2:00PM. \nTicket Price: PKR 500. \nBus station: RandStation located at H11.', 'Bus', '2:00PM', '1:00PM', 'Rawat', 'Islamabad', '13th Nov', '13th Nov', 500, 'ONGOING'),
(17, 11, 'Bus No. A-11 from Islamabad to Lahore departing on 11th Nov at 1:00PM and arriving on 11th Nov at 4:00PM. \nTicket Price: PKR 5000. \nBus station: RailwayStation located at H-11.', 'Train', '4:00PM', '1:00PM', 'Lahore', 'Islamabad', '11th Nov', '11th Nov', 5000, 'DONE');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customerauth`
--
ALTER TABLE `customerauth`
  ADD PRIMARY KEY (`userID`);

--
-- Indexes for table `hotelbooking`
--
ALTER TABLE `hotelbooking`
  ADD PRIMARY KEY (`bookingID`);

--
-- Indexes for table `hotelservice`
--
ALTER TABLE `hotelservice`
  ADD PRIMARY KEY (`HotelServiceID`);

--
-- Indexes for table `serviceproviderauth`
--
ALTER TABLE `serviceproviderauth`
  ADD PRIMARY KEY (`userID`);

--
-- Indexes for table `travelbooking`
--
ALTER TABLE `travelbooking`
  ADD PRIMARY KEY (`bookingID`);

--
-- Indexes for table `travelservice`
--
ALTER TABLE `travelservice`
  ADD PRIMARY KEY (`serviceID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customerauth`
--
ALTER TABLE `customerauth`
  MODIFY `userID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `hotelbooking`
--
ALTER TABLE `hotelbooking`
  MODIFY `bookingID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `hotelservice`
--
ALTER TABLE `hotelservice`
  MODIFY `HotelServiceID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `serviceproviderauth`
--
ALTER TABLE `serviceproviderauth`
  MODIFY `userID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `travelbooking`
--
ALTER TABLE `travelbooking`
  MODIFY `bookingID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `travelservice`
--
ALTER TABLE `travelservice`
  MODIFY `serviceID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
