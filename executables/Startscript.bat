echo "Starting the first Spring Boot application..."
start java -jar accountservice-0.0.1-SNAPSHOT.jar > account-app.log 2>&1 &

echo "Starting the second Spring Boot application..."
start java -jar transactionservice-0.0.1-SNAPSHOT.jar > transaction-app.log 2>&1 &
