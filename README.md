# TRELLO API AUTOMATION SUITE

API automation framework built using RestAssured, TestNG, and Maven

| Contents                                  |
|-------------------------------------------|
| [Setup](#setup)                           |
| - [Prerequisites](#setup)                 |
| - [Technologies Used](#technologies-used) |
| [Usage](#usage)                           |
| - [cli](#cli)                             |
| - [IDE](#ide)                             |
| - [Docker](#docker)                       |
| [Features](#features)                     |

## Setup

### Prerequisites

- Java & Maven  
  or
- Docker

### Technologies Used

- Java
- RestAssured – for API interaction
- TestNG – for test structure and execution
- Maven – for dependency management and build

## Usage

### Run the tests

1. #### cli
    - mvn clean test
        - optional parameters:
            - "-DsuiteXmlFile=board_suite.xml"
                - valid values: board_suite.xml, card_suite.xml, full_suite.xml
            - "-Djava.version=17" **based on the version of Java installed on your system*  
              **defaults to 25**  
              **11+ recommended**
2. #### IDE
    - Open the project in your IDE
        - Right-click on the test class or test method
            - Select "Run" or "Debug"
        - Right-click on the test suite file in the test-runner folder
            - Select "Run" or "Debug"

3. #### Docker
    - docker build -t trello_api_automation .
    - docker run --rm trello_api_automation

## Features

- Environment configuration via config.properties
- Request/Response logging
    - Log level can be set in the config.properties file for more detailed logging [java.util.
      logging]  
    - Log levels [SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST]
- Dockerized
- CI/CD pipeline [GitHub actions] with results displayed in the GitHub UI