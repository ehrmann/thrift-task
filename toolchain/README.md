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
    jar cvf ../repo/thriftc-executable-0.0.2.jar -C "${THRIFTC_TMP}" .

    # Copy the nestedvm Unix runtime into the project repo
    cp thriftc-build-jail/usr/src/nestedvm/unix_runtime.jar ../repo/nestedvm-unix-runtime-0.0.2.jar
