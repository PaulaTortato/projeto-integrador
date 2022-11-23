-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 10-Nov-2022 às 15:46
-- Versão do servidor: 10.4.20-MariaDB
-- versão do PHP: 7.3.29

SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `projeto_integrador`
--

--
-- Extraindo dados da tabela `buyer`
--

insert into `buyer` (`id`) values(1);
insert into `buyer` (`id`) values(2);
insert into `buyer` (`id`) values(3);
insert into `buyer` (`id`) values(4);
insert into `buyer` (`id`) values(5);
insert into `buyer` (`id`) values(6);
insert into `buyer` (`id`) values(7);
insert into `buyer` (`id`) values(8);
insert into `buyer` (`id`) values(9);
insert into `buyer` (`id`) values(10);

--
-- Extraindo dados da tabela `seller`
--

insert into `seller` (`id`, `first_name`, `last_name`, `email`, `address`, `house_number`, `zip_code`, `active`) values(1, 'Arthur', 'da Silva', 'arthur.dsilva@email.com', 'Rua Adalberto Carvalho de Araújo', 382, '81070250', 'ATIVO');
insert into `seller` (`id`, `first_name`, `last_name`, `email`, `address`, `house_number`, `zip_code`, `active`) values(2, 'Ana', 'Almeida', 'ana.almeida@email.com', 'Rua Tenente Tavares', 39, '78040085', 'ATIVO');
insert into `seller` (`id`, `first_name`, `last_name`, `email`, `address`, `house_number`, `zip_code`, `active`) values(3, 'Felipe', 'Antunes', 'felipe.antunes@email.com', 'Travessa Joaquim Viana', 282, '64600439', 'ATIVO');
insert into `seller` (`id`, `first_name`, `last_name`, `email`, `address`, `house_number`, `zip_code`, `active`) values(4, 'Fernanda', 'dos Santos', 'fernanda.dsantos@email.com', 'Rua Iraci Almeida da Costa', 439, '58073066', 'ATIVO');
insert into `seller` (`id`, `first_name`, `last_name`, `email`, `address`, `house_number`, `zip_code`, `active`) values(5, 'Gabriel', 'Cortez', 'gabriel.cortez@email.com', 'Rua Sílvia', 1045, '01331903', 'ATIVO');
insert into `seller` (`id`, `first_name`, `last_name`, `email`, `address`, `house_number`, `zip_code`, `active`) values(6, 'Gisele', 'Oliveira', 'gisele.oliveira@email.com', 'Quadra 303 Norte Alameda 14', 123, '77001230', 'ATIVO');
insert into `seller` (`id`, `first_name`, `last_name`, `email`, `address`, `house_number`, `zip_code`, `active`) values(7, 'João Carlos', 'da Silveira', 'jc.dsilveira@email.com', 'Rua Manoel Cristiano Bussinger', 90, '28613730', 'ATIVO');
insert into `seller` (`id`, `first_name`, `last_name`, `email`, `address`, `house_number`, `zip_code`, `active`) values(8, 'Juliana', 'Fonseca', 'juliana.fonseca@email.com', 'Rua da Rolinha', 768, '56326170', 'ATIVO');
insert into `seller` (`id`, `first_name`, `last_name`, `email`, `address`, `house_number`, `zip_code`, `active`) values(9, 'Marcos', 'Gularte', 'marcos.gularte@email.com', 'Rua Açores', 1904, '79094480', 'ATIVO');
insert into `seller` (`id`, `first_name`, `last_name`, `email`, `address`, `house_number`, `zip_code`, `active`) values(10, 'Monique', 'Silva Jardim', 'monique.silvajardim@email.com', 'Rua Araguaia', 1283, '64060380', 'ATIVO');

--
-- Extraindo dados da tabela `warehouse_operator`
--

