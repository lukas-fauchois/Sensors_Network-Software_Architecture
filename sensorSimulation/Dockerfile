FROM openjdk:11
COPY  . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN javac -classpath org.eclipse.paho.client.mqttv3-1.2.5.jar App.java Capteurs.java
CMD ["java","--illegal-access=deny","-cp","org.eclipse.paho.client.mqttv3-1.2.5.jar:.","App"]


