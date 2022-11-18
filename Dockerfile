FROM openjdk:11.0.16-jre
RUN java --version

#ARG JAR_PATH="build/libs/"
ENV JAVA_OPTS="-Xms256m -Xmx256m"

#RUN echo "Environment variable JAVA_OPTS : $JAVA_OPTS || Argumets JAR_PATH : $JAR_PATH"

ADD ./management-*.jar /opt/management.jar
ENTRYPOINT exec java -Dfile.encoding=UTF-8 -jar /opt/management.jar $JAVA_OPTS
EXPOSE 8081