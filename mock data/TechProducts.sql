INSERT INTO category (name)
VALUES
('Laptop'),
('Mobile Phone'),
('TV')
ON CONFLICT (name) DO NOTHING;

INSERT INTO attribute (name, value_type, category_id)
SELECT s.name, s.value_type, c.id
FROM (
	VALUES
	('Laptop', 'Laptop Brand', 'STRING'),
	('Laptop', 'Laptop CPU', 'STRING'),
	('Laptop', 'Laptop GPU', 'STRING'),
	('Laptop', 'Laptop Screen Size', 'NUMBER'),
	('Laptop', 'Laptop RAM', 'NUMBER'),
	('Laptop', 'Laptop Storage', 'NUMBER'),
	('Mobile Phone', 'Mobile Phone Brand', 'STRING'),
	('Mobile Phone', 'Mobile Phone Screen Size', 'NUMBER'),
	('Mobile Phone', 'Mobile Phone RAM', 'NUMBER'),
	('Mobile Phone', 'Mobile Phone Storage', 'NUMBER'),
	('Mobile Phone', 'Mobile Phone CPU', 'STRING'),
	('TV', 'TV Brand', 'STRING'),
	('TV', 'TV Screen Size', 'NUMBER'),
	('TV', 'TV Resolution', 'STRING'),
	('TV', 'TV Smart TV', 'BOOLEAN'),
	('TV', 'TV Refresh Rate', 'NUMBER')
) AS s(category_name, name, value_type)
JOIN category c ON c.name = s.category_name
WHERE NOT EXISTS (
	SELECT 1
	FROM attribute a
	WHERE a.name = s.name
	  AND a.category_id = c.id
);

INSERT INTO product (name, short_description, price, category_id)
VALUES
('NebulaBook A1', 'Lightweight 14-inch everyday laptop.', 899.99, (SELECT id FROM category WHERE name = 'Laptop')),
('VoltEdge Pro 15', 'Performance laptop for creators.', 1499.00, (SELECT id FROM category WHERE name = 'Laptop')),
('PixelForge X13', 'Compact ultrabook with long battery life.', 1099.50, (SELECT id FROM category WHERE name = 'Laptop')),
('AeroSlate M16', '16-inch productivity laptop.', 1299.90, (SELECT id FROM category WHERE name = 'Laptop')),
('QuantumLeaf 14', 'Balanced laptop for work and study.', 999.00, (SELECT id FROM category WHERE name = 'Laptop')),
('TitanCore G17', 'Gaming-focused laptop with strong cooling.', 1799.00, (SELECT id FROM category WHERE name = 'Laptop')),
('EchoBook Air 13', 'Silent fanless laptop for travel.', 849.00, (SELECT id FROM category WHERE name = 'Laptop')),
('NovaFrame Studio', 'Creator laptop with vivid display.', 1599.99, (SELECT id FROM category WHERE name = 'Laptop')),
('OrbitNote Plus', 'Reliable business laptop.', 1199.00, (SELECT id FROM category WHERE name = 'Laptop')),
('ZenCircuit Lite', 'Budget laptop for everyday tasks.', 649.00, (SELECT id FROM category WHERE name = 'Laptop')),
('SkyPhone One', 'Slim smartphone with all-day battery.', 699.00, (SELECT id FROM category WHERE name = 'Mobile Phone')),
('PulseMobile X', 'Flagship phone with fast processor.', 999.99, (SELECT id FROM category WHERE name = 'Mobile Phone')),
('LumaPhone Mini', 'Compact phone with bright display.', 549.00, (SELECT id FROM category WHERE name = 'Mobile Phone')),
('TerraTalk Pro', 'Durable phone with premium camera.', 899.00, (SELECT id FROM category WHERE name = 'Mobile Phone')),
('AstraCell 5G', '5G-ready phone with smooth performance.', 799.00, (SELECT id FROM category WHERE name = 'Mobile Phone')),
('Nimbus S', 'Midrange phone with clean software.', 599.99, (SELECT id FROM category WHERE name = 'Mobile Phone')),
('VoxPhone Ultra', 'Large-screen phone for media lovers.', 949.00, (SELECT id FROM category WHERE name = 'Mobile Phone')),
('EchoMobile Neo', 'Affordable 5G smartphone.', 449.00, (SELECT id FROM category WHERE name = 'Mobile Phone')),
('Photon Pocket', 'Entry smartphone with modern design.', 329.00, (SELECT id FROM category WHERE name = 'Mobile Phone')),
('ZenPhone Prime', 'Balanced phone with wireless charging.', 749.00, (SELECT id FROM category WHERE name = 'Mobile Phone')),
('VisionView 43', '43-inch Full HD smart television.', 399.00, (SELECT id FROM category WHERE name = 'TV')),
('CineWave 50', '50-inch 4K TV with HDR.', 549.99, (SELECT id FROM category WHERE name = 'TV')),
('AuroraScreen 55', '55-inch 4K smart TV for streaming.', 699.00, (SELECT id FROM category WHERE name = 'TV')),
('PrismPanel 65', '65-inch 4K TV with vivid colors.', 999.00, (SELECT id FROM category WHERE name = 'TV')),
('SoundFrame 58', '58-inch TV with enhanced speakers.', 749.00, (SELECT id FROM category WHERE name = 'TV')),
('NimbusVision 70', '70-inch large format smart TV.', 1299.00, (SELECT id FROM category WHERE name = 'TV')),
('PixelTheater 75', '75-inch premium home cinema TV.', 1699.00, (SELECT id FROM category WHERE name = 'TV')),
('LumaCast 48', '48-inch compact 4K TV.', 479.00, (SELECT id FROM category WHERE name = 'TV')),
('NovaDisplay 60', '60-inch smart TV with voice control.', 849.00, (SELECT id FROM category WHERE name = 'TV')),
('ZenView OLED 55', '55-inch OLED TV with deep contrast.', 1499.00, (SELECT id FROM category WHERE name = 'TV'))
ON CONFLICT DO NOTHING;

