# Create volume for persistent data

docker volume create food_delivery_pgdata

# Create network (optional, but good for connecting with other services)

docker network create food_delivery_network

# Run PostgreSQL container

docker run -d --name food_delivery_postgres --network food_delivery_network  --cpus=1 --memory=2g --restart unless-stopped -e POSTGRES_DB=db_food_delivery -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=123456 -v food_delivery_pgdata:/var/lib/postgresql/data -p 5431:5432 postgres:15

# Verify container is running

docker ps -a | grep movie_postgres

# Check logs

docker logs movie_postgres

# Connect to PostgreSQL using psql (if needed)

docker exec -it movie_postgres psql -U postgres -d db_movie

# Stop container

# docker stop movie_postgres

# Remove container

# docker rm movie_postgres

# Remove volume (careful - this deletes all data!)

# docker volume rm movie_pgdata

# Backup
docker exec movie_postgres pg_dump -U postgres db_movie > backup.sql

# Restore nếu cần
cat backup.sql | docker exec -i movie_postgres psql -U postgres -d db_movie




# Thêm các tham số này vào PostgreSQL config
max_connections = 100
shared_buffers = 256MB
work_mem = 4MB
maintenance_work_mem = 64MB

# Connect vào container
docker exec -it movie_postgres bash

# Edit postgresql.conf
echo "
max_connections = 100
shared_buffers = 256MB
work_mem = 4MB
maintenance_work_mem = 64MB
" >> /var/lib/postgresql/data/postgresql.conf

# Thêm monitoring:
# Install pg_stat_statements extension
docker exec -it movie_postgres psql -U postgres -d db_movie -c "CREATE EXTENSION pg_stat_statements;"

docker exec -it movie_postgres psql -U postgres -c "SHOW max_connections;"
docker exec -it movie_postgres psql -U postgres -c "SHOW shared_buffers;"
docker exec -it movie_postgres psql -U postgres -c "SHOW work_mem;"
docker exec -it movie_postgres psql -U postgres -c "SHOW maintenance_work_mem;"