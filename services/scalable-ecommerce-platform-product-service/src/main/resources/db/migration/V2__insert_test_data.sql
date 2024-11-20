-- Insert product categories
INSERT INTO product_category (id, name, description) VALUES
                                                         (1, 'Electronics', 'Electronic devices and accessories'),
                                                         (2, 'Books', 'Books in various formats and categories'),
                                                         (3, 'Sports', 'Sports equipment and accessories'),
                                                         (4, 'Home & Garden', 'Home and garden supplies'),
                                                         (5, 'Clothing', 'Clothes and accessories'),
                                                         (6, 'Beauty', 'Cosmetics and personal care'),
                                                         (7, 'Food & Beverages', 'Food products and drinks'),
                                                         (8, 'Toys', 'Games and toys for all ages'),
                                                         (9, 'Automotive', 'Car parts and accessories'),
                                                         (10, 'Pet Supplies', 'Products for pets');

-- Insert products
INSERT INTO product (id, name, description, available_quantity, price, product_category_id) VALUES
-- Electronics
(1, 'Smartphone Pro X', 'Latest 5G smartphone with high-end camera', 50, 999.99, 1),
(2, 'Laptop Elite Book', 'Powerful laptop for professionals', 25, 1499.99, 1),
(3, 'Wireless Headphones', 'Noise-cancelling wireless headphones', 100, 299.99, 1),
(4, 'Smart Watch Series 5', 'Advanced fitness tracking smartwatch', 75, 399.99, 1),
(5, 'Tablet Air', 'Lightweight tablet for entertainment', 60, 699.99, 1),
(6, 'Bluetooth Speaker', 'Portable wireless speaker', 120, 89.99, 1),
(7, 'Gaming Console XS', 'Next-gen gaming console', 30, 499.99, 1),
(8, 'Wireless Mouse', 'Ergonomic wireless mouse', 200, 49.99, 1),

-- Books
(9, 'Python Programming', 'Complete guide to Python', 150, 49.99, 2),
(10, 'Art History', 'Comprehensive art history guide', 75, 79.99, 2),
(11, 'Cooking Basics', 'Essential cooking techniques', 90, 39.99, 2),
(12, 'Science Fiction Collection', 'Best sci-fi stories anthology', 100, 29.99, 2),
(13, 'Business Strategy', 'Modern business approaches', 80, 59.99, 2),
(14, 'Health & Wellness', 'Guide to healthy living', 120, 34.99, 2),
(15, 'Travel Guide Europe', 'Comprehensive European travel guide', 95, 44.99, 2),
(16, 'Classic Literature Set', 'Collection of classic novels', 60, 89.99, 2),

-- Sports
(17, 'Basketball Pro', 'Professional basketball', 200, 49.99, 3),
(18, 'Yoga Mat Premium', 'Non-slip exercise mat', 300, 29.99, 3),
(19, 'Dumbbells Set', 'Set of adjustable dumbbells', 150, 199.99, 3),
(20, 'Tennis Racket', 'Professional tennis racket', 80, 159.99, 3),
(21, 'Running Shoes', 'Lightweight running shoes', 250, 129.99, 3),
(22, 'Fitness Tracker', 'Advanced activity tracker', 180, 89.99, 3),
(23, 'Swimming Goggles', 'Anti-fog swimming goggles', 400, 19.99, 3),
(24, 'Jump Rope', 'Speed jump rope', 500, 14.99, 3),

-- Home & Garden
(25, 'Tool Set', 'Complete home tool kit', 80, 199.99, 4),
(26, 'Ceramic Planter', 'Decorative flower pot', 400, 24.99, 4),
(27, 'Floor Lamp', 'Modern standing lamp', 60, 159.99, 4),
(28, 'Garden Hose', 'Expandable garden hose', 150, 39.99, 4),
(29, 'Bed Sheets Set', 'Cotton bed sheets set', 200, 79.99, 4),
(30, 'Kitchen Knife Set', 'Professional knife set', 100, 129.99, 4),
(31, 'Indoor Plant', 'Low-maintenance house plant', 250, 34.99, 4),
(32, 'Storage Containers', 'Set of storage boxes', 300, 49.99, 4),

