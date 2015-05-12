# Apache Thrift Ant Task #

Thrift-task is an native(ish) cross-platform Ant task for the [Apache Thrift](https://thrift.apache.org/)
project.  It also includes a full Java implementation of `thrift` than can be used from the command line.

## Usage ##
1. Put `thrift-task-0.0.2-SNAPSHOT.jar` into your project directory.
1. Define the task in your build file, setting classpath to the relative path to the jar:
```
<taskdef name="thrift" classname="com.nsegment.thrift.ThriftTask" classpath="thrift-task-0.0.2-SNAPSHOT.jar" />
```
1. Now add a task to your build file:
```
<thrift srcdir="." includes="**/*.thrift" destdir="bin" java="" />
```
1. This task will build all `.thrift` files in the project directory and drop off `.java` files in `bin/gen-java`

To run `thrift` as a standalone process (not from Ant), do
```
java -jar thrift-task-0.0.2-SNAPSHOT.jar -help
# or
java -cp thrift-task-0.0.2-SNAPSHOT.jar com.nsegment.thrift.Thriftc -help
```

Windows users using thrift-task *outside* of Ant should use absolute paths like `/c:/Path/to/file.thrift`.  Directory
separators are forward slashes, a leading forward slash is added before the drive letter, and the drive letter is
lower-case. When using the Ant task, this conversion is handled for you.

## Usage details ##
Thrift-task is a [MatchingTask](http://www.jajakarta.org/ant/ant-1.6.1/docs/en/manual/api/org/apache/tools/ant/taskdefs/MatchingTask.html), so it supports filesets, includes, and excludes like `<javac />`.

`--gen` flags are passed as attributes to the task, and their options are comma-separated attribute values:

```
<thrift srcdir="." includes="**/*.thrift" destdir="bin" java="hashcode,include_prefix" />
```

The following boolean flags are supported:
* nowarn
* strict
* verbose
* recurse
* debug
* allowNegKeys
* allow64bitConsts
* quiet
* A thrift-task addition, `quiet="true"` will discard all output, regardless of the settings of other flags

The following languages and options are supported and behave the same as those in the the compiled `thrift` command:
* as3 (bindable)
* c_glib
* cocoa (log\_unexpected, validate_required)
* cpp (cob_style, no_client_completion, no_default_operators, templates, pure_enums, dense, include\_prefix)
* csharp (async, asyncctp, wcf, serial, nullable, hashcode, union)
* d
* delphi (ansistr_binary, register_types, constprefix, events, xmldoc)
* erl (legacynames)
* go (package_prefix, thrift_import, package)
* hs
* html (standalone)
* java (beans, private-members, nocamel, fullcamel, android, android_legacy, java5, reuse-objects, sorted_containers)
* javame
* js (jquery, node, ts)
* json
* lua
* ocaml
* perl
* php (inlined, server, oop, rest, nsglobal=NAME, validate, json)
* py (new\_style, twisted, tornado, utf8strings, slots, dynamic, dynbase=CLS, dynexc=CLS, dynimport='from foo.bar import CLS')
* rb (rubygems, namespaced)
* st
* xsd

## Implementation details ##
Rather than being a native Java implementation, thrift-task uses code translated from a MIPS build of `thrift` to Java
byte code by the [NestedVM project](http://nestedvm.ibex.org/) and a modified Unix runtime.  It's essentially emulated
MIPS code, but in a compiled form.  While it's noticeably slower than a native executable or native Java code, it's far
easier to perfectly match existing behavior or change behavior to match a specific build.

The process for building the emulated class blob is documented in [toolchain](toolchain).  Once built, jars just need
to be copied to `repo`, and `pom.xml` modified to relfect the new versions.

## Limitations ##
* It's a little slow, but tolerable.  This is because the code is emulated.
* It doesn't detect if source files need to be rebuilt, so it always rebuilds.  Part of the reason is detecting
modification times requires parsing the file to find its includes.
