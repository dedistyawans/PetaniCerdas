-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 11, 2017 at 09:09 PM
-- Server version: 10.1.26-MariaDB
-- PHP Version: 7.1.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `petanicerdas`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `foto` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id`, `username`, `password`, `nama`, `foto`) VALUES
(1, 'dedistyawan', 'fb1c8188321b642603b2c6d219b33a1d', 'Dedi Styawan', 'admin-dedi.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `foto_penyakit`
--

CREATE TABLE `foto_penyakit` (
  `id` int(11) NOT NULL,
  `id_penyakit` int(11) NOT NULL,
  `nama_file` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `foto_tanah`
--

CREATE TABLE `foto_tanah` (
  `id` int(11) NOT NULL,
  `id_tanah` int(11) NOT NULL,
  `nama_file` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `foto_tanaman`
--

CREATE TABLE `foto_tanaman` (
  `id` int(11) NOT NULL,
  `id_tanaman` int(11) NOT NULL,
  `nama_file` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `penyakit`
--

CREATE TABLE `penyakit` (
  `id` int(11) NOT NULL,
  `id_tanaman` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `deskripsi` text NOT NULL,
  `ciri_ciri` text NOT NULL,
  `cara_menangani` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tanah`
--

CREATE TABLE `tanah` (
  `id` int(11) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `deskripsi` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tanaman`
--

CREATE TABLE `tanaman` (
  `id` int(11) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `umur` int(11) NOT NULL,
  `musim` varchar(30) NOT NULL,
  `ketinggian_min` int(11) NOT NULL,
  `ketinggian_max` int(11) NOT NULL,
  `curah_hujan` varchar(30) NOT NULL,
  `deskripsi` text NOT NULL,
  `rekomendasi_menanam` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tanaman_tanah`
--

CREATE TABLE `tanaman_tanah` (
  `id` int(11) NOT NULL,
  `id_tanaman` int(11) NOT NULL,
  `id_tanah` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- Indexes for table `foto_penyakit`
--
ALTER TABLE `foto_penyakit`
  ADD PRIMARY KEY (`id`),
  ADD KEY `foto_penyakit_ke_penyakit` (`id_penyakit`);

--
-- Indexes for table `foto_tanah`
--
ALTER TABLE `foto_tanah`
  ADD PRIMARY KEY (`id`),
  ADD KEY `foto_tanah_ke_tanah` (`id_tanah`);

--
-- Indexes for table `foto_tanaman`
--
ALTER TABLE `foto_tanaman`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD KEY `foto_tanaman_ke_tanaman` (`id_tanaman`);

--
-- Indexes for table `penyakit`
--
ALTER TABLE `penyakit`
  ADD PRIMARY KEY (`id`),
  ADD KEY `penyakit_ke_tanaman` (`id_tanaman`);

--
-- Indexes for table `tanah`
--
ALTER TABLE `tanah`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- Indexes for table `tanaman`
--
ALTER TABLE `tanaman`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- Indexes for table `tanaman_tanah`
--
ALTER TABLE `tanaman_tanah`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD KEY `tanaman_tanah_ke_tanaman` (`id_tanaman`),
  ADD KEY `tanaman_tanah_ke_tanahan` (`id_tanah`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `foto_penyakit`
--
ALTER TABLE `foto_penyakit`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `foto_tanah`
--
ALTER TABLE `foto_tanah`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=129;

--
-- AUTO_INCREMENT for table `foto_tanaman`
--
ALTER TABLE `foto_tanaman`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=107;

--
-- AUTO_INCREMENT for table `penyakit`
--
ALTER TABLE `penyakit`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `tanah`
--
ALTER TABLE `tanah`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;

--
-- AUTO_INCREMENT for table `tanaman`
--
ALTER TABLE `tanaman`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `tanaman_tanah`
--
ALTER TABLE `tanaman_tanah`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `foto_penyakit`
--
ALTER TABLE `foto_penyakit`
  ADD CONSTRAINT `foto_penyakit_ke_penyakit` FOREIGN KEY (`id_penyakit`) REFERENCES `penyakit` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `foto_tanah`
--
ALTER TABLE `foto_tanah`
  ADD CONSTRAINT `foto_tanah_ke_tanah` FOREIGN KEY (`id_tanah`) REFERENCES `tanah` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `foto_tanaman`
--
ALTER TABLE `foto_tanaman`
  ADD CONSTRAINT `foto_tanaman_ke_tanaman` FOREIGN KEY (`id_tanaman`) REFERENCES `tanaman` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `penyakit`
--
ALTER TABLE `penyakit`
  ADD CONSTRAINT `penyakit_ke_tanaman` FOREIGN KEY (`id_tanaman`) REFERENCES `tanaman` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tanaman_tanah`
--
ALTER TABLE `tanaman_tanah`
  ADD CONSTRAINT `tanaman_tanah_ke_tanahan` FOREIGN KEY (`id_tanah`) REFERENCES `tanah` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tanaman_tanah_ke_tanaman` FOREIGN KEY (`id_tanaman`) REFERENCES `tanaman` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
