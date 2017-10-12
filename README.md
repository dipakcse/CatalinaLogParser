# CatalinaLogParser
This is a java project that parse web server access log file and detect ip address that is made request equal or more than given input(threshold) parameter and other three parameters of log file location, start date, hourly/daily. It is also insert matched ip Addresses on mysql database to see details.
To run this project follow bellow instruction:
1: make a jar file
2: run following command
 access log file,start date,duration,threshold
java -jar .\parser.jar .\access.log 2017-01-01.15:00:00 hourly 200
