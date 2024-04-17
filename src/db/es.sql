-- Table for Products
CREATE TABLE Products (
    ProductID INT PRIMARY KEY,
    ProductName VARCHAR(255)
);

-- Table for Clients
CREATE TABLE Clients (
    ClientID INT PRIMARY KEY,
    ClientName VARCHAR(255)
);

-- Table for Product Pricing (details from Table A)
CREATE TABLE ProductPricing (
    ProductID INT,
    UnitCostEUR DECIMAL(10, 2),
    Markup VARCHAR(255),
    Promotion VARCHAR(255),
    PRIMARY KEY (ProductID),
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
);

-- Table for Client Discounts (details from Table B)
CREATE TABLE ClientDiscounts (
    ClientID INT,
    BasicClientDiscount DECIMAL(10, 2),
    AdditionalDiscountAbove10000 DECIMAL(10, 2),
    AdditionalDiscountAbove30000 DECIMAL(10, 2),
    PRIMARY KEY (ClientID),
    FOREIGN KEY (ClientID) REFERENCES Clients(ClientID)
);
-- Insert data into Products table
INSERT INTO Products (ProductID, ProductName) VALUES
(1, 'Danish Muffin'),
(2, 'Granny’s Cup Cake'),
(3, 'Frenchy’s Croissant'),
(4, 'Crispy chips');

-- Insert data into Clients table
INSERT INTO Clients (ClientID, ClientName) VALUES
(1, 'ABC Distribution'),
(2, 'DEF Foods'),
(3, 'GHI Trade'),
(4, 'JKL Kiosks'),
(5, 'MNO Vending 2');
-- Insert data into ProductPricing table
INSERT INTO ProductPricing (ProductID, UnitCostEUR, Markup, Promotion) VALUES
(1, 0.52, '80%', 'none'),
(2, 0.38, '120%', '30% off'),
(3, 0.41, '0.90 EUR/unit', 'none'),
(4, 0.60, '1.00 EUR/unit', 'Buy 2, get 3rd free');
-- Insert data into ClientDiscounts table
INSERT INTO ClientDiscounts (ClientID, BasicClientDiscount, AdditionalDiscountAbove10000, AdditionalDiscountAbove30000) VALUES
(1, 5, 0, 2),
(2, 4, 1, 2),
(3, 3, 1, 3),
(4, 2, 3, 5),
(5, 0, 5, 7);
