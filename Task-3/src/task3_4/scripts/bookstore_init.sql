\set ON_ERROR_STOP on
\encoding UTF8

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'bookstore') THEN
        EXECUTE 'CREATE DATABASE bookstore';
    END IF;
END $$;

\c bookstore
\i 'DDL bookstore.sql'
\i 'DML bookstore.sql'
