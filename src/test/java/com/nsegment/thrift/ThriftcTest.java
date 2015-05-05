package com.nsegment.thrift;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.BuildException;
import org.ibex.nestedvm.*;
import org.ibex.nestedvm.Runtime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class ThriftcTest {

    public static final String[] GENERATORS = {
            "as3",
            "c_glib",
            "cocoa",
            "cpp",
            "csharp",
            "d",
            "delphi",
            "erl",
            "go",
            "hs",
            "html",
            "java",
            "javame",
            "js",
            "json",
            "lua",
            "ocaml",
            "perl",
            "php",
            "py",
            "rb",
            "st",
            "xsd",
    };

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

}