INSERT INTO product_attribute_value (product_id, attribute_id, string_value, numeric_value, boolean_value)
SELECT p.id, a.id, v.string_value, v.numeric_value, v.boolean_value
FROM (
	VALUES
	('NebulaBook A1', 'NebulaTech', 'N-Core i5', 'Nebula Iris', 14.0, 16.0, 512.0),
	('VoltEdge Pro 15', 'VoltEdge', 'VX i7', 'Volt RTX 4060', 15.6, 32.0, 1000.0),
	('PixelForge X13', 'PixelForge', 'PF i5', 'PF Arc', 13.3, 16.0, 512.0),
	('AeroSlate M16', 'AeroSlate', 'AS i7', 'AS RTX 4050', 16.0, 32.0, 1000.0),
	('QuantumLeaf 14', 'QuantumLeaf', 'QL Ryzen 5', 'QL Vega', 14.0, 16.0, 512.0),
	('TitanCore G17', 'TitanCore', 'TC i9', 'TC RTX 4070', 17.3, 32.0, 2000.0),
	('EchoBook Air 13', 'EchoBook', 'EB M2', 'Integrated', 13.6, 8.0, 256.0),
	('NovaFrame Studio', 'NovaFrame', 'NF i9', 'NF RTX 4080', 16.0, 64.0, 2000.0),
	('OrbitNote Plus', 'OrbitNote', 'ON i7', 'ON Iris Xe', 14.0, 16.0, 1000.0),
	('ZenCircuit Lite', 'ZenCircuit', 'ZC i3', 'Integrated', 15.6, 8.0, 256.0)
) AS specs(product_name, brand, cpu, gpu, screen_size, ram, storage)
JOIN product p ON p.name = specs.product_name
JOIN LATERAL (
	VALUES
	('Laptop Brand', specs.brand, NULL::DOUBLE PRECISION, NULL::BOOLEAN),
	('Laptop CPU', specs.cpu, NULL::DOUBLE PRECISION, NULL::BOOLEAN),
	('Laptop GPU', specs.gpu, NULL::DOUBLE PRECISION, NULL::BOOLEAN),
	('Laptop Screen Size', NULL::TEXT, specs.screen_size, NULL::BOOLEAN),
	('Laptop RAM', NULL::TEXT, specs.ram, NULL::BOOLEAN),
	('Laptop Storage', NULL::TEXT, specs.storage, NULL::BOOLEAN)
) AS v(attribute_name, string_value, numeric_value, boolean_value) ON TRUE
JOIN attribute a ON a.name = v.attribute_name
JOIN category c ON c.id = a.category_id AND c.name = 'Laptop'
WHERE NOT EXISTS (
	SELECT 1
	FROM product_attribute_value pav
	WHERE pav.product_id = p.id
	  AND pav.attribute_id = a.id
);

