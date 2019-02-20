FROM apache/zeppelin:0.8.1

RUN mkdir /data
WORKDIR /data
RUN curl -O http://archive.ics.uci.edu/ml/machine-learning-databases/00222/bank.zip
RUN unzip bank.zip

WORKDIR /zeppelin
