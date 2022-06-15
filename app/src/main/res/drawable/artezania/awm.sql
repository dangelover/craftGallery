-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3308
-- Tiempo de generación: 21-02-2022 a las 21:48:32
-- Versión del servidor: 5.7.36
-- Versión de PHP: 7.4.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `awm`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `alumnos`
--

DROP TABLE IF EXISTS `alumnos`;
CREATE TABLE IF NOT EXISTS `alumnos` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellido_paterno` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellido_materno` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre_madre` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre_padre` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `direccion_Padre` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `direccion_madre` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `correo` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `direccion` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `Distrito` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `Provincia` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `Departamento` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `DNI` int(11) NOT NULL,
  `celular` int(11) NOT NULL,
  `grado_id` bigint(20) UNSIGNED NOT NULL,
  `seccion_id` bigint(20) UNSIGNED NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `alumnos_grado_id_foreign` (`grado_id`),
  KEY `alumnos_seccion_id_foreign` (`seccion_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `alumnos`
--

INSERT INTO `alumnos` (`id`, `nombre`, `apellido_paterno`, `apellido_materno`, `nombre_madre`, `nombre_padre`, `direccion_Padre`, `direccion_madre`, `correo`, `direccion`, `Distrito`, `Provincia`, `Departamento`, `DNI`, `celular`, `grado_id`, `seccion_id`, `created_at`, `updated_at`) VALUES
(1, 'Ricardo Eliasar', 'Mendoza', 'Sanabria', 'Rosa Bartra de la Cruz', 'Alan Garcia Perez', 'Jr. Los retoños 456', 'Jr. Los retoños 456', '74126358@RICARDO.com', 'Jr. Los retoños 456', 'El Tambo', 'Huancayo', 'Junin', 74126358, 952634178, 1, 1, '2022-02-21 20:39:34', '2022-02-21 22:16:58'),
(2, 'Luis', 'Cajincho', 'Montoya', 'Paulina Montoya Sanabria', 'Edgar Cajincho Diaz', 'Jr. 24 de Mayo 478', 'Jr. 24 de Mayo', '71526339@LUIS.com', 'Jr. 24 de Mayo', 'El Tambo', 'Huancayo', 'Junin', 71526339, 952368574, 2, 1, '2022-02-21 20:52:32', '2022-02-21 20:52:32');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `documentos`
--

DROP TABLE IF EXISTS `documentos`;
CREATE TABLE IF NOT EXISTS `documentos` (
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `archivo` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` bigint(20) UNSIGNED NOT NULL,
  `prosesos_id` bigint(20) UNSIGNED NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  KEY `documentos_user_id_foreign` (`user_id`),
  KEY `documentos_prosesos_id_foreign` (`prosesos_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `grados`
--

DROP TABLE IF EXISTS `grados`;
CREATE TABLE IF NOT EXISTS `grados` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `numero_grado` int(11) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `grados`
--

INSERT INTO `grados` (`id`, `numero_grado`, `created_at`, `updated_at`) VALUES
(1, 1, NULL, NULL),
(2, 2, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `migrations`
--

DROP TABLE IF EXISTS `migrations`;
CREATE TABLE IF NOT EXISTS `migrations` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `migration` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `batch` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `migrations`
--

INSERT INTO `migrations` (`id`, `migration`, `batch`) VALUES
(1, '2014_10_12_000000_create_users_table', 1),
(2, '2014_10_12_100000_create_password_resets_table', 1),
(3, '2020_07_04_134810_create_productos_table', 1),
(4, '2020_07_08_165720_create_usuarios_table', 1),
(5, '2020_07_20_002358_create_notas_table', 1),
(6, '2020_07_20_024649_create_rol_tables', 1),
(7, '2020_07_20_045351_create_procesos_tables', 1),
(8, '2020_07_21_151336_create_procedures_tables', 1),
(9, '2020_07_21_152030_create_prosesos_tables', 1),
(10, '2020_07_21_185437_add_image_users_table', 1),
(11, '2020_07_21_204626_create_documentos_tables', 1),
(12, '2020_07_21_211100_add_archivo_documentos_table', 1),
(13, '2020_07_27_041204_create_registers_table', 1),
(14, '2022_02_19_020846_create_grado_table', 1),
(15, '2022_02_19_020853_create_seccion_table', 1),
(16, '2022_02_19_022533_create_profesores_table', 1),
(17, '2022_02_19_022641_create_alumnos_table', 1),
(18, '2022_02_19_043634_renombrando_grados_table', 1),
(19, '2022_02_19_044021_renombrando_seccions_table', 1),
(20, '2022_02_19_050527_renombrando_profesors_table', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `notas`
--

DROP TABLE IF EXISTS `notas`;
CREATE TABLE IF NOT EXISTS `notas` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `titulo` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `texto` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `password_resets`
--

DROP TABLE IF EXISTS `password_resets`;
CREATE TABLE IF NOT EXISTS `password_resets` (
  `email` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  KEY `password_resets_email_index` (`email`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `procedures`
--

DROP TABLE IF EXISTS `procedures`;
CREATE TABLE IF NOT EXISTS `procedures` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `prosesos_id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `procedures_user_id_foreign` (`user_id`),
  KEY `procedures_prosesos_id_foreign` (`prosesos_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `procesos`
--

DROP TABLE IF EXISTS `procesos`;
CREATE TABLE IF NOT EXISTS `procesos` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proceso_user`
--

DROP TABLE IF EXISTS `proceso_user`;
CREATE TABLE IF NOT EXISTS `proceso_user` (
  `user_id` bigint(20) UNSIGNED NOT NULL,
  `proceso_id` bigint(20) UNSIGNED NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  KEY `proceso_user_user_id_foreign` (`user_id`),
  KEY `proceso_user_proceso_id_foreign` (`proceso_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

DROP TABLE IF EXISTS `productos`;
CREATE TABLE IF NOT EXISTS `productos` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `NombreArticulo` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `Precio` int(11) NOT NULL,
  `Fecha` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `PaisOrigen` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `profesors`
--

DROP TABLE IF EXISTS `profesors`;
CREATE TABLE IF NOT EXISTS `profesors` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellido_paterno` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellido_materno` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `correo` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `direccion` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `Distrito` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `Provincia` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `Departamento` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `DNI` int(11) NOT NULL,
  `celular` int(11) NOT NULL,
  `grado_id` bigint(20) UNSIGNED NOT NULL,
  `seccion_id` bigint(20) UNSIGNED NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `profesores_grado_id_foreign` (`grado_id`),
  KEY `profesores_seccion_id_foreign` (`seccion_id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `profesors`
--

INSERT INTO `profesors` (`id`, `nombre`, `apellido_paterno`, `apellido_materno`, `correo`, `direccion`, `Distrito`, `Provincia`, `Departamento`, `DNI`, `celular`, `grado_id`, `seccion_id`, `created_at`, `updated_at`) VALUES
(1, 'Luisa', 'Campos', 'Aranda', 'Luisas_12@hotmail.com', 'Jr. 24 de Mayo', 'El Tambo', 'Huancayo', 'Junin', 19851395, 910049151, 1, 2, '2022-02-20 23:11:46', '2022-02-21 04:59:54'),
(2, 'Rodolfo', 'Salome', 'Ramirez', 'Rodolfo@gmail.com', 'Jr. Amautas 452', 'El Tambo', 'Huancayo', 'Junin', 85967474, 952635252, 1, 1, '2022-02-20 23:18:02', '2022-02-20 23:18:02'),
(3, 'Bertha', 'Oscanoa', 'Diaz', 'Berta_oscanoa@gmail.com', 'Jr. 24 de junio', 'El Tambo', 'Huancayo', 'Junin', 72286996, 952417485, 2, 2, '2022-02-21 05:22:50', '2022-02-21 05:22:50'),
(4, 'Silvia', 'Lopez', 'Obrador', 'Silvia@gmail.com', 'Jr. Las violestas', 'El Tambo', 'Huancayo', 'Junin', 74859663, 965854174, 1, 2, '2022-02-21 05:27:44', '2022-02-21 05:27:44'),
(6, 'Mario', 'Riveros', 'Huamani', '74859663@MARIO.com', 'Jr. San Jacinto 152', 'El Tambo', 'Huancayo', 'Junin', 74859663, 952418596, 2, 2, '2022-02-21 20:31:17', '2022-02-21 20:31:17');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `prosesos`
--

DROP TABLE IF EXISTS `prosesos`;
CREATE TABLE IF NOT EXISTS `prosesos` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint(20) UNSIGNED NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `prosesos_user_id_foreign` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `registers`
--

DROP TABLE IF EXISTS `registers`;
CREATE TABLE IF NOT EXISTS `registers` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `procedure_id` bigint(20) UNSIGNED NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `registers_procedure_id_foreign` (`procedure_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

DROP TABLE IF EXISTS `roles`;
CREATE TABLE IF NOT EXISTS `roles` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `label` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id`, `name`, `label`, `created_at`, `updated_at`) VALUES
(1, 'administrador', NULL, '2022-02-20 23:13:34', '2022-02-20 23:13:34');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `role_user`
--

DROP TABLE IF EXISTS `role_user`;
CREATE TABLE IF NOT EXISTS `role_user` (
  `user_id` bigint(20) UNSIGNED NOT NULL,
  `role_id` bigint(20) UNSIGNED NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  KEY `role_user_user_id_foreign` (`user_id`),
  KEY `role_user_role_id_foreign` (`role_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `role_user`
--

INSERT INTO `role_user` (`user_id`, `role_id`, `created_at`, `updated_at`) VALUES
(1, 1, '2022-02-20 23:13:40', '2022-02-20 23:13:40');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `seccions`
--

DROP TABLE IF EXISTS `seccions`;
CREATE TABLE IF NOT EXISTS `seccions` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `letra_seccion` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `seccions`
--

INSERT INTO `seccions` (`id`, `letra_seccion`, `created_at`, `updated_at`) VALUES
(1, 'A', NULL, NULL),
(2, 'B', NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email_verified_at` timestamp NULL DEFAULT NULL,
  `imagen` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `remember_token` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `users_email_unique` (`email`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `email_verified_at`, `imagen`, `password`, `remember_token`, `created_at`, `updated_at`) VALUES
(1, 'kevin', 'kevincastrocampos@gmail.com', NULL, NULL, '$2y$10$da39z24Z5.8.Rn2deRt61ORGvvO1zKbCcAE3AZ66I64pE5nLyPkwK', NULL, '2022-02-20 23:12:35', '2022-02-20 23:12:35');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE IF NOT EXISTS `usuarios` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `direccion` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ciudad` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