insert into `warehouse_operator` (`id`) values(1);
insert into `warehouse_operator` (`id`) values(2);
insert into `warehouse_operator` (`id`) values(3);
insert into `warehouse_operator` (`id`) values(4);
insert into `warehouse_operator` (`id`) values(5);
insert into `warehouse_operator` (`id`) values(6);
insert into `warehouse_operator` (`id`) values(7);
insert into `warehouse_operator` (`id`) values(8);
insert into `warehouse_operator` (`id`) values(9);
insert into `warehouse_operator` (`id`) values(10);

--
-- Extraindo dados da tabela `warehouse`
--

insert into `warehouse` (`id`, `warehouse_operator_id`) values(1, 10);
insert into `warehouse` (`id`, `warehouse_operator_id`) values(2, 9);
insert into `warehouse` (`id`, `warehouse_operator_id`) values(3, 8);
insert into `warehouse` (`id`, `warehouse_operator_id`) values(4, 7);
insert into `warehouse` (`id`, `warehouse_operator_id`) values(5, 6);
insert into `warehouse` (`id`, `warehouse_operator_id`) values(6, 5);
insert into `warehouse` (`id`, `warehouse_operator_id`) values(7, 4);
insert into `warehouse` (`id`, `warehouse_operator_id`) values(8, 3);
insert into `warehouse` (`id`, `warehouse_operator_id`) values(9, 2);
insert into `warehouse` (`id`, `warehouse_operator_id`) values(10, 1);

--
-- Extraindo dados da tabela `product`
--

insert into `product` (`id`, `price`, `category`, `seller_id`) values(1, '1.75', 'CONGELADO', 1);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(2, '2.30', 'REFRIGERADO', 1);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(3, '3.90', 'REFRIGERADO', 1);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(4, '4.10', 'FRESCO', 2);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(5, '5.50', 'REFRIGERADO', 2);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(6, '6.10', 'CONGELADO', 3);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(7, '7.99', 'FRESCO', 3);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(8, '8.45', 'FRESCO', 1);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(9, '9.12', 'CONGELADO', 1);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(10, '10.20', 'REFRIGERADO', 3);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(11, '11.25', 'FRESCO', 4);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(12, '12.44', 'REFRIGERADO', 5);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(13, '13.00', 'CONGELADO', 4);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(14, '14.20', 'FRESCO', 5);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(15, '15.60', 'REFRIGERADO', 6);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(16, '6.23', 'FRESCO', 6);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(17, '7.10', 'REFRIGERADO', 6);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(18, '8.25', 'CONGELADO', 8);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(19, '9.20', 'FRESCO', 8);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(20, '2.00', 'CONGELADO', 9);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(21, '2.10', 'FRESCO', 9);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(22, '7.20', 'CONGELADO', 2);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(23, '3.30', 'FRESCO', 10);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(24, '4.00', 'CONGELADO', 10);
insert into `product` (`id`, `price`, `category`, `seller_id`) values(25, '14.20', 'FRESCO', 10);

--
-- Extraindo dados da tabela `section`
--

insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(1, 'FRESCO', 6100, 1);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(2, 'CONGELADO', 4100, 1);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(3, 'REFRIGERADO', 7100, 1);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(4, 'FRESCO', 6200, 2);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(5, 'CONGELADO', 4200, 2);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(6, 'REFRIGERADO', 7300, 3);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(7, 'FRESCO', 3000, 3);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(8, 'FRESCO', 2300, 4);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(9, 'CONGELADO', 1000, 4);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(10, 'REFRIGERADO', 5500, 4);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(11, 'FRESCO', 6200, 5);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(12, 'CONGELADO', 4200, 6);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(13, 'REFRIGERADO', 7300, 6);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(14, 'FRESCO', 7800, 7);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(15, 'FRESCO', 1800, 8);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(16, 'CONGELADO', 2000, 8);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(17, 'REFRIGERADO', 2300, 9);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(18, 'FRESCO', 4100, 10);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(19, 'CONGELADO', 5400, 10);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(20, 'REFRIGERADO', 1900, 10);

