-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 20-06-2026 a las 18:46:28
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `gestionplayasdb`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `distrito`
--

CREATE TABLE `distrito` (
  `id_distrito` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `distrito`
--

INSERT INTO `distrito` (`id_distrito`, `nombre`) VALUES
(1, 'Barranco'),
(2, 'Chorrillos'),
(3, 'Miraflores'),
(4, 'Ancon'),
(5, 'Santa Rosa'),
(6, 'Santa Maria del Mar'),
(7, 'Pucusana'),
(8, 'San Bartolo'),
(9, 'Punta Negra'),
(10, 'Punta Hermosa'),
(11, 'Lurin'),
(12, 'Villa El Salvador');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `playa`
--

CREATE TABLE `playa` (
  `id_playa` int(11) NOT NULL,
  `id_distrito` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `latitud` decimal(10,8) NOT NULL,
  `longitud` decimal(11,8) NOT NULL,
  `estado_general` varchar(50) DEFAULT 'Sin reportes recientes'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `playa`
--

INSERT INTO `playa` (`id_playa`, `id_distrito`, `nombre`, `latitud`, `longitud`, `estado_general`) VALUES
(1, 1, 'Los Yuyos', -12.15280000, -77.02670000, 'Sin reportes recientes'),
(2, 1, 'Las Sombrillas', -12.15480000, -77.02550000, 'Sin reportes recientes'),
(3, 2, 'Agua Dulce', -12.16450000, -77.03150000, 'Sin reportes recientes'),
(4, 2, 'La Herradura', -12.18950000, -77.03450000, 'Sin reportes recientes'),
(5, 3, 'Makaha', -12.12650000, -77.03550000, 'Sin reportes recientes'),
(6, 3, 'Waikiki', -12.12350000, -77.03850000, 'Sin reportes recientes'),
(7, 4, 'Los Pocitos', -11.75514300, -77.17127300, 'Sin reportes recientes'),
(8, 4, 'Las Conchitas', -11.75958600, -77.17067000, 'Sin reportes recientes'),
(9, 4, 'Miramar', -11.76511800, -77.17163400, 'Sin reportes recientes'),
(11, 4, 'Hermosa', -11.77427600, -77.18606200, 'Sin reportes recientes'),
(12, 4, 'La Puntilla', -11.77017400, -77.19681800, 'Sin reportes recientes'),
(13, 4, 'Huaquilla', -11.77609100, -77.19547900, 'Sin reportes recientes'),
(14, 4, 'San Francisco Grande', -11.77031300, -77.19084000, 'Sin reportes recientes'),
(15, 4, 'Salitral', -11.78174300, -77.19635700, 'Sin reportes recientes'),
(16, 5, 'Playa Grande', -11.79332900, -77.18271700, 'Sin reportes recientes'),
(17, 5, 'Hondable', -11.81422100, -77.17643100, 'Sin reportes recientes'),
(18, 5, 'Los Corales', -11.79459500, -77.17698100, 'Sin reportes recientes'),
(19, 6, 'Santa Maria', -12.40132400, -76.77728100, 'Sin reportes recientes'),
(20, 6, 'Embajadores', -12.41157600, -76.77746400, 'Sin reportes recientes'),
(21, 7, 'Pucusana', -12.48058300, -76.79900700, 'Sin reportes recientes'),
(22, 7, 'Naplo', -12.47946800, -76.79372000, 'Sin reportes recientes'),
(23, 8, 'San Bartolo Norte Las Sombrillas', -12.38536800, -76.78411600, 'Sin reportes recientes'),
(24, 8, 'San Bartolo Sur', -12.39138800, -76.77899700, 'Sin reportes recientes'),
(25, 9, 'Punta Negra', -12.36093900, -76.80000600, 'Sin reportes recientes'),
(26, 9, 'Santa Rosa Baja', -12.36408600, -76.79847300, 'Sin reportes recientes'),
(27, 9, 'Punta Rocas', -12.35464800, -76.80998100, 'Sin reportes recientes');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `recuperacion_clave`
--

CREATE TABLE `recuperacion_clave` (
  `id_recuperacion` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `token` varchar(255) NOT NULL,
  `fecha_expiracion` datetime NOT NULL,
  `usado` tinyint(1) DEFAULT 0,
  `fecha_creacion` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reporte_resena`
--

CREATE TABLE `reporte_resena` (
  `id_reporte` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `id_playa` int(11) NOT NULL,
  `calificacion_estrellas` int(11) DEFAULT NULL CHECK (`calificacion_estrellas` between 1 and 5),
  `nivel_limpieza` enum('Limpia','Contaminada','Llena de residuos sólidos') NOT NULL,
  `nivel_afluencia` enum('Vacía','Aforo moderado','Saturada') NOT NULL,
  `clima` enum('Soleado','Nublado','Lluvioso') NOT NULL,
  `seguridad` enum('Segura','Precaución','Insegura') NOT NULL,
  `comentario` text DEFAULT NULL,
  `foto_url` varchar(255) DEFAULT NULL,
  `fecha_reporte` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `reporte_resena`
--

INSERT INTO `reporte_resena` (`id_reporte`, `id_usuario`, `id_playa`, `calificacion_estrellas`, `nivel_limpieza`, `nivel_afluencia`, `clima`, `seguridad`, `comentario`, `foto_url`, `fecha_reporte`) VALUES
(6, 1, 2, 4, 'Limpia', 'Vacía', 'Soleado', 'Segura', 'dasdasdasd', 'playa_1781946436.jpg', '2026-06-20 04:07:16'),
(7, 1, 1, 4, 'Limpia', 'Vacía', 'Soleado', 'Segura', 'dasdsadas', 'playa_1781947356.jpg', '2026-06-20 04:22:36'),
(8, 1, 1, 4, 'Limpia', 'Vacía', 'Soleado', 'Segura', 'Sin duda alguna la mejor de las playas de l peru', 'playa_1781963800.jpg', '2026-06-20 08:56:40'),
(9, 1, 3, 4, 'Limpia', 'Vacía', 'Soleado', 'Segura', 'Unas ricas chelitas en todo el sol, super recomendada', 'playa_1781966940.jpg', '2026-06-20 09:49:00'),
(10, 1, 3, 1, 'Contaminada', 'Saturada', 'Nublado', 'Precaución', 'No recomendado plkaya en estado crtico', 'playa_1781967391.jpg', '2026-06-20 09:56:31'),
(11, 1, 6, 3, 'Limpia', 'Aforo moderado', 'Nublado', 'Segura', 'Se puede mejorar la getion de plauas', 'playa_1781967505.jpg', '2026-06-20 09:58:25');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id_usuario` int(11) NOT NULL,
  `nombre_completo` varchar(150) NOT NULL,
  `correo_electronico` varchar(150) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `fecha_registro` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id_usuario`, `nombre_completo`, `correo_electronico`, `contrasena`, `fecha_registro`) VALUES
(1, 'Usuario Demo', 'demo@playas.com', '1', '2026-06-20 02:59:52');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `distrito`
--
ALTER TABLE `distrito`
  ADD PRIMARY KEY (`id_distrito`);

--
-- Indices de la tabla `playa`
--
ALTER TABLE `playa`
  ADD PRIMARY KEY (`id_playa`),
  ADD KEY `id_distrito` (`id_distrito`);

--
-- Indices de la tabla `recuperacion_clave`
--
ALTER TABLE `recuperacion_clave`
  ADD PRIMARY KEY (`id_recuperacion`),
  ADD KEY `id_usuario` (`id_usuario`);

--
-- Indices de la tabla `reporte_resena`
--
ALTER TABLE `reporte_resena`
  ADD PRIMARY KEY (`id_reporte`),
  ADD KEY `id_usuario` (`id_usuario`),
  ADD KEY `id_playa` (`id_playa`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `correo_electronico` (`correo_electronico`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `distrito`
--
ALTER TABLE `distrito`
  MODIFY `id_distrito` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de la tabla `playa`
--
ALTER TABLE `playa`
  MODIFY `id_playa` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT de la tabla `recuperacion_clave`
--
ALTER TABLE `recuperacion_clave`
  MODIFY `id_recuperacion` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `reporte_resena`
--
ALTER TABLE `reporte_resena`
  MODIFY `id_reporte` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `playa`
--
ALTER TABLE `playa`
  ADD CONSTRAINT `playa_ibfk_1` FOREIGN KEY (`id_distrito`) REFERENCES `distrito` (`id_distrito`) ON DELETE CASCADE;

--
-- Filtros para la tabla `recuperacion_clave`
--
ALTER TABLE `recuperacion_clave`
  ADD CONSTRAINT `recuperacion_clave_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE CASCADE;

--
-- Filtros para la tabla `reporte_resena`
--
ALTER TABLE `reporte_resena`
  ADD CONSTRAINT `reporte_resena_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE CASCADE,
  ADD CONSTRAINT `reporte_resena_ibfk_2` FOREIGN KEY (`id_playa`) REFERENCES `playa` (`id_playa`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
