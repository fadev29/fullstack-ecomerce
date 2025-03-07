-- table customer --
create table customer(
id uuid primary key default gen_random_uuid(),
username varchar(50) unique not null,
email varchar(100)  unique not null,
password varchar(255) not null,
role varchar(15) not null check(role in('CUSTOMER','ADMIN')),
created_at timestamp default current_timestamp,
updated_at timestamp default current_timestamp
);
-- untuk melihat table customer
select * from customer

-- table category --
create table category(
id serial primary key,
category_name varchar(255) not null
);

-- untuk melihat table category
select * from customer


-- table products --
create table products(
id serial primary key,
category_id int not null,
product_name varchar(255) not null,
description varchar(255) not null,
price double precision not null,
stock int not null,
images text not null,
created_at timestamp default current_timestamp,
updated_at timestamp default current_timestamp,
constraint fk_products_category foreign key (category_id) references category(id) on delete cascade

);

-- untuk melihat table products
select * from products



-- table orders --
create table orders(
id serial primary key,
customer_id uuid not null,
total_price int not null,
status varchar(255) not null,
created_at timestamp default current_timestamp,
updated_at timestamp default current_timestamp,
constraint fk_order_customer foreign key (customer_id) references customer(id) on delete cascade
);

-- untuk melihat table orders
select * from orders

-- table order items
create table order_items(
id serial primary key,
order_id int not null,
product_id not null,
qty int not null,
total_price int not null,
constraint fk_order_items_orders foreign key (order_id) references orders(id) on delete cascade,
constraint fk_order_items_products foreign key (product_id) references products(id) on delete cascade
);

-- untuk melihat table order_items
select * from orders_items


-- table history --
create table history(
id serial primary key,
order_id int not null,
status varchar(255) not null,
created_at timestamp default current_timestamp,
constraint fk_history_orders foreign key (order_id) references orders(id) on delete cascade
);

-- untuk melihat table history
select * from history


-- table carts --
create table carts(
id serial primary key,
customer_id uuid not null,
product_id int not null,
qty int not null,
constraint fk_carts_customer foreign key (customer_id) references customer(id) on delete cascade
constraint fk_carts_products foreign key (product_id) references products(id) on delete cascade

);

