FROM openjdk:11
WORKDIR /app
COPY . .
RUN javac -cp "lib/*" src/CloudSimExample.java -d out
CMD ["java", "-cp", "lib/*:out", "cloudsim.CloudSimExample"]
