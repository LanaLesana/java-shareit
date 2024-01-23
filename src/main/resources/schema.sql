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

    CREATE TABLE IF NOT EXISTS items (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        description VARCHAR(255),
        is_available BOOL,
        owner_id INT,
        request_id VARCHAR(255)
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

    CREATE TABLE IF NOT EXISTS requests (
        id INT AUTO_INCREMENT PRIMARY KEY,
        description VARCHAR(255) NOT NULL,
        requester_id INT,
        created TIMESTAMP,
        CONSTRAINT requests_fk_user FOREIGN KEY (requester_id) REFERENCES users(id)

    );
