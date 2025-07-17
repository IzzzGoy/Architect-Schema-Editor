FROM bellsoft/liberica-openjdk-alpine:21
# Create and set working directory
WORKDIR /app

# Copy only necessary files (optimized for caching)
COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY gradle gradle
COPY gradlew .
COPY server/database/migrations server/database/migrations

# Copy build files for the relevant modules
COPY server/database/migrations/build.gradle.kts server/database/migrations/
ENTRYPOINT ./gradlew --parallel --build-cache server:database:migrations:runJvm