#! /bin/sh

DIR=${DIR:-./thriftc-build-jail}

mkdir -p $DIR || (echo "Couldn't create ${DIR}" && exit 1)
sudo true || (echo "Unable to run as root" && exit 1)
sudo which debootstrap || (echo "Unable to find debootstrap" && exit 1)

# There's an issue with jessie
sudo debootstrap wheezy $DIR http://http.debian.net/debian/

if [ `uname -o` = 'GNU/Linux' ]; then
    sudo mount -t proc proc $DIR/proc
    sudo mount -t sysfs sys $DIR/sys
    sudo mount -o bind /dev $DIR/dev
else
    echo 'Build not supported on' `uname -o` ; exit 1
fi

NESTEDVM_BUILD_DEPS='make openjdk-7-jdk gcc g++ curl libgmp-dev libmpfr-dev libmpc-dev'
THRIFT_BUILD_DEPS='libtool autoconf automake cmake flex bison'

sudo chroot $DIR apt-get update
sudo chroot $DIR apt-get install -y sudo git quilt $NESTEDVM_BUILD_DEPS $THRIFT_BUILD_DEPS
sudo mkdir -p $DIR/usr/src/thriftc
sudo cp -R Makefile patches-* toolchain-mips-nestedvm-thrift.cmake $DIR/usr/src/thriftc
