FROM mongo:4.2

MAINTAINER stephen(n10889299@qut.edu.au)

RUN mkdir /home/mongodb
COPY ./dump.tar.gz /home/mongodb/
COPY ./setup.sh /docker-entrypoint-initdb.d/
RUN cd /home/mongodb && tar xzvf dump.tar.gz && chown -R mongodb:mongodb /home/mongodb
USER root