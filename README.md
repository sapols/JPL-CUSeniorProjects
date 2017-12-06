Master: [![Build Status](https://travis-ci.org/RossBlassingame/JPL-CUSeniorProjects.svg?branch=master)](https://travis-ci.org/RossBlassingame/JPL-CUSeniorProjects)
Dev: [![Build Status](https://travis-ci.org/RossBlassingame/JPL-CUSeniorProjects.svg?branch=dev)](https://travis-ci.org/RossBlassingame/JPL-CUSeniorProjects)

# CU Senior Projects -- JPL Team

## M.A.R.S.
The Martian Autonomous Routing System (or MARS) is a command-line utility that, given a .tiff format elevation map, a start coordinate corresponding to the input map, a goal coordinate also corresponding to the map, and specifications for a theoretical rover (e.g. maximum incline possible), will produce two lists of coordinates describing possible paths for the rover to take to reach the goal from the start, within the bounds of its physical capabilities. Two routes are output: one assuming that the rover has the entire elevation map loaded, and that it follows an "ideal" route using a traditional path-finding algorithm, and a second that assumes the rover must discover the terrain by itself, and may include backtracking when it erroneously follows an impossible route based on this lack of information.

## Members:
 - Ross Blassingame
 - Robert Ballard
 - Shawn Polson
 - Josh Jenkins
 - Chandler Samuels
 - Dean Moser
 
## Sponsor
This project is sponsored by NASA Jet Propulsion Laboratory, with Marcel Llopis providing advice and oversight for the work.

## Usage
(obviously draft)

One can execute MARS by calling the program through a standard command line with the following possible arguments:

 - -m: Path to elevation map. Required for operation.
 - -s: Start coordinate, in format X,Y. X and Y must be integer values the correspond to the same pixel on the map. Required for operation.
 - -g: Goal coordinate, in format X,Y. X and Y must be integer values the correspond to the same pixel on the map. Required for operation.
 - -o: Output path. Required for operation.

Once MARS completes, the following files will be output in the provided output path:

 - absolutePath.csv: A comma delimited list of tuples containing the map coordinates for each step in the route with perfect information.
 - relativePath.csv: A comma delimited list of tuples containing the map coordinates for each step in the route with limited information.
 - stats.txt: A list of stats regarding the two routes. This file is human-readable.

## Copyright
Placeholder
