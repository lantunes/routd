# Routd
------

Routd is a small and simple URL routing library for Java. It is currently not trie-based, 
and may not be as performant as trie-based implementations. However, it should be sufficient
for most cases.

## Getting Started
------------------

Download [the latest .jar](http://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=org.bigtesting&a=routd&v=LATEST).
Or, add the following dependency to your pom.xml:

```xml
<dependency>
    <groupId>org.bigtesting</groupId>
    <artifactId>routd</artifactId>
    <version>1.0.0</version>
</dependency>

## Examples
-----------

There are two classes of interest in the Routd library: **Route** and **RouteMap**.

First, you create Routes:

```java
Route r1 = new Route("/");
Route r2 = new Route("/client/:name");
Route r3 = new Route("/customer/:id<[0-9]+>");
```

...and add them to a RouteMap:

```java
RouteMap rm = new RegexRouteMap();
rm.add(r1);
rm.add(r2);
rm.add(r3);
```

Then you can retrieve the routes:

```java
assertEquals(r1, rm.getRoute("/"));
assertEquals(r2, rm.getRoute("/client/Tim"));
assertEquals(r3, rm.getRoute("/customer/123"));
```

With the routes, you can get the parameter values from a path:

```java
Route route = new Route("/customer/:id/named/:name");
String path = "/customer/1/named/John";

assertEquals("1", route.getPathParameter("id", path));
assertEquals("John", route.getPathParameter("name", path));
assertNull(route.getPathParameter("blah", path));
```

...and you can also get the path parameter elements directly:

```java
Route route = new Route("/customer/:id<[0-9]+>/named/:name");

List<PathParameterElement> params = route.pathParameterElements();
assertEquals(2, params.size());

PathParameterElement elem = params.get(0);
assertEquals("id", elem.name());
assertEquals(1, elem.index());
assertEquals("[0-9]+", elem.regex());

elem = params.get(1);
assertEquals("name", elem.name());
assertEquals(3, elem.index());
assertNull(elem.regex());
```
