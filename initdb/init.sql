CREATE EXTENSION IF NOT EXISTS dblink;
-- Criação do banco de dados camunda_db (somente se não existir)
DO
$$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'camunda_db') THEN
      PERFORM dblink_exec('dbname=postgres', 'CREATE DATABASE camunda_db');
   END IF;
END
$$;

-- Criação do banco de dados clientes_db (somente se não existir)
DO
$$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'clientes_db') THEN
      PERFORM dblink_exec('dbname=postgres', 'CREATE DATABASE clientes_db');
   END IF;
END
$$;

-- Conceder permissões
GRANT ALL PRIVILEGES ON DATABASE camunda_db TO camunda;
GRANT ALL PRIVILEGES ON DATABASE clientes_db TO camunda;

-- Conectar e ajustar permissões
\c clientes_db
GRANT ALL ON SCHEMA public TO camunda;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO camunda;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO camunda;

\c camunda_db
GRANT ALL ON SCHEMA public TO camunda;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO camunda;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO camunda;
