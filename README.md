# IT Developer

Desktop application IT developers with time registration, password storage and other common features useful for 
working as an IT Developer. 

[Documentation](doc/README.md)

## Build and running

### Prerequisites

1. Java 17 with JavaFX

The solution has been developed and tested with `Java Liberica 17.0.4.1.fx` which can be installed with
```
sdk install java 17.0.4.1.fx-librca
```
with the [SDK Man Tool](https://sdkman.io/)

### Build actions

| Action | Command           |
| ------ |-------------------|
| Clean  | `./gradlew clean` |
| Build  | `./gradlew build` |
| Run    | `./gradlew run`   |

### Run the application

When you run the application then it will use/generate the following local files:

| Name        | Destination                           | Description                                                                  |
|-------------|---------------------------------------|------------------------------------------------------------------------------|
| H2 database | `<data_dir>/data.mv.db`               | Database with time registrations and setup                                   |
| User States | `<data_dir>/user-states.json`         | File with states used by the UI to remember sizes and positions about the UI |
| Log file    | `<current_dir>/logs/it-developer.log` | Log file                                                                     |

`<data_dir>` is defined by the OS:

| OS       | Destination                                                     |
| -------- |-----------------------------------------------------------------|
| Linux    | `<user_dir>/.it-developer/<version>`                            |
| Mac OS X | `<user_dir>/Library/Application Support/IT-Developer/<version>` |

It's possible to overwrite the `data_dir` for running the application with local data. This is mainly done by developers
that needs to test the application locally.

If you want to run it with local data then the command is:
```
./gradlew run -Dapp.os.name=localOS -Dapp.local.directory=<path-local-data>
```
The database will be created or migrated under `<path-local-data>`. `<path-local-data>` can both be a relative or a 
complete path.
