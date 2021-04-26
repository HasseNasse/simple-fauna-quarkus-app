# Deployment (AWS)

1. Start off by creating a bucket `S3_BUCKET=myrandombucket make bucket`
2. Build the quarkus native executable `make build`
3. Deploy `S3_BUCKET=myrandombucket make deploy`
