-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3310
-- Generation Time: Nov 25, 2024 at 09:47 PM
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
(38, 'KainatTravels', 'Road 12, Sadder', 'Bus-123'),
(39, 'KainatTravels', 'Road 12, Sadder', 'Bus-222'),
(40, 'KainatTravels', 'Road 12, Sadder', 'Bus-333');

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
(10, 'Ammar', 'Cus1.com', '1234'),
(11, 'Maria', 'Cus2.com', '987654321'),
(12, 'Faiq', 'Cus3.com', '982123');

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
(10, 'Ammar', '1'),
(11, 'Maria', '2'),
(12, 'Faiq', '3');

-- --------------------------------------------------------

--
-- Table structure for table `customernotifications`
--

CREATE TABLE `customernotifications` (
  `customerID` int(11) NOT NULL,
  `message` varchar(2000) NOT NULL,
  `date` varchar(1000) NOT NULL,
  `agency` varchar(1000) NOT NULL,
  `tag` varchar(100) NOT NULL,
  `type` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `customernotifications`
--

INSERT INTO `customernotifications` (`customerID`, `message`, `date`, `agency`, `tag`, `type`) VALUES
(10, 'Thank you for traveling with Kainat_Travels. We hope you had a great experience!', '26/11/24 (00:44)', 'Kainat_Travels', 'Completed', 'COMPLETE'),
(11, 'Thank you for traveling with Kainat_Travels. We hope you had a great experience!', '26/11/24 (00:44)', 'Kainat_Travels', 'Completed', 'COMPLETE'),
(12, 'Your booking has been cancelled due to service unavailability (45), you have been refunded', '26/11/24 (01:42)', 'Kainat_Travels', 'Cancelled', 'Bus');

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

--
-- Dumping data for table `flightservice`
--

INSERT INTO `flightservice` (`ServiceID`, `AirportName`, `AirportLocation`, `FlightNumber`, `GateNumber`) VALUES
(43, 'Quetta Airport', 'Sadder', 'FL-A17', 'Gate 4'),
(44, 'Quetta Airport', 'Sadder', 'FL-225A', 'Gate 4');

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
  `username` varchar(1000) NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `hotelbooking`
--

INSERT INTO `hotelbooking` (`bookingID`, `customerID`, `listingID`, `roomType`, `price`, `bookingDate`, `username`, `status`) VALUES
(1, 10, 15, 'Basic Room', 40999, '2024-11-25', 'Ammar', 1),
(2, 10, 16, 'Double Room', 5000, '2024-11-25', 'Ammar', 2),
(3, 10, 14, 'Basic Room', 25999, '2024-11-25', 'Ammar', 1),
(4, 11, 14, 'Double Room', 40000, '2024-11-26', 'Maria', 1),
(5, 11, 15, 'Double Room', 60900, '2024-11-26', 'Maria', 1),
(6, 12, 14, 'Double Room', 40000, '2024-11-26', 'Faiq', 1),
(7, 12, 17, 'Double Room', 12000, '2024-11-26', 'Faiq', 1),
(8, 12, 16, 'Double Room', 5000, '2024-11-26', 'Faiq', 1);

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
(14, 20, 'Pearl Hotels', 'DHA', 1, 25999, 40000, 'Islamabad'),
(15, 20, 'Delux Hotels', 'Bahria', 1, 40999, 60900, 'Islamabad'),
(16, 20, 'AAA Hotels', 'PWD Colony', 1, 2000, 5000, 'Islamabad'),
(17, 20, 'Karachi Hotels', 'North Naziamabad', 1, 6000, 12000, 'Karachi'),
(18, 20, 'Lahore Hotels', 'Bahria Phase 2', 1, 6000, 12000, 'Lahore');

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

--
-- Dumping data for table `servicefeedback`
--

INSERT INTO `servicefeedback` (`serviceID`, `rating`, `comment`, `customerID`, `customerUsername`) VALUES
(40, 5, 'Best Service', 10, 'Ammar'),
(40, 1, 'pathetic Service', 11, 'Maria');

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
(17, NULL, 'BusSP1', 'Kainat_Travels', NULL, 3, 'Bus'),
(18, NULL, 'TrainSP1.com', 'GreenLine', NULL, 0, 'Train'),
(19, NULL, 'FlightSP1.com', 'AIRBLUE', NULL, 0, 'Flight'),
(20, NULL, 'HotelSP1.com', 'Five_Star_Hotels', NULL, 0, 'Hotel');

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
(17, 'KainatTravel', '111'),
(18, 'Greenline', '111'),
(19, 'Airblue', '111'),
(20, 'FiveStar', '111');

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
(41, 'Quetta Station', 'Road 12, Sadder', 'TR-102'),
(42, 'Quetta Station', 'Road 12, Sadder', 'TR-555');

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
  `username` varchar(1000) NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `travelbooking`
--

INSERT INTO `travelbooking` (`bookingID`, `customerID`, `serviceID`, `TotalPrice`, `serviceType`, `bookingDate`, `username`, `status`) VALUES
(1, 10, 39, 5990, 'Bus', '2024-11-25', 'Ammar', 2),
(2, 10, 44, 29999, 'Flight', '2024-11-25', 'Ammar', 1),
(3, 10, 41, 10000, 'Train', '2024-11-25', 'Ammar', 1),
(4, 10, 40, 5990, 'Bus', '2024-11-25', 'Ammar', 0),
(5, 11, 39, 5990, 'Bus', '2024-11-26', 'Maria', 1),
(6, 11, 40, 5990, 'Bus', '2024-11-26', 'Maria', 0),
(7, 11, 44, 29999, 'Flight', '2024-11-26', 'Maria', 1),
(8, 11, 42, 12999, 'Train', '2024-11-26', 'Maria', 1),
(9, 11, 43, 27475, 'Flight', '2024-11-26', 'Maria', 1),
(10, 11, 38, 5990, 'Bus', '2024-11-26', 'Maria', 1),
(11, 12, 39, 5990, 'Bus', '2024-11-26', 'Faiq', 1),
(12, 12, 38, 5990, 'Bus', '2024-11-26', 'Faiq', 1),
(13, 12, 44, 29999, 'Flight', '2024-11-26', 'Faiq', 1),
(14, 12, 42, 12999, 'Train', '2024-11-26', 'Faiq', 1),
(15, 12, 41, 10000, 'Train', '2024-11-26', 'Faiq', 1);

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
  `status` varchar(100) NOT NULL,
  `totalSeats` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `travelservice`
--

INSERT INTO `travelservice` (`serviceID`, `serviceProviderID`, `description`, `serviceType`, `arrivalTime`, `departureTime`, `arrivalLocation`, `departureLocation`, `departureDate`, `arrivalDate`, `ticketPrice`, `status`, `totalSeats`) VALUES
(38, 17, 'Transport No. Bus-123 from Hyderabad to Karachi departing on 12-12 at 12:00 and arriving on 13-12 at 13:00. \nTicket Price: PKR 5990. \nBus station: KainatTravels located at Road 12, Sadder.', 'Bus', '13:00', '12:00', 'Karachi', 'Hyderabad', '12-12', '13-12', 5990, 'ONGOING', 2),
(39, 17, 'Transport No. Bus-222 from Hyderabad to Islamabad departing on 12-12 at 5:00 and arriving on 13-12 at 4:00. \nTicket Price: PKR 5990. \nBus station: KainatTravels located at Road 12, Sadder.', 'Bus', '4:00', '5:00', 'Islamabad', 'Hyderabad', '12-12', '13-12', 5990, 'ONGOING', 2),
(40, 17, 'Transport No. Bus-333 from Lahore to Islamabad departing on 12-12 at 5:00 and arriving on 13-12 at 4:00. \nTicket Price: PKR 5990. \nBus station: KainatTravels located at Road 12, Sadder.', 'Bus', '4:00', '5:00', 'Islamabad', 'Lahore', '12-12', '13-12', 5990, 'DONE', 2),
(41, 18, 'Transport No. TR-102 from Quetta to Islamabad departing on 11-12 at 12:00 and arriving on 12-12 at 6:00. \nTicket Price: PKR 10000. \nBus station: Quetta Station located at Road 12, Sadder.', 'Train', '6:00', '12:00', 'Islamabad', 'Quetta', '11-12', '12-12', 10000, 'ONGOING', 1),
(42, 18, 'Transport No. TR-555 from Quetta to Islamabad departing on 11-12 at 12:00 and arriving on 12-12 at 6:00. \nTicket Price: PKR 12999. \nBus station: Quetta Station located at Road 12, Sadder.', 'Train', '6:00', '12:00', 'Islamabad', 'Quetta', '11-12', '12-12', 12999, 'ONGOING', 1),
(43, 19, 'Transport No. FL-A17 from Quetta to Islamabad departing on 11-12 at 12:00 and arriving on 11-12 at 2:00. \nTicket Price: PKR 27475. \nBus station: Quetta Airport located at Sadder.', 'Flight', '2:00', '12:00', 'Islamabad', 'Quetta', '11-12', '11-12', 27475, 'ONGOING', 1),
(44, 19, 'Transport No. FL-225A from Quetta to Islamabad departing on 11-12 at 5:00 and arriving on 11-12 at 8:00. \nTicket Price: PKR 29999. \nBus station: Quetta Airport located at Sadder.', 'Flight', '8:00', '5:00', 'Islamabad', 'Quetta', '11-12', '11-12', 29999, 'ONGOING', 1);

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
  MODIFY `userID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `hotelbooking`
--
ALTER TABLE `hotelbooking`
  MODIFY `bookingID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `hotelservice`
--
ALTER TABLE `hotelservice`
  MODIFY `HotelServiceID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `serviceproviderauth`
--
ALTER TABLE `serviceproviderauth`
  MODIFY `userID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `travelbooking`
--
ALTER TABLE `travelbooking`
  MODIFY `bookingID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=61;

--
-- AUTO_INCREMENT for table `travelservice`
--
ALTER TABLE `travelservice`
  MODIFY `serviceID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=46;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
