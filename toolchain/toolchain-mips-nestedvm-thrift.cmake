# this one is important
SET(CMAKE_SYSTEM_NAME Generic)
#this one not so much
SET(CMAKE_SYSTEM_VERSION 1)

SET(CMAKE_SYSTEM_PROCESSOR mips)
SET(CMAKE_CROSSCOMPILING 1)

#SET(CMAKE_SHARED_LIBRARY_LINK_C_FLAGS "")
#SET(CMAKE_SHARED_LIBRARY_LINK_CXX_FLAGS "")

# specify the cross compiler
SET(CMAKE_C_COMPILER   /opt/mips-unknown-elf/bin/mips-unknown-elf-gcc)
SET(CMAKE_CXX_COMPILER /opt/mips-unknown-elf/bin/mips-unknown-elf-g++)

SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++0x -DPATH_MAX=4096 -march=mips1 -specs=/opt/mips-unknown-elf/mips-unknown-elf/lib/crt0-override.spec" CACHE STRING "CXX_FLAGS for nestedvm")
SET(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -DPATH_MAX=4096 -march=mips1 -specs=/opt/mips-unknown-elf/mips-unknown-elf/lib/crt0-override.spec" CACHE STRING "C_FLAGS for nestedvm")

# where is the target environment 
SET(CMAKE_FIND_ROOT_PATH  /opt/mips-unknown-elf)

# search for programs in the build host directories
SET(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)
# for libraries and headers in the target directories
SET(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY)
SET(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY)
