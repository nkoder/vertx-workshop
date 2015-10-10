vertx-workshop
==============

run
---

* Run main class defined in `pom.xml`: `java -jar target/workshop-0.0.1-SNAPSHOT-jar-with-dependencies.jar`
* Run verticle defined in `Starter` class: `vertx run io.vertx.workshop.boot._01_helloWorld.Main_01_helloWorld -cp target/workshop-0.0.1-SNAPSHOT-jar-with-dependencies.jar`

It's recommended to have one 'starter' verticle which deploys other verticles.

blocking time limit
-------------------
If we need to do sth that blocks thread for longer time (like 2 seconds and more)
then it's preferable to use "workers".

event bus
---------

It's possible to have many servers and even separate webclient connected to same vent bus.
It means that it is possible to communicate webclient with server without REST API, using only event bus.

logger
------

Vert.x uses JUL logger by default. Do not use it on production because it is very slow.

metrics
-------

There are metrics available in Vert.x.
