package com.nsegment.thrift;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.ibex.nestedvm.Runtime;


public class ThriftcTask extends MatchingTask {

    protected boolean nowarn = false;
    protected boolean strict = false;
    protected boolean verbose = false;
    protected boolean recurse = false;
    protected boolean debug = false;
    protected boolean quiet = false;

    protected static final String OPTION_MARKER = ":";
    protected static final String OPTION_DELIMITER = ",";
    protected static final String OPTION_ASSIGNER = "=";

    // Options are verified linearly, so switch that code to use a map if these arrays
    // get too big.
    // TODO: Really; this needs to be more programmatic
    protected static final String[] AS3_OPTIONS = { "bindable" };
    protected static final String[] C_GLIB = {};
    protected static final String[] COCOA_OPTIONS = { "log_unexpected", "validate_required" };
    protected static final String[] CPP_OPTIONS = { "cob_style", "no_client_completion", "no_default_operators", "templates", "pure_enums", "dense", "include_prefix", };
    protected static final String[] CSHARP_OPTIONS = { "async", "asyncctp", "wcf", "serial", "nullable", "hashcode", "union", };
    protected static final String[] D_OPTIONS = {};
    protected static final String[] DELPHI_OPTIONS = { "ansistr_binary", "register_types", "constprefix", "events", "xmldoc", };
    protected static final String[] ERL_OPTIONS = { "legacynames", };
    protected static final String[] GO_OPTIONS = { "package_prefix", "thrift_import", "package", };
    protected static final String[] HS_OPTIONS = {};
    protected static final String[] HTML_OPTIONS = { "standalone", };
    protected static final String[] JAVA_OPTIONS = { "beans", "private-members", "nocamel", "fullcamel", "android", "android_legacy", "java5", "reuse-objects", "sorted_containers", };
    protected static final String[] JAVAME_OPTIONS = {};
    protected static final String[] JS_OPTIONS = { "jquery", "node", "ts", };
    protected static final String[] JSON_OPTIONS = {};
    protected static final String[] LUA_OPTIONS = {};
    protected static final String[] OCAML_OPTIONS = {};
    protected static final String[] PERL_OPTIONS = {};
    protected static final String[] PHP_OPTIONS = { "inlined", "server", "oop", "rest", "nsglobal", "validate", "json", };
    protected static final String[] PY_OPTIONS = { "new_style", "twisted", "tornado", "utf8strings", "slots", "dynamic", "dynbase", "dynexc", "dynimport", };
    protected static final String[] RB_OPTIONS = { "rubygems", "namespaced", };
    protected static final String[] ST_OPTIONS = {};
    protected static final String[] XSD_OPTIONS= {};

    protected String genAs3 = null;
    protected String genCGlib = null;
    protected String genCocoa = null;
    protected String genCpp = null;
    protected String genCsharp = null;
    protected String genD = null;
    protected String genDelphi = null;
    protected String genErl = null;
    protected String genGo;
    protected String genHs = null;
    protected String genHtml = null;
    protected String genJava = null;
    protected String genJavame = null;
    protected String genJs = null;
    protected String genJson = null;
    protected String genLua = null;
    protected String genOcaml = null;
    protected String genPerl = null;
    protected String genPhp = null;
    protected String genPy = null;
    protected String genRb = null;
    protected String genSt = null;
    protected String genXsd = null;

    protected Path src = null;
    protected FileSet includePath = null;
    protected File destDir = null;

