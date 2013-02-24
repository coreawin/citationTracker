@echo off

set CLASSPATH=./
if not defined CITATION_TRACKER_HOME set CITATION_TRACKER_HOME=%~dp0%

FOR %%F IN (%CITATION_TRACKER_HOME%\lib\*.jar) DO call :addcp %%F
goto extlibe

:addcp
set CLASSPATH=%CLASSPATH%;%1
goto :eof

:extlibe
%CITATION_TRACKER_HOME%\jre\bin\java -Xms256m -Xmx1024m  -classpath %CLASSPATH%  kr.co.topquadrant.citationTracker.Executor %CITATION_TRACKER_HOME%
