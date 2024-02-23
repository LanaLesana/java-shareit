    DROP TABLE IF EXISTS users CASCADE;
    DROP TABLE IF EXISTS items CASCADE;
    DROP TABLE IF EXISTS bookings CASCADE;
    DROP TABLE IF EXISTS comments CASCADE;
    DROP TABLE IF EXISTS requests CASCADE;
    DROP TABLE IF EXISTS comment CASCADE;


    CREATE TABLE IF NOT EXISTS users (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255) ,
        email VARCHAR(255)
    );

     create table IF NOT EXISTS REQUESTS
(
    ID   INT auto_increment,
    DESCRIPTION  VARCHAR(1000)               not null,
    REQUESTER_ID INT                      not null,
    CREATED      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    constraint "REQUESTS_pk"
    primary key (ID),
    constraint "REQUESTS_USERS_ID_fk"
    foreign key (REQUESTER_ID) references USERS (ID) ON DELETE CASCADE
    );

    CREATE TABLE IF NOT EXISTS items (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        description VARCHAR(255),
        is_available BOOL,
        owner_id INT,
        request_id INT,
        FOREIGN KEY (owner_id) REFERENCES users(id),
        FOREIGN KEY (request_id) REFERENCES requests(id)
    );

    CREATE TABLE IF NOT EXISTS bookings (
        id INT AUTO_INCREMENT PRIMARY KEY,
        start_of_booking TIMESTAMP,
        end_of_booking TIMESTAMP,
        item_id INT,
        booker_id INT,
        status VARCHAR(255),
        CONSTRAINT booking_fk_items FOREIGN KEY (item_id) REFERENCES items(id),
        CONSTRAINT booking_fk_user FOREIGN KEY (booker_id) REFERENCES users(id)
    );


    CREATE TABLE IF NOT EXISTS comments (
        id INT AUTO_INCREMENT PRIMARY KEY,
        text VARCHAR(255) NOT NULL,
        item_id INT,
        author_id INT,
        created TIMESTAMP,
        CONSTRAINT comments_fk_items FOREIGN KEY (item_id) REFERENCES items(id),
        CONSTRAINT comments_fk_user FOREIGN KEY (author_id) REFERENCES users(id)
    );