    @Override
    public void execute() throws BuildException {
        ArrayList<String> parameters = new ArrayList<String>();

        // Assorted options
        if (debug) {
            parameters.add("-debug");
        }
        if (nowarn) {
            parameters.add("-nowarn");
        }
        if (strict) {
            parameters.add("-strict");
        }
        if (verbose) {
            parameters.add("-v");
        }
        if (recurse) {
            parameters.add("-r");
        }

        // Include path
        if (this.includePath != null) {
            DirectoryScanner includePathScanner = this.includePath.getDirectoryScanner(getProject());
            String[] includePaths = includePathScanner.getIncludedDirectories();
            for (String includePath : includePaths) {
                parameters.add("-I");
                parameters.add("/" + includePath.replace(File.separatorChar, '/'));
            }
        }

        // Output directory
        if (destDir != null) {
            if (!destDir.isDirectory()) {
                throw new BuildException("destDir '" + destDir.getPath() + "' not found.");
            }

            try {
                String destDir = this.destDir.getCanonicalPath().replace(File.separatorChar, '/');
                parameters.add("-o");
                parameters.add(destDir);
            } catch (IOException e) {
                throw new BuildException("Error accessing destDir '" + destDir.getPath() + "'", e);
            }
        }
        // TODO: else use getProject().getBaseDir().getCanonicalPath()?


        int initialParamCount = parameters.size();

        // Generators
        if (genAs3 != null) {
            parameters.add("--gen");
            parameters.add(genAs3);
        }
        if (genCGlib != null) {
            parameters.add("--gen");
            parameters.add(genCGlib);
        }
        if (genCocoa != null) {
            parameters.add("--gen");
            parameters.add(genCocoa);
        }
        if (genCpp != null) {
            parameters.add("--gen");
            parameters.add(genCpp);
        }
        if (genCsharp != null) {
            parameters.add("--gen");
            parameters.add(genCsharp);
        }
        if (genD != null) {
            parameters.add("--gen");
            parameters.add(genD);
        }
        if (genDelphi != null) {
            parameters.add("--gen");
            parameters.add(genDelphi);
        }
        if (genErl != null) {
            parameters.add("--gen");
            parameters.add(genErl);
        }
        if (genGo != null) {
            parameters.add("--gen");
            parameters.add(genGo);
        }
        if (genHs != null) {
            parameters.add("--gen");
            parameters.add(genHs);
        }
        if (genHtml != null) {
            parameters.add("--gen");
            parameters.add(genHtml);
        }
        if (genJava != null) {
            parameters.add("--gen");
            parameters.add(genJava);
        }
        if (genJavame != null) {
            parameters.add("--gen");
            parameters.add(genJavame);
        }
        if (genJs != null) {
            parameters.add("--gen");
            parameters.add(genJs);
        }
        if (genJson != null) {
            parameters.add("--gen");
            parameters.add(genJson);
        }
        if (genLua != null) {
            parameters.add("--gen");
            parameters.add(genLua);
        }
        if (genOcaml != null) {
            parameters.add("--gen");
            parameters.add(genOcaml);
        }
        if (genPerl != null) {
            parameters.add("--gen");
            parameters.add(genPerl);
        }
        if (genPhp != null) {
            parameters.add("--gen");
            parameters.add(genPhp);
        }
        if (genPy != null) {
            parameters.add("--gen");
            parameters.add(genPy);
        }
        if (genRb != null) {
            parameters.add("--gen");
            parameters.add(genRb);
        }
        if (genSt != null) {
            parameters.add("--gen");
            parameters.add(genSt);
        }
        if (genXsd != null) {
            parameters.add("--gen");
            parameters.add(genXsd);
        }

        if (parameters.size() == initialParamCount) {
            throw new BuildException("No generators specified");
        }


        // Add file placeholder
        parameters.add("");
        String[] parameterArray = parameters.toArray(new String[parameters.size()]);

        ArrayList<String> filesToProcess = new ArrayList<String>();

        if (src == null) {
            throw new BuildException("srcdir attribute must be set!");
        }

        String[] srcDirs = src.list();
        for (String srcDir : srcDirs) {
            File srcDirFile = getProject().resolveFile(srcDir);
            if (!srcDirFile.exists()) {
                throw new BuildException("srcdir '" + srcDir + "' does not exist.");
            }

            String resolvedSrcDir;
            try {
                resolvedSrcDir = srcDirFile.getCanonicalPath().replace(File.separatorChar, '/');
            } catch (IOException e) {
                throw new BuildException("Failed to resolve source directory '" + srcDir + "'", e);
            }

            DirectoryScanner ds = this.getDirectoryScanner(srcDirFile);
            String[] files = ds.getIncludedFiles();
            for (String file : files) {
                file = file.replace(File.separatorChar, '/');
                filesToProcess.add((resolvedSrcDir + "/" + file).replaceAll("/+", "/"));
            }
        }

        for (String file : filesToProcess) {
            parameterArray[parameterArray.length - 1] = file;

            if (!this.quiet) {
                System.out.print("thriftc");
                for (String param : parameterArray) {
                    System.out.print(" " + param);
                }
                System.out.println();
            }

            Thriftc thriftc = new Thriftc();

            if (quiet) {
                int fd;
                fd = thriftc.addFD(new Runtime.InputOutputStreamFD(NullOutputStream.NULL_OUTPUT_STREAM));
                if (thriftc.sys_dup2(fd, 1) < 0) {
                    throw new BuildException(("Failed to redirect stdout"));
                }
                fd = thriftc.addFD(new Runtime.InputOutputStreamFD(NullOutputStream.NULL_OUTPUT_STREAM));
                if (thriftc.sys_dup2(fd, 2) < 0) {
                    throw new BuildException(("Failed to redirect stderr"));
                }
            }

            int status = thriftc.run("thriftc", parameterArray);

            if (status != 0) {
                throw new BuildException("thriftc build failed.");
            }
        }
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    public void setDestdir(File destDir) {
        this.destDir = destDir;
    }

    public void setSrcdir(Path srcDir) {
        src = srcDir;
    }

    public void setIncludePath(String includePath) {
        if (this.includePath == null) {
            this.includePath = new FileSet();
            this.includePath.setDir(getProject().getBaseDir());
        }

        this.includePath.setIncludes(includePath);
    }

    public void setNowarn(boolean nowarn) {
        this.nowarn = nowarn;
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void setRecurse(boolean recurse) {
        this.recurse = recurse;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }



    public void setAs3(String as3) {
        if (as3 != null) {
            this.genAs3 = "as3" + generateOptionString(parseGeneratorOptions(as3, AS3_OPTIONS));
        } else {
            this.genAs3 = null;
        }
    }

    public void setC_glib(String cGlib) {
        if (cGlib != null) {
            this.genCGlib = "c_glib" + generateOptionString(parseGeneratorOptions(cGlib, C_GLIB));
        } else {
            this.genCGlib = null;
        }
    }

    public void setCocoa(String cocoa) {
        if (cocoa != null) {
            this.genCocoa = "cocoa" + generateOptionString(parseGeneratorOptions(cocoa, COCOA_OPTIONS));
        } else {
            this.genCocoa = null;
        }
    }

    public void setCpp(String cpp) {
        if (cpp != null) {
            this.genCpp = "cpp" + generateOptionString(parseGeneratorOptions(cpp, CPP_OPTIONS));
        } else {
            this.genCpp = null;
        }
    }

    public void setCsharp(String csharp) {
        if (csharp != null) {
            this.genCsharp = "csharp" + generateOptionString(parseGeneratorOptions(csharp, CSHARP_OPTIONS));
        } else {
            this.genCsharp = null;
        }
    }

    public void setD(String d) {
        if (d != null) {
            this.genD = "d" + generateOptionString(parseGeneratorOptions(d, D_OPTIONS));
        } else {
            this.genD = null;
        }
    }

    public void setDelphi(String delphi) {
        if (delphi != null) {
            this.genDelphi = "delphi" + generateOptionString(parseGeneratorOptions(delphi, DELPHI_OPTIONS));
        } else {
            this.genDelphi = null;
        }
    }

    public void setErl(String erl) {
        if (erl != null) {
            this.genErl = "erl" + generateOptionString(parseGeneratorOptions(erl, ERL_OPTIONS));
        } else {
            this.genErl = null;
        }
    }

    public void setGo(String go) {
        if (go != null) {
            this.genGo = "go" + generateOptionString(parseGeneratorOptions(go, GO_OPTIONS));
        } else {
            this.genGo = null;
        }
    }

    public void setHs(String hs) {
        if (hs != null) {
            this.genHs = "hs" + generateOptionString(parseGeneratorOptions(hs, HS_OPTIONS));
        } else {
            this.genHs = null;
        }
    }

    public void setHtml(String html) {
        if (html != null) {
            this.genHtml = "html" + generateOptionString(parseGeneratorOptions(html, HTML_OPTIONS));
        } else {
            this.genHtml = null;
        }
    }

    public void setJava(String java) {
        if (java != null) {
            this.genJava = "java" + generateOptionString(parseGeneratorOptions(java, JAVA_OPTIONS));
        } else {
            this.genJava = null;
        }
    }

    public void setJavame(String javame) {
        if (javame != null) {
            this.genJavame = "javame" + generateOptionString(parseGeneratorOptions(javame, JAVAME_OPTIONS));
        } else {
            this.genJavame = null;
        }
    }

    public void setJs(String js) {
        if (js != null) {
            this.genJs = "js" + generateOptionString(parseGeneratorOptions(js, JS_OPTIONS));
        } else {
            this.genJs = null;
        }
    }

    public void setJson(String json) {
        if (json != null) {
            this.genJson = "json" + generateOptionString(parseGeneratorOptions(json, JSON_OPTIONS));
        } else {
            this.genJson = null;
        }
    }

    public void setLua(String lua) {
        if (lua != null) {
            this.genLua = "lua" + generateOptionString(parseGeneratorOptions(lua, LUA_OPTIONS));
        } else {
            this.genLua = null;
        }
    }

    public void setOcaml(String ocaml) {
        if (ocaml != null) {
            this.genOcaml = "ocaml" + generateOptionString(parseGeneratorOptions(ocaml, OCAML_OPTIONS));
        } else {
            this.genOcaml = null;
        }
    }

    public void setPerl(String perl) {
        if (perl != null) {
            this.genPerl = "perl" + generateOptionString(parseGeneratorOptions(perl, PERL_OPTIONS));
        } else {
            this.genPerl = null;
        }
    }

    public void setPhp(String php) {
        if (php != null) {
            this.genPhp = "php" + generateOptionString(parseGeneratorOptions(php, PHP_OPTIONS));
        } else {
            this.genPhp = null;
        }
    }

    public void setPy(String py) {
        if (py != null) {
            this.genPy = "py" + generateOptionString(parseGeneratorOptions(py, PY_OPTIONS));
        } else {
            this.genPy = null;
        }
    }

    public void setRb(String rb) {
        if (rb != null) {
            this.genRb = "rb" + generateOptionString(parseGeneratorOptions(rb, RB_OPTIONS));
        } else {
            this.genRb = null;
        }
    }

    public void setSt(String st) {
        if (st != null) {
            this.genSt = "st" + generateOptionString(parseGeneratorOptions(st, ST_OPTIONS));
        } else {
            this.genSt = null;
        }
    }

    public void setXsd(String xsd) {
        if (xsd != null) {
            this.genXsd = "xsd" + generateOptionString(parseGeneratorOptions(xsd, XSD_OPTIONS));
        } else {
            this.genXsd = null;
        }
    }

    protected Map<String, String> parseGeneratorOptions(String options, String[] validOptions) {
        if (options == null || options.length() == 0) {
            return Collections.emptyMap();
        }

        String[] optionArray = options.split("[,;]");

        Map<String, String> resultSet = new LinkedHashMap<String, String>();

        for (String option : optionArray) {
            String[] pair = option.split("=");
            if (pair.length == 1) {
                resultSet.put(pair[0].trim(), null);
            } else if (pair.length == 2) {
                resultSet.put(pair[0].trim(), pair[1].trim());
            } else {
                throw new BuildException("Unable to parse name-value pairs from '" + options + "'");
            }
        }

        for (String key : resultSet.keySet()) {
            boolean valid = false;
            for (String option : validOptions) {
                if (key.equals(option)) {
                    valid = true;
                    break;
                }
            }

            if (!valid) {
                throw new BuildException("Unrecognized option '" + key + "'");
            }
        }

        return resultSet;
    }

    protected String generateOptionString(Map<String, String> optionMap) {
        StringBuilder sb = new StringBuilder();
        if (optionMap.size() > 0) {
            sb.append(OPTION_MARKER);

            Iterator<Entry<String, String>> iterator = optionMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> entry = iterator.next();
                sb.append(entry.getKey());
                if (entry.getValue() != null) {
                    sb.append(OPTION_ASSIGNER);
                    sb.append(entry.getValue());
                }
                if (iterator.hasNext()) {
                    sb.append(OPTION_DELIMITER);
                }
            }
        }

        return sb.toString();
    }

    public static class NullOutputStream extends OutputStream {

        public static final NullOutputStream NULL_OUTPUT_STREAM = new NullOutputStream();

        @Override
        public void write(byte[] b, int off, int len) {
        }

        @Override
        public void write(int b) {
        }

        @Override
        public void write(byte[] b) throws IOException {
        }
    }
}