--
-- Extraindo dados da tabela `inbound_order`
--

insert into `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) values(1, '2022-11-08', 1, 1, 10);
insert into `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) values(2, '2022-11-08', 1, 1, 10);
insert into `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) values(3, '2022-11-08', 2, 1, 10);
insert into `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) values(4, '2022-11-08', 4, 2, 9);
insert into `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) values(5, '2022-11-08', 6, 3, 8);
insert into `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) values(6, '2022-11-08', 10, 4, 7);
insert into `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) values(7, '2022-11-08', 11, 5, 6);
insert into `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) values(8, '2022-11-08', 12, 6, 5);
insert into `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) values(9, '2022-11-08', 14, 7, 4);
insert into `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) values(10, '2022-11-08', 17, 9, 2);

--
-- Extraindo dados da tabela `item_batch`
--

insert into `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `category`, `volume`, `inbound_order_id`, `product_id`) values(1, '2023-01-11', '2022-11-08', '2022-11-08 22:27:42', '30.20', 5, 'CONGELADO', 3, 1, 1);
insert into `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `category`, `volume`, `inbound_order_id`, `product_id`) values(2, '2022-12-12', '2022-11-09', '2022-11-09 22:27:42', '55.20', 13, 'CONGELADO', 3, 1, 1);
insert into `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `category`, `volume`, `inbound_order_id`, `product_id`) values(3, '2023-01-13', '2022-11-10', '2022-11-10 22:27:42', '45.20', 7, 'CONGELADO', 3, 1, 1);
insert into `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `category`, `volume`, `inbound_order_id`, `product_id`) values(4, '2022-12-10', '2022-11-10', '2022-11-10 21:25:00', '50.00', 20, 'FRESCO', 21, 2, 4);
insert into `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `category`, `volume`, `inbound_order_id`, `product_id`) values(5, '2022-12-30', '2022-11-12', '2022-11-12 22:27:42', '75.20', 9, 'FRESCO', 18, 2, 4);
insert into `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `category`, `volume`, `inbound_order_id`, `product_id`) values(6, '2022-12-13', '2022-11-12', '2022-11-12 22:27:42', '75.20', 6, 'FRESCO', 12, 2, 4);
insert into `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `category`, `volume`, `inbound_order_id`, `product_id`) values(7, '2022-12-18', '2022-11-10', '2022-11-10 22:27:42', '45.20', 5, 'CONGELADO', 5, 1, 6);
insert into `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `category`, `volume`, `inbound_order_id`, `product_id`) values(8, '2022-12-30', '2022-11-10', '2022-11-10 22:27:42', '85.20', 20, 'CONGELADO', 48, 3, 1);
insert into `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `category`, `volume`, `inbound_order_id`, `product_id`) values(9, '2022-12-30', '2022-11-10', '2022-11-10 22:27:42', '5.20', 6, 'CONGELADO', 5, 3, 6);
insert into `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `category`, `volume`, `inbound_order_id`, `product_id`) values(10, '2022-11-20', '2022-11-10', '2022-11-10 22:27:42', '521.20', 15, 'CONGELADO', 5, 3, 6);

--
-- Extraindo dados da tabela `order_purchase`
--
insert into `order_purchase` (`id`, `date_order`, `status`, `buyer_id`) values(1, '2022-11-10', 0, 1);
insert into `order_purchase` (`id`, `date_order`, `status`, `buyer_id`) values(2, '2022-11-10', 0, 2);

--
-- Extraindo dados da tabela `product_order`
--
insert into `product_order` (`id`, `product_id`, `quantity`, `order_purchase_id`) values(1, 1, 2, 1);
insert into `product_order` (`id`, `product_id`, `quantity`, `order_purchase_id`) values(2, 1, 1, 2);

SET FOREIGN_KEY_CHECKS=1;
commit;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;