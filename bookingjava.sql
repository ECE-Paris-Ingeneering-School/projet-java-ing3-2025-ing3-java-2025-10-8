-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : sam. 12 avr. 2025 à 18:49
-- Version du serveur : 9.1.0
-- Version de PHP : 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `booking`
--

-- --------------------------------------------------------

--
-- Structure de la table `admin`
--

DROP TABLE IF EXISTS `admin`;
CREATE TABLE IF NOT EXISTS `admin` (
  `id_utilisateur` int NOT NULL,
  `role_specifique` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_utilisateur`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `appartement`
--

DROP TABLE IF EXISTS `appartement`;
CREATE TABLE IF NOT EXISTS `appartement` (
  `id_hebergement` int NOT NULL,
  `nombre_pieces` int DEFAULT NULL,
  `petit_dejeuner` tinyint(1) DEFAULT NULL,
  `etage` int DEFAULT NULL,
  PRIMARY KEY (`id_hebergement`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `appartement`
--

INSERT INTO `appartement` (`id_hebergement`, `nombre_pieces`, `petit_dejeuner`, `etage`) VALUES
(2, 2, 1, 3),
(5, 2, 1, 3),
(8, 2, 1, 3),
(11, 2, 1, 3);

-- --------------------------------------------------------

--
-- Structure de la table `avis`
--

DROP TABLE IF EXISTS `avis`;
CREATE TABLE IF NOT EXISTS `avis` (
  `id_avis` int NOT NULL AUTO_INCREMENT,
  `id_utilisateur` int NOT NULL,
  `id_hebergement` int NOT NULL,
  `note` int DEFAULT NULL,
  `commentaire` text,
  `date_avis` date DEFAULT NULL,
  PRIMARY KEY (`id_avis`),
  KEY `id_utilisateur` (`id_utilisateur`),
  KEY `id_hebergement` (`id_hebergement`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `client`
--

DROP TABLE IF EXISTS `client`;
CREATE TABLE IF NOT EXISTS `client` (
  `id_utilisateur` int NOT NULL,
  `type_client` enum('Nouveau','Ancien') NOT NULL,
  PRIMARY KEY (`id_utilisateur`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `hebergement`
--

DROP TABLE IF EXISTS `hebergement`;
CREATE TABLE IF NOT EXISTS `hebergement` (
  `id_hebergement` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) NOT NULL,
  `adresse` varchar(255) NOT NULL,
  `prix_par_nuit` decimal(10,2) DEFAULT NULL,
  `description` text,
  `specification` text,
  PRIMARY KEY (`id_hebergement`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `hebergement`
--

INSERT INTO `hebergement` (`id_hebergement`, `nom`, `adresse`, `prix_par_nuit`, `description`, `specification`) VALUES
(1, 'Hôtel du Soleil', '10 Rue des Palmiers', 155.50, 'Un hôtel chaleureux près de la plage', 'Wi-Fi, Climatisation, Petit-déjeuner'),
(2, 'Appartement Vue Mer', '3 Avenue des Sables', 95.00, 'Appartement cosy avec balcon', 'Cuisine équipée, TV'),
(3, 'La Maison des Bois', '12 Chemin des Arbres', 130.00, 'Maison d\'hôtes au cœur de la forêt', 'Cheminée, Terrasse, Parking'),
(4, 'Hôtel du Soleil', '10 Rue des Palmiers', 155.50, 'Un hôtel chaleureux près de la plage', 'Wi-Fi, Climatisation, Petit-déjeuner'),
(5, 'Appartement Vue Mer', '3 Avenue des Sables', 95.00, 'Appartement cosy avec balcon', 'Cuisine équipée, TV'),
(6, 'La Maison des Bois', '12 Chemin des Arbres', 130.00, 'Maison d\'hôtes au cœur de la forêt', 'Cheminée, Terrasse, Parking'),
(7, 'Hôtel du Soleil', '10 Rue des Palmiers', 155.50, 'Un hôtel chaleureux près de la plage', 'Wi-Fi, Climatisation, Petit-déjeuner'),
(8, 'Appartement Vue Mer', '3 Avenue des Sables', 95.00, 'Appartement cosy avec balcon', 'Cuisine équipée, TV'),
(9, 'La Maison des Bois', '12 Chemin des Arbres', 130.00, 'Maison d\'hôtes au cœur de la forêt', 'Cheminée, Terrasse, Parking'),
(10, 'Hôtel du Soleil', '10 Rue des Palmiers', 155.50, 'Un hôtel chaleureux près de la plage', 'Wi-Fi, Climatisation, Petit-déjeuner'),
(11, 'Appartement Vue Mer', '3 Avenue des Sables', 95.00, 'Appartement cosy avec balcon', 'Cuisine équipée, TV'),
(12, 'La Maison des Bois', '12 Chemin des Arbres', 130.00, 'Maison d\'hôtes au cœur de la forêt', 'Cheminée, Terrasse, Parking');

-- --------------------------------------------------------

--
-- Structure de la table `hotel`
--

DROP TABLE IF EXISTS `hotel`;
CREATE TABLE IF NOT EXISTS `hotel` (
  `id_hebergement` int NOT NULL,
  `nombre_etoiles` int DEFAULT NULL,
  `petit_dejeuner` tinyint(1) DEFAULT NULL,
  `piscine` tinyint(1) DEFAULT NULL,
  `spa` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id_hebergement`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `hotel`
--

INSERT INTO `hotel` (`id_hebergement`, `nombre_etoiles`, `petit_dejeuner`, `piscine`, `spa`) VALUES
(1, 4, 1, 0, 1),
(4, 4, 1, 0, 1),
(7, 4, 1, 0, 1),
(10, 4, 1, 0, 1);

-- --------------------------------------------------------

--
-- Structure de la table `image`
--

DROP TABLE IF EXISTS `image`;
CREATE TABLE IF NOT EXISTS `image` (
  `id_image` int NOT NULL AUTO_INCREMENT,
  `id_hebergement` int NOT NULL,
  `url_image` varchar(500) NOT NULL,
  `description` text,
  PRIMARY KEY (`id_image`),
  KEY `id_hebergement` (`id_hebergement`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `maisonhotes`
--

DROP TABLE IF EXISTS `maisonhotes`;
CREATE TABLE IF NOT EXISTS `maisonhotes` (
  `id_hebergement` int NOT NULL,
  `petit_dejeuner` tinyint(1) DEFAULT NULL,
  `jardin` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id_hebergement`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `maisonhotes`
--

INSERT INTO `maisonhotes` (`id_hebergement`, `petit_dejeuner`, `jardin`) VALUES
(6, 1, 1),
(9, 1, 1),
(12, 1, 1);

-- --------------------------------------------------------

--
-- Structure de la table `offrereduction`
--

DROP TABLE IF EXISTS `offrereduction`;
CREATE TABLE IF NOT EXISTS `offrereduction` (
  `id_offre` int NOT NULL AUTO_INCREMENT,
  `id_utilisateur` int NOT NULL,
  `description` text,
  `pourcentage_reduction` decimal(5,2) DEFAULT NULL,
  `date_debut` date DEFAULT NULL,
  `date_fin` date DEFAULT NULL,
  PRIMARY KEY (`id_offre`),
  KEY `id_utilisateur` (`id_utilisateur`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `paiement`
--

DROP TABLE IF EXISTS `paiement`;
CREATE TABLE IF NOT EXISTS `paiement` (
  `id_paiement` int NOT NULL AUTO_INCREMENT,
  `id_reservation` int NOT NULL,
  `montant` decimal(10,2) DEFAULT NULL,
  `methode_paiement` enum('Carte bancaire','PayPal','Virement') DEFAULT NULL,
  `statut` enum('En attente','Payé','Annulé') DEFAULT 'En attente',
  `date_paiement` date DEFAULT NULL,
  PRIMARY KEY (`id_paiement`),
  KEY `id_reservation` (`id_reservation`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
CREATE TABLE IF NOT EXISTS `reservation` (
  `id_reservation` int NOT NULL AUTO_INCREMENT,
  `id_utilisateur` int NOT NULL,
  `id_hebergement` int NOT NULL,
  `date_arrivee` date NOT NULL,
  `date_depart` date NOT NULL,
  `nombre_adultes` int DEFAULT NULL,
  `nombre_enfants` int DEFAULT NULL,
  `nombre_chambres` int DEFAULT NULL,
  `statut` enum('Confirmée','Annulée','En attente') DEFAULT 'En attente',
  PRIMARY KEY (`id_reservation`),
  KEY `id_utilisateur` (`id_utilisateur`),
  KEY `id_hebergement` (`id_hebergement`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
CREATE TABLE IF NOT EXISTS `utilisateur` (
  `id_utilisateur` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `email` varchar(255) NOT NULL,
  `mdp` varchar(255) NOT NULL,
  `date_inscription` date NOT NULL,
  PRIMARY KEY (`id_utilisateur`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
