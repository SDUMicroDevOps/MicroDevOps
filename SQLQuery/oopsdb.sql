-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: db
-- Generation Time: Dec 19, 2022 at 11:36 AM
-- Server version: 8.0.31
-- PHP Version: 8.0.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `oops_test`
--

-- --------------------------------------------------------

--
-- Table structure for table `privilage`
--

CREATE TABLE `privilage` (
  `id` int NOT NULL,
  `role_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `privilage`
--

INSERT INTO `privilage` (`id`, `role_name`) VALUES
(1, 'user'),
(2, 'admin'),
(3, 'developer');

-- --------------------------------------------------------

--
-- Table structure for table `solution`
--

CREATE TABLE `solution` (
  `task_id` varchar(255) NOT NULL,
  `user` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `date` date NOT NULL,
  `is_optimal` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `solver`
--

CREATE TABLE `solver` (
  `id` int NOT NULL,
  `solver_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `solver`
--

INSERT INTO `solver` (`id`, `solver_name`) VALUES
(1, 'chuffed'),
(2, 'gecode');

-- --------------------------------------------------------

--
-- Table structure for table `task_queue`
--

CREATE TABLE `task_queue` (
  `user` varchar(255) NOT NULL,
  `solver` int NOT NULL,
  `task_id` varchar(255) NOT NULL,
  `solver_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `timeout` int NOT NULL,
  `vCPU` int NOT NULL,
  `max_memory` int NOT NULL,
  `mzn` text NOT NULL,
  `dzn` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `username` varchar(255) NOT NULL,
  `pwd` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `privilage_id` int NOT NULL,
  `vCPU_limit` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `privilage`
--
ALTER TABLE `privilage`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `solution`
--
ALTER TABLE `solution`
  ADD PRIMARY KEY (`task_id`),
  ADD KEY `user` (`user`);

--
-- Indexes for table `solver`
--
ALTER TABLE `solver`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `task_queue`
--
ALTER TABLE `task_queue`
  ADD KEY `user` (`user`),
  ADD KEY `solver` (`solver`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`username`),
  ADD KEY `privilage_id` (`privilage_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `privilage`
--
ALTER TABLE `privilage`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `solver`
--
ALTER TABLE `solver`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `solution`
--
ALTER TABLE `solution`
  ADD CONSTRAINT `solution_ibfk_1` FOREIGN KEY (`user`) REFERENCES `user` (`username`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Constraints for table `task_queue`
--
ALTER TABLE `task_queue`
  ADD CONSTRAINT `task_queue_ibfk_1` FOREIGN KEY (`user`) REFERENCES `user` (`username`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `task_queue_ibfk_2` FOREIGN KEY (`solver`) REFERENCES `solver` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`privilage_id`) REFERENCES `privilage` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
