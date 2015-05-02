On a Linux machine with `debootstrap` installed, do

    ./mk-chroot.sh
    sudo chroot thriftc-build-jail bash -c 'cd /usr/src/thriftc && make'
    java -cp thriftc-build-jail/usr/src/nestedvm/nestedvm.jar \
        org.ibex.nestedvm.Compiler \
            -o unixRuntime,lessConstants,maxInsnPerMethod=256 \
            -outfile .../Thriftc.class \
            foo.bar.Thriftc \
            thriftc-build-jail/usr/src/thriftc/build/thrift
    cp thriftc-build-jail/usr/src/nestedvm/unix_runtime.jar ...
