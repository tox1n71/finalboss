FROM openjdk:18
COPY build/libs/finalboss.jar /app/finalboss.jar
CMD java -jar /app/finalboss.jar