INSERT INTO product_attribute_value (product_id, attribute_id, string_value, numeric_value, boolean_value)
SELECT p.id, a.id, v.string_value, v.numeric_value, v.boolean_value
FROM (
	VALUES
	('SkyPhone One', 'SkyPhone', 6.1, 8.0, 256.0, 'SPX-1'),
	('PulseMobile X', 'PulseMobile', 6.7, 12.0, 512.0, 'PM Tensor X'),
	('LumaPhone Mini', 'LumaPhone', 5.8, 6.0, 128.0, 'LM Core 8'),
	('TerraTalk Pro', 'TerraTalk', 6.5, 12.0, 256.0, 'TT Snapdragon 8'),
	('AstraCell 5G', 'AstraCell', 6.6, 8.0, 256.0, 'AC 5G Pro'),
	('Nimbus S', 'Nimbus', 6.4, 8.0, 128.0, 'NB Fusion'),
	('VoxPhone Ultra', 'VoxPhone', 6.8, 12.0, 512.0, 'VX UltraChip'),
	('EchoMobile Neo', 'EchoMobile', 6.2, 6.0, 128.0, 'EM NeoCore'),
	('Photon Pocket', 'Photon', 6.0, 4.0, 64.0, 'PH Lite'),
	('ZenPhone Prime', 'ZenPhone', 6.5, 8.0, 256.0, 'ZP Prime 1')
) AS specs(product_name, brand, screen_size, ram, storage, cpu)
JOIN product p ON p.name = specs.product_name
JOIN LATERAL (
	VALUES
	('Mobile Phone Brand', specs.brand, NULL::DOUBLE PRECISION, NULL::BOOLEAN),
	('Mobile Phone CPU', specs.cpu, NULL::DOUBLE PRECISION, NULL::BOOLEAN),
	('Mobile Phone Screen Size', NULL::TEXT, specs.screen_size, NULL::BOOLEAN),
	('Mobile Phone RAM', NULL::TEXT, specs.ram, NULL::BOOLEAN),
	('Mobile Phone Storage', NULL::TEXT, specs.storage, NULL::BOOLEAN)
) AS v(attribute_name, string_value, numeric_value, boolean_value) ON TRUE
JOIN attribute a ON a.name = v.attribute_name
JOIN category c ON c.id = a.category_id AND c.name = 'Mobile Phone'
WHERE NOT EXISTS (
	SELECT 1
	FROM product_attribute_value pav
	WHERE pav.product_id = p.id
	  AND pav.attribute_id = a.id
);

INSERT INTO product_attribute_value (product_id, attribute_id, string_value, numeric_value, boolean_value)
SELECT p.id, a.id, v.string_value, v.numeric_value, v.boolean_value
FROM (
	VALUES
	('VisionView 43', 'VisionView', 43.0, '1080p', TRUE, 60.0),
	('CineWave 50', 'CineWave', 50.0, '4K', TRUE, 60.0),
	('AuroraScreen 55', 'AuroraScreen', 55.0, '4K', TRUE, 120.0),
	('PrismPanel 65', 'PrismPanel', 65.0, '4K', TRUE, 120.0),
	('SoundFrame 58', 'SoundFrame', 58.0, '4K', TRUE, 60.0),
	('NimbusVision 70', 'NimbusVision', 70.0, '4K', TRUE, 120.0),
	('PixelTheater 75', 'PixelTheater', 75.0, '4K', TRUE, 144.0),
	('LumaCast 48', 'LumaCast', 48.0, '4K', TRUE, 60.0),
	('NovaDisplay 60', 'NovaDisplay', 60.0, '4K', TRUE, 120.0),
	('ZenView OLED 55', 'ZenView', 55.0, '4K OLED', TRUE, 120.0)
) AS specs(product_name, brand, screen_size, resolution, smart_tv, refresh_rate)
JOIN product p ON p.name = specs.product_name
JOIN LATERAL (
	VALUES
	('TV Brand', specs.brand, NULL::DOUBLE PRECISION, NULL::BOOLEAN),
	('TV Resolution', specs.resolution, NULL::DOUBLE PRECISION, NULL::BOOLEAN),
	('TV Screen Size', NULL::TEXT, specs.screen_size, NULL::BOOLEAN),
	('TV Refresh Rate', NULL::TEXT, specs.refresh_rate, NULL::BOOLEAN),
	('TV Smart TV', NULL::TEXT, NULL::DOUBLE PRECISION, specs.smart_tv)
) AS v(attribute_name, string_value, numeric_value, boolean_value) ON TRUE
JOIN attribute a ON a.name = v.attribute_name
JOIN category c ON c.id = a.category_id AND c.name = 'TV'
WHERE NOT EXISTS (
	SELECT 1
	FROM product_attribute_value pav
	WHERE pav.product_id = p.id
	  AND pav.attribute_id = a.id
);
