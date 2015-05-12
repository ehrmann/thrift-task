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

        String outputDir = folder.getRoot().getCanonicalPath().replace(File.separatorChar, '/');
        String src = folder.getRoot().getCanonicalPath().replace(File.separatorChar, '/') + "/struct.thrift";

        String os = System.getProperty("os.name");
        if (os != null && os.toLowerCase().startsWith("windows")) {
            if (outputDir.matches("^[A-Za-z]:.*")) {
                outputDir = new StringBuilder(outputDir)
                        .replace(0, 1, outputDir.substring(0, 1).toLowerCase())
                        .insert(0, "/")
                        .toString();
            }
            if (src.matches("^[A-Za-z]:.*")) {
                src = new StringBuilder(src)
                        .replace(0, 1, src.substring(0, 1).toLowerCase())
                        .insert(0, "/")
                        .toString();
            }
        }

        String[] args = new String [] {
                "-o",
                outputDir,
                "--gen",
                "js",
                src,
        };

        Thriftc thrift = new Thriftc();
        assertEquals(0, thrift.run("thrift", args));

        // TODO: finish
        // String js = FileUtils.readFileToString(new File(new File(folder.getRoot(), "gen-js"), "struct_types.js"), "ASCII");
    }
}
