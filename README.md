# Routd
------

Routd is a small and simple URL routing library for Java. It is currently not trie-based, 
and may not be as performant as trie-based implementations. However, it does contain a
tree-based implementation, which should be sufficient for most cases.

## Getting Started
------------------

Download [the latest .jar](http://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=org.bigtesting&a=routd&v=LATEST).
Or, add the following dependency to your pom.xml:

```xml
<dependency>
    <groupId>org.bigtesting</groupId>
    <artifactId>routd</artifactId>
    <version>1.0.4</version>
</dependency>
```

## Usage
-----------

There are two classes of interest in the Routd library: **Route** and **Router**.

First, you create Routes:

```java
Route r1 = new Route("/");
Route r2 = new Route("/client/:name");
Route r3 = new Route("/customer/:id<[0-9]+>");
Route r4 = new Route("/user/*/account");
```

...and add them to a Router:

```java
Router router = new TreeRouter();
router.add(r1);
router.add(r2);
router.add(r3);
router.add(r4);
```

Then you can retrieve the routes:

```java
assertEquals(r1, router.route("/"));
assertEquals(r2, router.route("/client/Tim"));
assertEquals(r3, router.route("/customer/123"));
assertEquals(r4, router.route("/user/john/account"));
```

With the routes, you can get the parameter values from a path:

```java
Route route = new Route("/customer/:id/named/:name/*");
String path = "/customer/1/named/John/Doe";

assertEquals("1", route.getNamedParameter("id", path));
assertEquals("John", route.getNamedParameter("name", path));
assertNull(route.getNamedParameter("blah", path));
assertEquals("Doe", route.splat(path)[0]);
```

...and you can also get the path parameter elements directly:

```java
Route route = new Route("/customer/:id<[0-9]+>/named/:name/*");

List<PathElement> elements = route.getPathElements();
assertEquals(5, elements.size());

StaticPathElement firstStaticElement = (StaticPathElement)elements.get(0);
assertEquals("customer", firstStaticElement.name());
assertEquals(0, firstStaticElement.index());

NamedParameterElement firstNamedElement = (NamedParameterElement)elements.get(1);
assertEquals("id", firstNamedElement.name());
assertEquals(1, firstNamedElement.index());
assertEquals("[0-9]+", firstNamedElement.regex());

NamedParameterElement secondNamedElement = (NamedParameterElement)elements.get(3);
assertEquals("name", secondNamedElement.name());
assertEquals(3, secondNamedElement.index());
assertNull(secondNamedElement.regex());

SplatParameterElement splatElement = (SplatParameterElement)elements.get(4);
assertEquals(4, splatElement.index());
```

## Notes
-----------

Routers currently expect URL paths to be undecoded. That is, paths should retain any URL encodings.
The router will handle any URL decoding required.