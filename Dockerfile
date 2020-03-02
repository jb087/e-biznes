FROM ubuntu:18.04

ENV TZ=Europe/Warsaw
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone 

#JAVA
RUN apt-get update &&\
	apt-get upgrade -y &&\
    apt-get install -y  software-properties-common &&\
	add-apt-repository ppa:openjdk-r/ppa &&\
	apt-get update &&\
	apt-get -y install openjdk-8-jdk
	
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV PATH $JAVA_HOME/bin:$PATH

#VIM, GIT, UNZIP, WGET, CURL
RUN apt update && apt install -y \
  unzip \
  vim \
  git \
  wget \
  curl

#SCALA
RUN wget http://scala-lang.org/files/archive/scala-2.12.8.deb &&\
    dpkg -i scala-2.12.8.deb &&\
    apt-get update &&\
    apt-get install scala

#SBT
RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list &&\
	curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add &&\
	apt-get update &&\
	apt-get install sbt

#NPM
RUN apt-get update &&\
	apt-get install -y nodejs &&\
	apt-get install -y npm
	
RUN npm install -g npm@6.8.0

EXPOSE 8000
EXPOSE 9000
EXPOSE 5000
EXPOSE 8888

RUN useradd -ms /bin/bash bjedrzejczyk
RUN adduser bjedrzejczyk sudo

USER bjedrzejczyk
WORKDIR /home/bjedrzejczyk/
VOLUME /home/bjedrzejczyk/projekt

#RUN java -version
#RUN git --version
#RUN vim --version
#RUN unzip -v
#RUN scala -version
#RUN sbt --version
#RUN npm -v