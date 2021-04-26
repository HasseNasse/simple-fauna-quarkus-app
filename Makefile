AWS_REGION ?= eu-west-1
S3_BUCKET ?= foobar-artifactory-bucket

build:
	mvn package -Pnative -Dquarkus.native.container-build=true

deploy:
	sam deploy -t sam.yaml \
	--stack-name simple-fauna-quarkus-app-stack \
	--region $(AWS_REGION) \
	--capabilities CAPABILITY_NAMED_IAM \
	--s3-bucket $(S3_BUCKET) \
	--parameter-overrides \
		Service=simple-fauna-quarkus-app \

bucket: 
	aws s3 mb s3://$(S3_BUCKET)
