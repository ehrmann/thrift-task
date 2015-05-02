package com.nsegment.thrift;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ThriftTaskTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testIsValidAntTask() {
        new Project().checkTaskClass(ThriftTask.class);
    }

    @Test
    public void testSimple() throws IOException {
        FileUtils.copyInputStreamToFile(
                this.getClass().getResourceAsStream("simple.thrift"),
                folder.newFile("simple.thrift"));

        File buildFile = folder.newFile("build.xml");
        FileUtils.copyInputStreamToFile(
                this.getClass().getResourceAsStream("simple.xml"),
                buildFile);

        ProjectHelper helper = ProjectHelper.getProjectHelper();
        Project project = new Project();
        helper.parse(project, buildFile);
        project.setProperty("ant.file", buildFile.getAbsolutePath());

        project.init();
        project.setBaseDir(folder.getRoot());
        project.executeTarget("build");

        Set<String> files = new HashSet<String>(Arrays.asList(folder.getRoot().list()));
        files.remove("build.xml");
        files.remove("simple.thrift");

        String prefix = folder.getRoot().getCanonicalPath();
        Set<String> fileSet = new TreeSet<String>();

        for (File file : FileUtils.listFiles(folder.getRoot(), null, true)) {
            String fileStr = file.getCanonicalPath();
            assertTrue(fileStr.startsWith(prefix));
            fileSet.add(fileStr.substring(prefix.length()));
        }

        assertEquals(new TreeSet<String>(Arrays.asList(new String[]{
                "/build.xml",
                "/simple.thrift",
                "/gen-java/UserProfile.java",
                "/gen-java/UserStorage.java",
                "/gen-perl/Constants.pm",
                "/gen-perl/Types.pm",
                "/gen-perl/UserStorage.pm",
                "/gen-php/Types.php",
                "/gen-php/UserStorage.php",
                "/gen-py/__init__.py",
                "/gen-py/simple/__init__.py",
                "/gen-py/simple/constants.py",
                "/gen-py/simple/ttypes.py",
                "/gen-py/simple/UserStorage-remote",
                "/gen-py/simple/UserStorage.py",
                "/gen-rb/simple_constants.rb",
                "/gen-rb/simple_types.rb",
                "/gen-rb/user_storage.rb",
        })), fileSet);
    }

    @Test
    public void testEachGenerator() throws IOException {
        for (String generator : ThriftcTest.GENERATORS) {
            File testFolder = folder.newFolder(generator + "-test");

            //File thriftFile = new File(testFolder, "struct.thrift");
            FileUtils.copyInputStreamToFile(
                    this.getClass().getResourceAsStream("struct.thrift"),
                    new File(testFolder, "struct.thrift"));

            String build = IOUtils.toString(this.getClass().getResourceAsStream("generator.xml"), "UTF-8");
            build = build.replace("GENERATOR", generator);

            File buildFile = new File(testFolder, "build.xml");
            FileUtils.write(buildFile, build, "UTF-8");

            ProjectHelper helper = ProjectHelper.getProjectHelper();
            Project project = new Project();
            helper.parse(project, buildFile);
            project.setProperty("ant.file", buildFile.getAbsolutePath());

            project.init();
            project.setBaseDir(testFolder.getAbsoluteFile());
            project.executeTarget("build");


            Set<String> generatedDirs = new TreeSet<String>();

            File[] list = testFolder.listFiles();
            if (list != null) {
                for (File file : list) {
                    if (file.isDirectory() && file.getName().startsWith("gen-")) {
                        generatedDirs.add(file.getName());
                    }
                }
            }

            assertEquals(Collections.singleton("gen-" + generator), generatedDirs);
        }
    }

    @Test
    public void testRelativePaths0() throws IOException {
        System.out.println(this.folder.getRoot());
        File project0 = this.folder.newFolder("project0");
        File project1 = this.folder.newFolder("project1");
        File artifacts = this.folder.newFolder("artifacts");

        FileUtils.copyInputStreamToFile(
                this.getClass().getResourceAsStream("wrapper.thrift"),
                new File(project0, "wrapper.thrift"));

        FileUtils.copyInputStreamToFile(
                this.getClass().getResourceAsStream("struct.thrift"),
                new File(project1, "struct.thrift"));

        File buildFile = new File(project0, "build.xml");
        FileUtils.copyInputStreamToFile(
                this.getClass().getResourceAsStream("relative_paths0.xml"),
                buildFile);

        ProjectHelper helper = ProjectHelper.getProjectHelper();
        Project project = new Project();
        helper.parse(project, buildFile);
        project.setProperty("ant.file", buildFile.getAbsolutePath());

        project.init();
        project.setBaseDir(project0);
        project.executeTarget("build");

        String prefix = artifacts.getCanonicalPath();
        Set<String> fileSet = new TreeSet<String>();

        for (File file : FileUtils.listFiles(artifacts, null, true)) {
            String fileStr = file.getCanonicalPath();
            assertTrue(fileStr.startsWith(prefix));
            fileSet.add(fileStr.substring(prefix.length()));
        }

        assertEquals(new TreeSet<String>(Arrays.asList(new String[]{
                "/gen-java/UserProfileResponse.java",
        })), fileSet);
    }

    @Test
    public void testRelativePaths1() throws IOException {
        System.out.println(this.folder.getRoot());
        File thrift = this.folder.newFolder("thrift");
        File artifacts = this.folder.newFolder("artifacts");
        File control = this.folder.newFolder("control");

        FileUtils.copyInputStreamToFile(
                this.getClass().getResourceAsStream("wrapper.thrift"),
                new File(thrift, "wrapper.thrift"));

        FileUtils.copyInputStreamToFile(
                this.getClass().getResourceAsStream("struct.thrift"),
                new File(thrift, "struct.thrift"));

        File buildFile = new File(control, "build.xml");
        FileUtils.copyInputStreamToFile(
                this.getClass().getResourceAsStream("relative_paths1.xml"),
                buildFile);

        ProjectHelper helper = ProjectHelper.getProjectHelper();
        Project project = new Project();
        helper.parse(project, buildFile);
        project.setProperty("ant.file", buildFile.getAbsolutePath());

        project.init();
        project.setBaseDir(control);
        project.executeTarget("build");

        String prefix = artifacts.getCanonicalPath();
        Set<String> fileSet = new TreeSet<String>();

        for (File file : FileUtils.listFiles(artifacts, null, true)) {
            String fileStr = file.getCanonicalPath();
            assertTrue(fileStr.startsWith(prefix));
            fileSet.add(fileStr.substring(prefix.length()));
        }

        assertEquals(new TreeSet<String>(Arrays.asList(new String[]{
                "/gen-java/UserProfileResponse.java",
                "/gen-java/UserProfile.java",
        })), fileSet);
    }

    @Test
    public void testRelativePaths2() throws IOException {
        System.out.println(this.folder.getRoot());
        File thrift = this.folder.newFolder("thrift");
        File control = this.folder.newFolder("control");

        FileUtils.copyInputStreamToFile(
                this.getClass().getResourceAsStream("struct.thrift"),
                new File(thrift, "struct.thrift"));

        File buildFile = new File(control, "build.xml");
        FileUtils.copyInputStreamToFile(
                this.getClass().getResourceAsStream("relative_paths2.xml"),
                buildFile);

        ProjectHelper helper = ProjectHelper.getProjectHelper();
        Project project = new Project();
        helper.parse(project, buildFile);
        project.setProperty("ant.file", buildFile.getAbsolutePath());

        project.init();
        project.setBaseDir(this.folder.getRoot());
        project.executeTarget("build");

        String prefix = this.folder.getRoot().getCanonicalPath();
        Set<String> fileSet = new TreeSet<String>();

        for (File file : FileUtils.listFiles(this.folder.getRoot(), null, true)) {
            String fileStr = file.getCanonicalPath();
            assertTrue(fileStr.startsWith(prefix));
            fileSet.add(fileStr.substring(prefix.length()));
        }

        assertEquals(new TreeSet<String>(Arrays.asList(new String[]{
                "/thrift/struct.thrift",
                "/control/build.xml",
                "/gen-java/UserProfile.java",
        })), fileSet);
    }

    @Test
    public void testNegKeys() throws IOException {
        FileUtils.copyInputStreamToFile(
                this.getClass().getResourceAsStream("simple.thrift"),
                folder.newFile("simple.thrift"));

        File buildFile = folder.newFile("build.xml");
        FileUtils.copyInputStreamToFile(
                this.getClass().getResourceAsStream("allow_neg_keys.xml"),
                buildFile);

        PrintStream oldSystemOut = System.out;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));

            ProjectHelper helper = ProjectHelper.getProjectHelper();
            Project project = new Project();
            helper.parse(project, buildFile);
            project.setProperty("ant.file", buildFile.getAbsolutePath());

            project.init();
            project.setBaseDir(folder.getRoot());
            project.executeTarget("build");

            String outStr = new String(out.toByteArray(), "ASCII");
            assertTrue("Expected --allow-neg-keys in thrift command, got: " + outStr, outStr.contains("--allow-neg-keys"));
        } finally {
            System.setOut(oldSystemOut);
        }
    }

    @Test
    public void test64bitConsts() throws IOException {
        FileUtils.copyInputStreamToFile(
                this.getClass().getResourceAsStream("simple.thrift"),
                folder.newFile("simple.thrift"));

        File buildFile = folder.newFile("build.xml");
        FileUtils.copyInputStreamToFile(
                this.getClass().getResourceAsStream("allow_64bit_consts.xml"),
                buildFile);

        PrintStream oldSystemOut = System.out;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));

            ProjectHelper helper = ProjectHelper.getProjectHelper();
            Project project = new Project();
            helper.parse(project, buildFile);
            project.setProperty("ant.file", buildFile.getAbsolutePath());

            project.init();
            project.setBaseDir(folder.getRoot());
            project.executeTarget("build");

            String outStr = new String(out.toByteArray(), "ASCII");
            assertTrue("Expected --allow-64bit-consts in thrift command, got: " + outStr, outStr.contains("--allow-64bit-consts"));
        } finally {
            System.setOut(oldSystemOut);
        }
    }

    @Test
    public void testPythonDynImport() throws IOException {
        FileUtils.copyInputStreamToFile(
                this.getClass().getResourceAsStream("simple.thrift"),
                folder.newFile("simple.thrift"));

        File buildFile = folder.newFile("build.xml");
        FileUtils.copyInputStreamToFile(
                this.getClass().getResourceAsStream("python_dyn_import.xml"),
                buildFile);

        ProjectHelper helper = ProjectHelper.getProjectHelper();
        Project project = new Project();
        helper.parse(project, buildFile);
        project.setProperty("ant.file", buildFile.getAbsolutePath());

        project.init();
        project.setBaseDir(folder.getRoot());
        project.executeTarget("build");

        File simple = new File(new File(folder.getRoot(), "gen-py"), "simple");
        String ttypes = FileUtils.readFileToString(new File(simple, "ttypes.py"), "ASCII");

        assertTrue("ttypes.py didn't contain the dynamic import", ttypes.matches("(?ms).*^from\\s+foo\\.bar\\s+import\\s+DynBase\\s*$.*"));
        assertTrue("ttypes.py didn't contain the dynamic class", ttypes.matches("(?ms).*^class\\s+UserProfile\\s*[(]\\s*DynBase\\s*[)]\\s*:\\s*$.*"));
    }
}