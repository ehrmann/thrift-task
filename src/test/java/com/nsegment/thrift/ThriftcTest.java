package com.nsegment.thrift;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

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

    @Test
    public void testJsonRoundTrip() throws IOException {
        FileUtils.copyInputStreamToFile(
                this.getClass().getResourceAsStream("struct.thrift"),
                new File(folder.getRoot(), "struct.thrift"));

        String[] args = {
                "-o",
                folder.getRoot().getCanonicalPath().replace(File.separatorChar, '/'),
                "--gen",
                "js",
                folder.getRoot().getCanonicalPath().replace(File.separatorChar, '/') + "/struct.thrift",
        };

        Thriftc thrift = new Thriftc();
        assertEquals(0, thrift.run("thrift", args));

        // TODO: finish
        // String js = FileUtils.readFileToString(new File(new File(folder.getRoot(), "gen-js"), "struct_types.js"), "ASCII");
    }
}
