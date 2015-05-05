package com.nsegment.thrift;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ThriftcTaskTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testIsValidAntTask() {
        new Project().checkTaskClass(ThriftcTask.class);
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

    public void testRelativePaths() {

    }
}