-- Clothing
(33, 'Cotton T-Shirt', 'Basic cotton t-shirt', 500, 19.99, 5),
(34, 'Denim Jeans', 'Classic fit jeans', 300, 59.99, 5),
(35, 'Hoodie', 'Warm hooded sweatshirt', 250, 49.99, 5),
(36, 'Winter Jacket', 'Waterproof winter coat', 150, 199.99, 5),
(37, 'Dress Shoes', 'Formal leather shoes', 100, 149.99, 5),
(38, 'Sunglasses', 'UV protection sunglasses', 200, 89.99, 5),
(39, 'Baseball Cap', 'Adjustable cotton cap', 400, 24.99, 5),
(40, 'Wool Scarf', 'Warm winter scarf', 300, 29.99, 5),

-- Beauty
(41, 'Face Cream', 'Moisturizing face cream', 200, 39.99, 6),
(42, 'Shampoo', 'Natural hair shampoo', 300, 14.99, 6),
(43, 'Perfume', 'Luxury perfume', 150, 89.99, 6),
(44, 'Makeup Set', 'Complete makeup kit', 100, 199.99, 6),
(45, 'Hair Dryer', 'Professional hair dryer', 80, 129.99, 6),
(46, 'Face Mask Set', 'Hydrating face masks', 400, 19.99, 6),
(47, 'Nail Polish', 'Long-lasting nail polish', 250, 9.99, 6),
(48, 'Body Lotion', 'Moisturizing body lotion', 350, 24.99, 6),

-- Food & Beverages
(49, 'Coffee Beans', 'Premium coffee beans', 200, 19.99, 7),
(50, 'Green Tea', 'Organic green tea', 300, 14.99, 7),
(51, 'Dark Chocolate', 'Premium dark chocolate', 400, 9.99, 7),
(52, 'Olive Oil', 'Extra virgin olive oil', 150, 29.99, 7),
(53, 'Mixed Nuts', 'Assorted premium nuts', 250, 24.99, 7),
(54, 'Energy Bars', 'Protein energy bars pack', 500, 19.99, 7),
(55, 'Honey', 'Organic raw honey', 200, 15.99, 7),
(56, 'Dried Fruits', 'Mixed dried fruits pack', 300, 12.99, 7),

-- Toys
(57, 'Building Blocks', 'Educational building blocks', 100, 39.99, 8),
(58, 'Board Game', 'Family board game', 150, 29.99, 8),
(59, 'Remote Car', 'RC racing car', 80, 89.99, 8),
(60, 'Plush Bear', 'Soft teddy bear', 200, 19.99, 8),
(61, 'Puzzle Set', '1000-piece puzzle', 120, 24.99, 8),
(62, 'Art Set', 'Children''s art supplies', 150, 34.99, 8),
(63, 'Action Figure', 'Collectible action figure', 100, 49.99, 8),
(64, 'Educational Robot', 'Coding robot for kids', 50, 149.99, 8),

-- Automotive
(65, 'Car Battery', 'High-performance car battery', 50, 199.99, 9),
(66, 'Motor Oil', 'Synthetic motor oil', 200, 49.99, 9),
(67, 'Windshield Wipers', 'All-weather wipers', 300, 29.99, 9),
(68, 'Car Cover', 'Waterproof car cover', 100, 79.99, 9),
(69, 'Air Freshener', 'Car air freshener', 500, 9.99, 9),
(70, 'Tire Inflator', 'Portable tire pump', 150, 59.99, 9),
(71, 'Car Wash Kit', 'Complete car cleaning kit', 120, 49.99, 9),
(72, 'Jump Starter', 'Emergency jump starter', 80, 129.99, 9),

-- Pet Supplies
(73, 'Dog Food', 'Premium dry dog food', 150, 54.99, 10),
(74, 'Cat Litter', 'Clumping cat litter', 200, 19.99, 10),
(75, 'Pet Bed', 'Comfortable pet bed', 100, 49.99, 10),
(76, 'Pet Toys', 'Assorted pet toys', 300, 14.99, 10),
(77, 'Fish Tank', 'Glass aquarium set', 50, 199.99, 10),
(78, 'Pet Carrier', 'Travel pet carrier', 80, 69.99, 10),
(79, 'Pet Brush', 'Grooming brush', 200, 15.99, 10),
(80, 'Pet Bowl Set', 'Stainless steel food bowls', 250, 24.99, 10);