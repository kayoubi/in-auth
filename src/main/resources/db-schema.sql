-- create DB
create database inauth_locations with owner postgres;


-- switch to DB
-- \connect inauth_locations

-- create postgis extension
CREATE EXTENSION postgis;
CREATE EXTENSION postgis_topology;

-- create schema
create schema inauth;

create table inauth.locations (
    id serial PRIMARY KEY,
    geom geometry(point, 4269)
);
CREATE INDEX idx_locations_geom ON inauth.locations USING gist(geom);

create table inauth.cities (
    id serial PRIMARY KEY,
    name varchar(256),
    geom geometry(point, 4269)
);
CREATE INDEX idx_cities_geom ON inauth.cities USING gist(geom);
CREATE UNIQUE INDEX idx_cities_name ON inauth.cities (name);

