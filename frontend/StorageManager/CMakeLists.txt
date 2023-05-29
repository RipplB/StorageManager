cmake_minimum_required(VERSION 3.16)

project(StorageManager VERSION 0.1 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD_REQUIRED ON)

find_package(Qt6 6.4 REQUIRED COMPONENTS Quick)

qt_standard_project_setup()

qt_add_executable(appStorageManager
    main.cpp
)

qt_add_qml_module(appStorageManager
    URI StorageManager
    VERSION 1.0
    QML_FILES Main.qml
    SOURCES
    abstractresource.h
    util.h util.cpp
    restservice.h restservice.cpp
    restaccessmanager.h restaccessmanager.cpp
    loginservice.h loginservice.cpp
    productservice.h productservice.cpp
    locationservice.h locationservice.cpp
    stockservice.h stockservice.cpp
    userservice.h userservice.cpp
    reportingservice.h reportingservice.cpp
)

set_target_properties(appStorageManager PROPERTIES
    MACOSX_BUNDLE_GUI_IDENTIFIER my.example.com
    MACOSX_BUNDLE_BUNDLE_VERSION ${PROJECT_VERSION}
    MACOSX_BUNDLE_SHORT_VERSION_STRING ${PROJECT_VERSION_MAJOR}.${PROJECT_VERSION_MINOR}
    MACOSX_BUNDLE TRUE
    WIN32_EXECUTABLE TRUE
)

find_package(Qt6 REQUIRED COMPONENTS Widgets)
target_link_libraries(appStorageManager PRIVATE Qt6::Widgets)

target_link_libraries(appStorageManager
    PRIVATE Qt6::Quick
)

install(TARGETS appStorageManager
    BUNDLE DESTINATION .
    LIBRARY DESTINATION ${CMAKE_INSTALL_LIBDIR}
    RUNTIME DESTINATION ${CMAKE_INSTALL_BINDIR}
)
