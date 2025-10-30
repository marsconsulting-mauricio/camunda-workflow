CREATE EXTENSION IF NOT EXISTS dblink;

-- =========================================================
-- Criação dos bancos camunda e clientes_db
-- =========================================================
DO
$$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'camunda') THEN
      PERFORM dblink_exec('dbname=postgres', 'CREATE DATABASE camunda');
   END IF;
END
$$;

DO
$$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'clientes_db') THEN
      PERFORM dblink_exec('dbname=postgres', 'CREATE DATABASE clientes_db');
   END IF;
END
$$;

-- =========================================================
-- Conceder permissões
-- =========================================================
GRANT ALL PRIVILEGES ON DATABASE camunda TO camunda;
GRANT ALL PRIVILEGES ON DATABASE clientes_db TO camunda;

\c clientes_db
GRANT ALL ON SCHEMA public TO camunda;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO camunda;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO camunda;

\c camunda
GRANT ALL ON SCHEMA public TO camunda;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO camunda;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO camunda;

-- =========================================================
-- Criação do usuário spring_user e permissões em clientes_db
-- =========================================================
DO
$$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'spring_user') THEN
      CREATE ROLE spring_user LOGIN PASSWORD 'dbclientes';
   END IF;
END
$$;

ALTER DATABASE clientes_db OWNER TO spring_user;

\c clientes_db
GRANT ALL ON SCHEMA public TO spring_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO spring_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO spring_user;
