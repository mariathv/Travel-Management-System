-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: b9vpudlz4yz3pd30claw-mysql.services.clever-cloud.com:3306
-- Generation Time: Nov 16, 2024 at 03:41 PM
-- Server version: 8.0.22-13
-- PHP Version: 8.2.21

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
-- Table structure for table `BusService`
--

CREATE TABLE `BusService` (
  `ServiceID` int NOT NULL,
  `StationName` varchar(1000) NOT NULL,
  `StationLocation` varchar(1000) NOT NULL,
  `BusNo` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `BusService`
--

INSERT INTO `BusService` (`ServiceID`, `StationName`, `StationLocation`, `BusNo`) VALUES
(1, 'FaisalMovers', 'Faizabad', 'TAF-101'),
(4, 'StationHEH', 'Faizabad', 'ABC-101'),
(8, 'station', 'lahore', 'dsa-ewq');

-- --------------------------------------------------------

--
-- Table structure for table `Customer`
--

CREATE TABLE `Customer` (
  `CustomerID` int NOT NULL,
  `name` varchar(1000) NOT NULL,
  `email` varchar(1000) NOT NULL,
  `phoneNo` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `customerAuth`
--

CREATE TABLE `customerAuth` (
  `userID` int NOT NULL,
  `username` varchar(1000) NOT NULL,
  `password` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `FlightService`
--

CREATE TABLE `FlightService` (
  `ServiceID` int NOT NULL,
  `AirportName` varchar(1000) NOT NULL,
  `AirportLocation` varchar(1000) NOT NULL,
  `FlightNumber` varchar(1000) NOT NULL,
  `GateNumber` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `FlightService`
--

INSERT INTO `FlightService` (`ServiceID`, `AirportName`, `AirportLocation`, `FlightNumber`, `GateNumber`) VALUES
(5, 'Islamabad Airport', 'Islamabad', 'A-101', '2'),
(9, 'swat', 'swat', '432-321', 'gate_04');

-- --------------------------------------------------------

--
-- Table structure for table `HotelService`
--

CREATE TABLE `HotelService` (
  `HotelServiceID` int NOT NULL,
  `ServiceProviderID` int NOT NULL,
  `HotelName` varchar(1000) NOT NULL,
  `HotelLocation` varchar(1000) NOT NULL,
  `Rating` int NOT NULL,
  `BasicRoomPrice` int NOT NULL,
  `DoubleRoomPrice` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ServiceFeedback`
--

CREATE TABLE `ServiceFeedback` (
  `serviceID` int NOT NULL,
  `rating` int NOT NULL,
  `comment` varchar(1000) NOT NULL,
  `customerID` int NOT NULL,
  `customerUsername` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ServiceProvider`
--

CREATE TABLE `ServiceProvider` (
  `serviceProviderID` int NOT NULL,
  `phoneNum` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `email` varchar(1000) NOT NULL,
  `travelAgencyName` varchar(1000) NOT NULL,
  `name` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `rating` int DEFAULT NULL,
  `serviceType` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ServiceProvider`
--

INSERT INTO `ServiceProvider` (`serviceProviderID`, `phoneNum`, `email`, `travelAgencyName`, `name`, `rating`, `serviceType`) VALUES
(8, NULL, 'mariank@gmail.com', 'MariaNK', NULL, 0, 'Hotel'),
(9, NULL, 'ammarhussain@gmail.com', 'AmmarTravels', NULL, 0, 'Bus'),
(10, NULL, 'rehannaeem@gmail.com', 'RehanTravels', NULL, 0, 'Flight'),
(11, NULL, 'faizan@gmail.com', 'FaizanTravels', NULL, 0, 'Train'),
(14, NULL, '01.com', '01_travels', NULL, 0, 'Flight'),
(15, NULL, 'heu@gmail.com', 'HeuHotel', NULL, 0, 'Hotel');

-- --------------------------------------------------------

--
-- Table structure for table `serviceProviderAuth`
--

CREATE TABLE `serviceProviderAuth` (
  `userID` int NOT NULL,
  `username` varchar(1000) NOT NULL,
  `password` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

--
-- Dumping data for table `serviceProviderAuth`
--

INSERT INTO `serviceProviderAuth` (`userID`, `username`, `password`) VALUES
(8, 'MariaNK', 'sllg123hop'),
(9, 'AmmarH', '123456'),
(10, 'RehanNKh', '123456'),
(11, 'FaizanNaeem', '123456'),
(12, 'faiq', '1234'),
(13, 'faiq', '12345'),
(14, 'Sample01', '01'),
(15, 'heu', '123');

-- --------------------------------------------------------

--
-- Table structure for table `TrainService`
--

CREATE TABLE `TrainService` (
  `ServiceID` int NOT NULL,
  `StationName` varchar(1000) NOT NULL,
  `StationLocation` varchar(1000) NOT NULL,
  `TrainNumber` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `TrainService`
--

INSERT INTO `TrainService` (`ServiceID`, `StationName`, `StationLocation`, `TrainNumber`) VALUES
(6, 'Lahore Junction', 'Swami Nagar, Lahore', 'A32'),
(7, 'Railway Station', 'H 9/1 H-9, Islamabad', 'A113');

-- --------------------------------------------------------

--
-- Table structure for table `TravelService`
--

CREATE TABLE `TravelService` (
  `serviceID` int NOT NULL,
  `serviceProviderID` int NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `serviceType` varchar(100) NOT NULL,
  `arrivalTime` varchar(1000) NOT NULL,
  `departureTime` varchar(1000) NOT NULL,
  `arrivalLocation` varchar(1000) NOT NULL,
  `departureLocation` varchar(1000) NOT NULL,
  `departureDate` varchar(1000) NOT NULL,
  `arrivalDate` varchar(1000) NOT NULL,
  `ticketPrice` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `TravelService`
--

INSERT INTO `TravelService` (`serviceID`, `serviceProviderID`, `description`, `serviceType`, `arrivalTime`, `departureTime`, `arrivalLocation`, `departureLocation`, `departureDate`, `arrivalDate`, `ticketPrice`) VALUES
(1, 9, 'Travel Service: Bus No. TAF-101 from Islamabad to Lahore departing on 12th Nov at 9:00PM and arriving on 13th Nov at 2:00AM. Ticket Price: PKR 1500. Bus station: FaisalMovers located at Faizabad.', 'Bus', '2:00AM', '9:00PM', 'Lahore', 'Islamabad', '12th Nov', '13th Nov', 1500),
(4, 9, 'Travel Service: Bus No. ABC-101 from Islamabad to Karachi departing on 11th Nov at 1:00PM and arriving on 12th Nov at 10:00PM. Ticket Price: PKR 5000. Bus station: StationHEH located at Faizabad.', 'Bus', '10:00PM', '1:00PM', 'Karachi', 'Islamabad', '11th Nov', '12th Nov', 5000),
(5, 10, 'Bus No. A-101 from Islamabad to Karachi departing on 12th Nov at 1:00PM and arriving on 12th Nov at 3:00PM. \nTicket Price: PKR 20000. \nBus station: Islamabad Airport located at Islamabad.', 'Flight', '3:00PM', '1:00PM', 'Karachi', 'Islamabad', '12th Nov', '12th Nov', 20000),
(6, 11, 'Bus No. A32 from Lahore to Islamabad departing on 12th Nov at 10:00AM and arriving on 12th Nov at 4:00AM. \nTicket Price: PKR 2000. \nBus station: Lahore Junction located at Swami Nagar, Lahore.', 'Train', '4:00AM', '10:00AM', 'Islamabad', 'Lahore', '12th Nov', '12th Nov', 2000),
(7, 11, 'Bus No. A113 from Islamabad to Lahore departing on 14th Nov at 12:00PM and arriving on 14th Nov at 4:00PM. \nTicket Price: PKR 2500. \nBus station: Railway Station located at H 9/1 H-9, Islamabad.', 'Train', '4:00PM', '12:00PM', 'Lahore', 'Islamabad', '14th Nov', '14th Nov', 2500),
(8, 9, 'Travel Service: Bus No. dsa-ewq from lahore to Hyderabad departing on 11-11 at 10:00 and arriving on 12-11 at 11:00. Ticket Price: PKR 5432. Bus station: station located at lahore.', 'Bus', '11:00', '10:00', 'Hyderabad', 'lahore', '11-11', '12-11', 5432),
(9, 14, 'Bus No. 432-321 from swat to karachi departing on 25-11 at 11:00 and arriving on 25-11 at 17:00. \nTicket Price: PKR 12345. \nBus station: swat located at swat.', 'Flight', '17:00', '11:00', 'karachi', 'swat', '25-11', '25-11', 12345);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customerAuth`
--
ALTER TABLE `customerAuth`
  ADD PRIMARY KEY (`userID`);

--
-- Indexes for table `HotelService`
--
ALTER TABLE `HotelService`
  ADD PRIMARY KEY (`HotelServiceID`);

--
-- Indexes for table `serviceProviderAuth`
--
ALTER TABLE `serviceProviderAuth`
  ADD PRIMARY KEY (`userID`);

--
-- Indexes for table `TravelService`
--
ALTER TABLE `TravelService`
  ADD PRIMARY KEY (`serviceID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customerAuth`
--
ALTER TABLE `customerAuth`
  MODIFY `userID` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `HotelService`
--
ALTER TABLE `HotelService`
  MODIFY `HotelServiceID` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `serviceProviderAuth`
--
ALTER TABLE `serviceProviderAuth`
  MODIFY `userID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `TravelService`
--
ALTER TABLE `TravelService`
  MODIFY `serviceID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
