all: thriftc_mips
.PHONY: nestedvm thriftc_mips

# The runtimes are built against a branch with runtime fixes
# The compiler is build against a branc that adds support for .rel.dyn sections
# These branches should probably be merged together
nestedvm:
	cd .. && git clone https://github.com/ehrmann/nestedvm.git
	cd ../nestedvm && git reset --hard origin/gcc-4.8.3-newlib-1.20.0 && make all nestedvm.jar cxxtest
	cd ../nestedvm && git reset --hard origin/windows-runtime-compat3 && make unix_runtime.jar runtime.jar
	cp -R ../nestedvm/upstream/install /opt/mips-unknown-elf

thriftc_mips: nestedvm
	cd .. && wget -O thrift-0.9.2.tar.gz https://github.com/apache/thrift/archive/0.9.2.tar.gz
	cd .. && tar -xvzf thrift-0.9.2.tar.gz
	export QUILT_PATCHES="${PWD}/patches-thrift" && cd ../thrift-0.9.2 && quilt push -a
	mkdir build
	cd build && cmake -DCMAKE_TOOLCHAIN_FILE=../toolchain-mips-nestedvm-thrift.cmake ../../thrift-0.9.2/compiler/cpp
	cd build && make

