## Build and run application with docker compose
```
docker compose -f docker-compose.yml up --build
```

## Build Docker Image
```
docker build -t product-service .
```

### Connect to database
```
mysql -u root -p
```

### Show Databases
```
show databases; \g
```

### Show Tables
```
show tables from productservice; \g
```

### Use Database productservice
```
use productservice; \g
```

### Example query
```
select * from product; \g
```
