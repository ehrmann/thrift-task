On a Linux machine with `debootstrap` installed, do

    # Set up the build environment
    ./mk-chroot.sh

    # Build nestedvm, the mips toolchain, and thriftc
    sudo chroot thriftc-build-jail bash -c 'cd /usr/src/thriftc && make'

    # Compile the mips binary to Java, then package it in a jar
    THRIFTC_TMP="$(mktemp -d)"
    THRIFTC_PATH="${THRIFTC_TMP}/com/nsegment/thrift"
    mkdir -p "${THRIFTC_PATH}"
    java -cp thriftc-build-jail/usr/src/nestedvm/nestedvm.jar \
        org.ibex.nestedvm.Compiler \
            -o unixRuntime,lessConstants,maxInsnPerMethod=256 \
            -outfile "${THRIFTC_PATH}/Thriftc.class" \
            com.nsegment.thrift.Thriftc \
            thriftc-build-jail/usr/src/thriftc/build/thrift
    jar cvf ../repo/com/nsegment/thrift/thriftc-executable/0.0.2/thriftc-executable-0.0.2.jar -C "${THRIFTC_TMP}" .

    # Copy the nestedvm Unix runtime into the project repo
    cp thriftc-build-jail/usr/src/nestedvm/unix_runtime.jar ../repo/org/ibex/nestedvm/nestedvm-unix-runtime/0.0.2/nestedvm-unix-runtime-0.0.2.jar

    # To improve performance...
    java -jar lib/proguard.jar -target 6 -dontobfuscate -injars thriftc-executable-0.0.2.jar  -outjars foo2.jar -dontwarn -keep 'public class com.nsegment.thrift.Thriftc { public static void main(java.lang.String[]); }' -dontshrink -dontoptimize
    # There's also an optimization where thriftc is built to support only one platform
