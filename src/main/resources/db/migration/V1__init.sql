
CREATE TABLE car (
    id BIGSERIAL NOT NULL,
    make VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    year INTEGER NOT NULL
);

ALTER TABLE car ADD CONSTRAINT pk_car PRIMARY KEY (id);

INSERT INTO car (make, model, year) VALUES ('Mercedes-Benz', '500 SL', 1992);