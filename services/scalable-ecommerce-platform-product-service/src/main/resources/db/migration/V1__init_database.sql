create sequence if not exists product_category_sequence increment by 50;
create sequence if not exists product_sequence increment by 50;

create table if not exists product_category
(
    id bigint not null primary key,
    description varchar(255),
    name varchar(255)
);

create table if not exists product
(
    id bigint not null primary key,
    description varchar(255),
    name varchar(255),
    available_quantity int not null,
    price numeric(38, 2),
    product_category_id bigint constraint fk_cat_to_product references product_category
);