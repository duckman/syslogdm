SyslogDM
========
Frontend for displaying your syslog generated by syslog-ng's mongodb plugin
Example: https://www.serverdm.net/syslog/
Install
-------
git clone https://github.com/duckman/syslogdm.git
cd syslogdm
ant
cp dist/syslog.war <insert where ever>
Config syslog-ng
----------------
append
```
destination d_mongodb { mongodb(); };
log { source(src); destination(d_mongodb); };
```
to syslog-ng.conf. Your src might be different, look ath the other log lines for examples.