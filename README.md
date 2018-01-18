domestic
========

A domestic robot has the goal of serving beer to its owner. Its
mission is quite simple, it just receives some beer requests from the
owner, goes to the fridge, takes out a bottle of beer, and brings it
back to the owner.  However, the robot should also be concerned with
the beer stock (and eventually order more beer using the supermarket. Its
home delivery service) and some rules hard-wired into the robot by
the Department of Health (in this example this rule defines the limit
of daily beer consumption).

This is basically the domestic robot as provided with JASON

see http://jason.sourceforge.net/

But the agent was pulled out of the JASON infrastructure to provide a bare-bones EIS interface implementation.

Dependency information 
=====================

```
<repository>
 <id>eishub-mvn-repo</id>
 <url>https://raw.github.com/eishub/mvn-repo/master</url>
</repository>
```
	
```	
<dependency>
	<groupId>eishub</groupId>
	<artifactId>Domestic</artifactId>
	<version>1.3.0</version>
</dependency>
```
