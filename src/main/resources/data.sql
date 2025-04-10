-- Insert Products (with currency)
INSERT INTO "PRODUCT" (id, external_id, name, description, price, currency) VALUES
(1, '11111111-1111-1111-1111-111111111111', 'Laptop', 'Powerful gaming laptop', 1999.99, 'USD'),
(2, '11111111-1111-1111-1111-111111111112', 'Keyboard', 'Mechanical keyboard', 89.99, 'USD'),
(3, '11111111-1111-1111-1111-111111111113', 'Mouse', 'Wireless mouse', 45.00, 'USD'),
(4, '11111111-1111-1111-1111-111111111114', 'Monitor', '27-inch 4K monitor', 349.99, 'USD'),
(5, '11111111-1111-1111-1111-111111111115', 'Headphones', 'Noise-cancelling headphones', 199.99, 'USD'),
(6, '11111111-1111-1111-1111-111111111116', 'Webcam', 'HD webcam', 79.99, 'USD'),
(7, '11111111-1111-1111-1111-111111111117', 'Microphone', 'Studio condenser mic', 129.00, 'USD'),
(8, '11111111-1111-1111-1111-111111111118', 'Chair', 'Ergonomic office chair', 249.50, 'USD'),
(9, '11111111-1111-1111-1111-111111111119', 'Desk', 'Standing desk', 399.99, 'USD'),
(10, '11111111-1111-1111-1111-111111111120', 'Docking Station', 'USB-C docking station', 149.00, 'USD');

-- Insert Orders (no currency)
INSERT INTO "ORDER_ENTITY" (id, external_id, email, created_at) VALUES
(1, '22222222-2222-2222-2222-222222222201', 'a@b.com', '2024-01-10T10:30:00Z'),
(2, '22222222-2222-2222-2222-222222222202', 'xyz@gmail.com', '2024-02-15T14:15:00Z'),
(3, '22222222-2222-2222-2222-222222222203', 'a@b.com', '2024-03-20T09:45:00Z'),
(4, '22222222-2222-2222-2222-222222222204', 'a@b.com', '2024-04-25T16:00:00Z'),
(5, '22222222-2222-2222-2222-222222222205', 'a@b.com', '2024-06-01T12:00:00Z'),
(6, '22222222-2222-2222-2222-222222222206', 'xyz@gmail.com', '2024-08-05T08:45:00Z'),
(7, '22222222-2222-2222-2222-222222222207', 'xyz@gmail.com', '2024-10-10T17:30:00Z'),
(8, '22222222-2222-2222-2222-222222222208', 'xyz@gmail.com', '2024-12-15T11:20:00Z');

-- Insert Order Items (with currency)
INSERT INTO "ORDER_ITEM" (id, product_id, order_id, price, currency) VALUES
-- Order 1
(1, 1, 1, 1999.99, 'USD'),
(2, 2, 1, 89.99, 'USD'),
(3, 3, 1, 45.00, 'USD'),

-- Order 2
(4, 4, 2, 349.99, 'USD'),
(5, 5, 2, 199.99, 'USD'),
(6, 6, 2, 79.99, 'USD'),
(7, 7, 2, 129.00, 'USD'),

-- Order 3
(8, 8, 3, 249.50, 'USD'),
(9, 9, 3, 399.99, 'USD'),
(10, 10, 3, 149.00, 'USD'),

-- Order 4
(11, 1, 4, 1999.99, 'USD'),
(12, 5, 4, 199.99, 'USD'),
(13, 3, 4, 45.00, 'USD'),

-- Order 5
(14, 2, 5, 89.99, 'USD'),
(15, 4, 5, 349.99, 'USD'),
(16, 6, 5, 79.99, 'USD'),

-- Order 6
(17, 7, 6, 129.00, 'USD'),
(18, 8, 6, 249.50, 'USD'),
(19, 9, 6, 399.99, 'USD'),
(20, 10, 6, 149.00, 'USD'),

-- Order 7
(21, 1, 7, 1999.99, 'USD'),
(22, 3, 7, 45.00, 'USD'),
(23, 6, 7, 79.99, 'USD'),
(24, 2, 7, 89.99, 'USD'),
(25, 7, 7, 129.00, 'USD'),

-- Order 8
(26, 4, 8, 349.99, 'USD'),
(27, 5, 8, 199.99, 'USD'),
(28, 10, 8, 149.00, 'USD');