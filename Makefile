build.run:
	./gradlew clean build -x test
	java -jar build/libs/urlshortener-0.0.1-SNAPSHOT.jar

run.dev:
	set -o allexport; source .env.dev.local; set +o allexport; ./gradlew bootRun --args='--spring.profiles.active=dev'

docker.build:
	docker build --no-cache -t ranjabi/urlshortener .

docker.run:
	docker run -p 8081:8080 -t ranjabi/urlshortener

compose.down.dev:
	docker compose -f docker-compose.dev.yml down --volumes

compose.up.dev:
	make compose.down.dev
	docker compose -f docker-compose.dev.yml --env-file .env.dev.docker up --build