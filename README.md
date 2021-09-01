# TimeLog

Desktop application to manage time registrations for employees.

[Documentation](doc/README.md)

## Build and running

### Build actions

| Action | Command |
| ------ | ------- |
| Clean  | `./gradlew clean` |
| Build  | `./gradlew build` |
| Run    | `./gradlew run`   |

### Run the application

When you run the application then it will use/generate the following local files:

| Name | Destination | Description |
| ---- | ----------- | ----------- |
| H2 database | <data_dir>/.timelog/<version>/data.mv.db | Database with time registrations and setup |
| User States | <data_dir>/.timelog/<version>/user-states.json | File with states used by the UI to remember sizes and positions about the UI |
| Log file | <current_dir>/logs/timelog.log | Log file |

`<data_dir>` is defined by the OS:

| OS       | Destination |
| -------- | ----------- |
| Linux    | <user_dir>/.timelog/<version> |
| Mac OS X | <user_dir>/Library/Application Support/TimeLog/<version> |

It's possible to overwrite the `data_dir` for running the application with local data. This is mainly done by developers
that needs to test the application locally.

If you want to run it with local data then the command is:
```
./gradlew run -Dtimelog.os.name=localOS -Dtimelog.local.directory=<path-local-data>
```
The database will be created or migrated under `<path-local-data>`. `<path-local-data>` can both be a relative or a 
complete path.
