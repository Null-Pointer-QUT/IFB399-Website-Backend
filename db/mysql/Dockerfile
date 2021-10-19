FROM mysql:5.7

MAINTAINER stephen(n10889299@qut.edu.au)

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ./*.sql /docker-entrypoint-initdb.d/
