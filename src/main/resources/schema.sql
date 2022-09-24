    create table client (
       id bigint generated by default as identity,
        password varchar(255),
        username varchar(255),
        wallet double not null,
        primary key (id)
    );

    create table order_product (
       order_id bigint not null,
        product_id bigint not null
    );

    create table orders (
       id bigint generated by default as identity,
        order_time timestamp,
        client_id bigint,
        primary key (id)
    );

    create table product (
       id bigint generated by default as identity,
        date timestamp,
        description varchar(255),
        image varchar(255),
        price double not null,
        stocks integer not null,
        title varchar(255),
        client_id bigint,
        primary key (id)
    );

    alter table client 
       add constraint username_unique unique (username);

    alter table order_product 
       add constraint FKhnfgqyjx3i80qoymrssls3kno 
       foreign key (product_id) 
       references product;

    alter table order_product 
       add constraint FKl5mnj9n0di7k1v90yxnthkc73 
       foreign key (order_id) 
       references orders;

    alter table orders 
       add constraint FK17yo6gry2nuwg2erwhbaxqbs9 
       foreign key (client_id) 
       references client;

    alter table product 
       add constraint FK3g8nmhhbt7mwbf9r0g5qon8m0 
       foreign key (client_id) 
       references client;
