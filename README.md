# Orbit Simulator

This is a small, casual project that simulates orbits in 2D space using classical mechanics. It was written in Java using AWT and was developed as a small solo side project in Eclipse.

## Features

- **Mass Addition**: Click anywhere on the screen to add masses.
- **Custom Mass Properties**: Define custom properties of the masses (mass, initial velocity, radius) through the command line during runtime.
- **2D Camera Control**: Use the WASD keys to control the 2D camera view.
- **Time Speed Control**: Adjust the speed of time passing using the up/down arrow keys.
- **Camera Movement Speed**: Increase or decrease the camera movement speed with the left/right arrow keys.

## Running the Project

To run the project:

1. Ensure that Java and Maven are installed on your system.
2. Download the repository ZIP file.
3. Unzip the file.
4. Open a terminal and navigate to the root directory of the project.
5. Execute the following commands:

    ```sh
    mvn clean package
    java -jar target/orbit-1.0.jar
    ```

## Notes

- This project is a casual and small-scale endeavor, so the code may not be very organized and contains some hardcoded elements.
- There are plans to expand and improve the project in the future.
