![Main Status][workflow-badge-main]
![Version][version-badge]

# wildcert
Tool for requesting and renewing wildcard SSL certificates

Currently, this tool only supports LetsEncrypt as a CA and GoDaddy as a domain provider.
Please raise an issue if there are additional CAs or providers you'd like to see supported.

## Running
Run locally using
```shell
./gradlew run
```

### Usage
#### MVP 
```shell
wildcert accounts add -n name -e email -k keypair --default
wildcert accounts list

wildcert certs request -d domains -o output -a account
wildcert certs renew -d domains -a account -e expiresIn
wildcert certs list
```

#### Future
```shell
wildcert accounts update -n name -e email -k keypair --default
wildcert accounts delete

wildcert certs revoke
```

Replace the below
#### Request certificate(s)
Run the `request` command to request new wildcard certificates for 1 or more domains.
These will be written to a specified output folder.
```
Usage: wildcert request options_list
Options:
    --domains, -d -> Domains to request certificates for. (always required) { Comma-separated list }
    --output, -o -> Directory to output new certificates to. (always required) { String }
    --ca [LETSENCRYPT] -> Certificate authority to use. { Value should be one of [letsencrypt] }
    --provider [GODADDY] -> Domain provider to use. { Value should be one of [godaddy] }
    --help, -h -> Usage info
```

### Renew certificate(s)
Run the `renew` command to renew all certificates in a specific source folder due to expire within a specified number of days.
```
Usage: wildcert renew options_list
Options:
    --domains, -d -> Domains to renew certificates for. Defaults to all. { Comma-separated list }
    --source, -s -> Directory read certificates to renew from. (always required) { String }
    --expiresIn, -e [7] -> Renew all certs expiring in this number of days { Int }
    --ca [LETSENCRYPT] -> Certificate authority to use. { Value should be one of [letsencrypt] }
    --provider [GODADDY] -> Domain provider to use. { Value should be one of [godaddy] }
    --help, -h -> Usage info
```

## Tests
To run unit tests:
```shell
./gradlew test
```

To run integration tests:
```shell
./gradlew integrationTest
```

## Releasing
Tagging a commit with a semantic version (e.g. vx.x.x) will start the workflow.
This will build the application, create a GitHub release, and build a jar file.

### Updating the running application
Once the new image has been pushed by the step above, you can update the running container.
You should be logged in as the `docker` user created above for these steps.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

New features, fixes, and bugs should be branched off of main.

Please make sure to update tests as appropriate.

## License
[MIT][mit-license]

[workflow-badge-main]: https://img.shields.io/github/workflow/status/lucystevens/wildcert/test/main?label=main
[version-badge]: https://img.shields.io/github/v/release/lucystevens/wildcert
[mit-license]: https://choosealicense.com/licenses/mit/