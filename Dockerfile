FROM ubuntu:18.04

MAINTAINER Wojciech Ozimek, https://github.com/oziomek1

RUN useradd oziomek --create-home

RUN apt-get update
RUN apt-get install -y vim unzip curl git software-properties-common apt-utils apt-transport-https

# add config here:

# Install Java
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
	add-apt-repository -y ppa:webupd8team/java && \
	apt-get update && \
	apt-get install -y --allow-unauthenticated oracle-java8-installer && \
	apt-get install oracle-java8-set-default && \
	rm -rf /var/lib/apt/lists/* && \
	rm -rf /var/cache/oracle-jdk8-installer

# Install Scala
ENV SCALA_VERSION 2.12.8
ENV SCALA_PKG scala-$SCALA_VERSION

RUN echo "deb http://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list && \
	apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823 && \
    apt-get update && \
    apt-get install --no-install-recommends -y --allow-unauthenticated sbt && \
    wget http://www.scala-lang.org/files/archive/$SCALA_PKG.deb && \
    dpkg -i $SCALA_PKG.deb && rm $SCALA_PKG.deb && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*


RUN mkdir /home/directory

VOLUME ["/home/directory/"]

WORKDIR /home

ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

USER oziomek

CMD echo "Hello World"