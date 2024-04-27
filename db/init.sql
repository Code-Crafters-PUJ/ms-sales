CREATE USER replicator WITH REPLICATION ENCRYPTED PASSWORD 'replicator_password';
SELECT pg_create_physical_replication_slot('replication_slot');

-- Create table client
CREATE TABLE IF NOT EXISTS client (
  id SERIAL PRIMARY KEY,
  name VARCHAR(45) NOT NULL
);

-- Create table paymentMethod
CREATE TABLE IF NOT EXISTS paymentMethod (
  id SERIAL PRIMARY KEY,
  method VARCHAR(45) NOT NULL UNIQUE
);

-- Create table bill
CREATE TABLE IF NOT EXISTS bill (
  id SERIAL PRIMARY KEY,
  branch_id INT NOT NULL,
  type CHAR(1) CHECK (type IN ('N', 'E')) NOT NULL,
  date DATE NOT NULL,
  seller VARCHAR(45) NOT NULL,
  email VARCHAR(45),
  client_id INT NOT NULL,
  paymentMethod_id INT NOT NULL,
  aiu BOOLEAN NOT NULL DEFAULT FALSE,
  withholding_tax BOOLEAN NOT NULL DEFAULT FALSE,
  charge_tax BOOLEAN NOT NULL DEFAULT FALSE,
  FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  FOREIGN KEY (paymentMethod_id) REFERENCES paymentMethod(id) ON DELETE NO ACTION ON UPDATE NO ACTION
);


-- Table creation for 'product'
CREATE TABLE IF NOT EXISTS product (
  id SERIAL PRIMARY KEY,
  name VARCHAR(45) NOT NULL,
  description VARCHAR(45) NOT NULL,
  quantity INT NOT NULL,
  costPrice DOUBLE PRECISION NOT NULL,
  salePrice DOUBLE PRECISION NOT NULL,
  discount INT,
  Category_id INT NOT NULL
);

-- Table creation for 'bill_has_product'
CREATE TABLE IF NOT EXISTS bill_has_product (
  bill_id INT NOT NULL,
  product_id INT NOT NULL,
  quantity INT,
  FOREIGN KEY (bill_id) REFERENCES bill(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE NO ACTION ON UPDATE NO ACTION
);
