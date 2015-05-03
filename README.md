# Thriftc Ant Task #

Thriftc-task is an native(ish) cross-platform Ant task for the [Apache Thrift](http://incubator.apache.org/thrift/) project.  It also includes a full Java implementation of `thriftc` than can be used from the command line.

## Usage ##
  1. Put thriftc\_task.jar into your project directory.
  1. Define the task in your build file, setting classpath to the relative path to the jar:
```
<taskdef name="thriftc" classname="com.nsegment.thrift.ThriftcTask" classpath="thriftc_task.jar"/>
```
  1. Now add a task to your build file:
```
<thriftc srcdir="." includes="**/*.thrift" destdir="bin" java=""/>
```
  1. This task will build all `.thrift` files in the project directory and drop off `.java` files in `bin/gen-java`

To run `thriftc` as a standalone process (not from Ant), do
```
java -jar thriftc_task.jar -help
# or
java -cp thriftc_task.jar com.nsegment.thrift.Thriftc --help
```

All files must be in the same directory tree that `java` was run in.  `/foo.thrift` _actually_ means `./foo.thrift`.

## Usage details ##
Thriftc-task is a [MatchingTask](http://www.jajakarta.org/ant/ant-1.6.1/docs/en/manual/api/org/apache/tools/ant/taskdefs/MatchingTask.html), so it supports filesets, includes, and excludes like ` <javac/> `.

--gen flags are passed as attributes to the task, and their options are comma-separated attribute values:

```
<thriftc srcdir="." includes="**/*.thrift" destdir="bin" java="hashcode,include_prefix"/>
```

The following boolean flags are supported:
  * nowarn
  * strict
  * verbose
  * recurse
  * debug
  * quiet
    * A thriftc-task addition, ` quiet="true" ` will discard all output, regardless of the settings of other flags.

The following languages and options are supported:
  * as3 (bindable)
  * cocoa (log\_unexpected)
  * cpp (dense, include\_prefix)
  * csharp
  * erl
  * hs
  * html
  * java (beans, private-members, nocamel, hashcode)
  * js
  * ocaml
  * perl
  * php (inlined, server, autoload, oop, rest)
  * py (new\_style, twisted)
  * rb
  * st
  * xsd

## Implementation details ##
Rather than being a native Java implementation, Thriftc-task uses code translated from a MIPS build of `thriftc` to Java byte code by the [NestedVM project](http://nestedvm.ibex.org/) and a modified Unix runtime.  It's essentially emulated MIPS code, but in a compiled form.  While it's noticeably slower than a native executable or native Java code, it's far easier to perfectly match existing behavior or change behavior to match a specific build.

The build process has several phases:
  1. Build NestedVM with C++ support
  1. Check out Apache Thrift source.
  1. Make miscellaneous changes to NestedVM headers
  1. Use a custom makefile (yes, make.  Oh, the irony) to cross-compile `thriftc` into a statically-linked MIPS executable.
  1. Use NestedVM to generate a `.java` class file.
  1. Drop it off in the thriftc-task project and build!

## Limitations ##
  * The source and destination directors must be somewhere within the project root directory.  Doing `srcdir="../"` will cause a build exception.
    * This is a side effect of supporting Windows.  The original `thriftc` code makes some assumptions that it's running under either Cygwin or nix, so the runtime environment translates calls.  Windows effectively has multiple roots (` c:\, d:\`), and handling that would have been a pain, so all directories are rooted in the project directory.
  * It's a little slow, but tolerable.  This is because the code is emulated.
  * It doesn't detect if source files need to be rebuilt, so it always rebuilds.  Part of the reason is detecting modification times requires parsing the file to find its includes.
